package com.ekoskladvalidator.Validators;


import com.ekoskladvalidator.CustomExceptions.*;
import com.ekoskladvalidator.Models.Enums.Presence;
import com.ekoskladvalidator.Models.Enums.QueryType;
import com.ekoskladvalidator.Models.PresenceMatcher;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.Models.SupplierResource;
import com.ekoskladvalidator.ParseUtils.DocQueryParser;
import com.ekoskladvalidator.RestServices.ProductRestService;
import com.ekoskladvalidator.Services.ProductService;
import com.ekoskladvalidator.Services.SupplierResourceService;
import com.ekoskladvalidator.SyncUtils.DbRestSynchronizer;
import com.ekoskladvalidator.Validators.ValidatorUtils.ProductValidatorUtils;
import lombok.Data;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Data
@Component
public class ProductValidator {

    private static Logger logger = LoggerFactory.getLogger(ProductValidator.class);

    private final ProductService productService;

    private final ProductRestService productRestService;

    private final DbRestSynchronizer dbRestSynchronizer;

    private final DocQueryParser docQueryParser;

    private final ProductValidatorUtils priceValidatorUtils;

    private final SupplierResourceService supplierResourceService;

    @Value("${validator.host-request-delay-ms:1500}")
    private long hostRequestDelayMs;

    @Value("${validator.max-retries:3}")
    private int maxRetries;

    @Value("${validator.retry-base-delay-ms:2000}")
    private long retryBaseDelayMs;

    @Value("${validator.max-concurrent-hosts:6}")
    private int maxConcurrentHosts;


    public ProductValidator(ProductService productService,
                            ProductRestService productRestService,
                            DbRestSynchronizer dbRestSynchronizer,
                            ProductValidatorUtils priceValidatorUtils,
                            SupplierResourceService supplierResourceService,
                            DocQueryParser docQueryParser) {
        this.productService = productService;
        this.productRestService = productRestService;
        this.dbRestSynchronizer = dbRestSynchronizer;
        this.priceValidatorUtils = priceValidatorUtils;
        this.supplierResourceService = supplierResourceService;
        this.docQueryParser = docQueryParser;
    }


    @Scheduled(fixedDelay = 10800000)
    public void validateProducts() throws InterruptedException {

        List<Product> syncProductList = dbRestSynchronizer.synchronizeDbProductsWithRestApiModels();

        List<Product> productListForExSave = productService.findAll().stream().
                filter(product -> {

                    boolean ndToAdd = true;

                    for (Product syncProduct : syncProductList) {
                        if (syncProduct.getId() == product.getId()) {
                            ndToAdd = false;
                            break;
                        }
                    }

                    if (!product.isDataForValidatingExist()) ndToAdd = true;

                    return ndToAdd;
                }).
                collect(Collectors.toList()).stream().map(product -> {
                    product.setValidationStatus(false);
                    return product;
                }).collect(Collectors.toList());

        List<Product> productListForValidation = productService.findAll().stream().
                filter(Product::isDataForValidatingExist).
                filter(product -> !product.getUrlForValidating().contains("aquapolis.ua")).
                filter(product -> !product.getUrlForValidating().contains("poolsmarket.com.ua")).
                filter(product -> !product.getName().contains("Kratki")).
                filter(product -> {
                    for (Product syncProduct : syncProductList) {
                        if (syncProduct.getId() == product.getId()) return true;
                    }
                    return false;
                }).
                collect(Collectors.toList()).stream().map(product -> {
                    product.setValidationStatus(false);
                    return product;
                }).collect(Collectors.toList());

        validateGroupedByHost(productListForValidation);

        List<Product> productsThatHadntBeenValidatedAfterSaving = productService.save(productListForExSave);

        List<Product> productThatShouldBeValidatingAfterSaving = productService.save(productListForValidation);

        List<Product> productThatHaveBeenValidated = productRestService.postProducts(productThatShouldBeValidatingAfterSaving);

        logger.info("Validation run summary: skipped={}, skippedAfterSave={}, toValidate={}, toValidateAfterSave={}, postedToProm={}",
                productListForExSave.size(), productsThatHadntBeenValidatedAfterSaving.size(), productListForValidation.size(),
                productThatShouldBeValidatingAfterSaving.size(), productThatHaveBeenValidated.size());

    }

    private void validateGroupedByHost(List<Product> productListForValidation) throws InterruptedException {

        Map<String, List<Product>> productsByHost = groupByHost(productListForValidation);

        if (productsByHost.isEmpty()) {
            logger.info("No products to validate in this run");
            return;
        }

        int concurrency = Math.min(maxConcurrentHosts, productsByHost.size());
        logger.info("Starting validation of {} products across {} hosts (concurrency={})",
                productListForValidation.size(), productsByHost.size(), concurrency);

        ExecutorService executor = Executors.newFixedThreadPool(concurrency);

        List<Future<?>> futures = new ArrayList<>();
        for (Map.Entry<String, List<Product>> entry : productsByHost.entrySet()) {
            futures.add(executor.submit(() -> validateHostProducts(entry.getKey(), entry.getValue())));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                logger.error("Unexpected failure while validating a host batch", e);
            }
        }

        executor.shutdown();
        logger.info("Finished validation run for {} hosts", productsByHost.size());
    }

    private Map<String, List<Product>> groupByHost(List<Product> products) {
        Map<String, List<Product>> byHost = new LinkedHashMap<>();
        for (Product product : products) {
            String host;
            try {
                host = new URL(product.getUrlForValidating()).getHost();
            } catch (MalformedURLException e) {
                host = "unknown";
            }
            byHost.computeIfAbsent(host, k -> new ArrayList<>()).add(product);
        }
        return byHost;
    }

    private void validateHostProducts(String host, List<Product> hostProducts) {
        long startedAt = System.currentTimeMillis();
        int ok = 0;
        int failed = 0;

        logger.info("[{}] validating {} products", host, hostProducts.size());

        for (Product prFV : hostProducts) {
            logger.debug("[{}] fetching product id={} url={}", host, prFV.getId(), prFV.getUrlForValidating());
            Document document;

            try {
                document = fetchWithRetry(prFV.getUrlForValidating());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("[{}] interrupted while validating product id={}", host, prFV.getId());
                return;
            } catch (Exception e) {
                failed++;
                logger.error("[{}] failed to fetch document for product id={} url={}: {}",
                        host, prFV.getId(), prFV.getUrlForValidating(), e.getMessage());
                if (!sleepBetweenRequests()) return;
                continue;
            }

            Optional<Float> newPrice = priceValidatorUtils.getValidPriceByCssQuery(document,
                    prFV.getCssQueryForValidating());

            if (newPrice.isPresent()) {
                ok++;
                prFV.setPrice(newPrice.get());
                prFV.setValidationStatus(true);
                prFV.updateLastValidationDate();
            } else {
                failed++;
                prFV.setValidationStatus(false);
                logger.warn("[{}] could not extract price for product id={} using cssQuery='{}'",
                        host, prFV.getId(), prFV.getCssQueryForValidating());
            }

            try {
                prFV.setPresence(getPresence(document, prFV));
                logger.debug("[{}] product id={} presence={}", host, prFV.getId(), prFV.getPresence());
            } catch (NoSupplierResourceException | MoreThenOneMatchingException | NotValidQueryException |
                     IOException e) {
                logger.error("[{}] failed to resolve presence for product id={}: {}", host, prFV.getId(), e.getMessage());
            } catch (NoMatchingException e) {
                prFV.setPresence(tableProductsPresence(document, prFV));
                logger.debug("[{}] no presence matcher matched product id={}, fell back to presence={}",
                        host, prFV.getId(), prFV.getPresence());
            }

            if (!sleepBetweenRequests()) return;
        }

        logger.info("[{}] finished validating {} products (ok={}, failed={}) in {} ms",
                host, hostProducts.size(), ok, failed, System.currentTimeMillis() - startedAt);
    }

    /**
     * Sleeps the configured delay between two requests to the same host.
     * @return false if the thread was interrupted while sleeping (caller should stop processing)
     */
    private boolean sleepBetweenRequests() {
        try {
            Thread.sleep(hostRequestDelayMs);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private Document fetchWithRetry(String url) throws Exception {
        Exception lastException;
        int attempt = 0;

        do {
            try {
                return docQueryParser.getDocument(url)
                        .orElseThrow(() -> new NoDocumentException("Couldn't get document from url : " + url));
            } catch (IOException | NoDocumentException e) {
                lastException = e;
                if (attempt < maxRetries) {
                    long backoffMs = retryBaseDelayMs * (1L << attempt);
                    logger.warn("Retry {}/{} for {} in {} ms: {}", attempt + 1, maxRetries, url, backoffMs, e.getMessage());
                    Thread.sleep(backoffMs);
                }
            }
            attempt++;
        } while (attempt <= maxRetries);

        throw lastException;
    }

    public Presence tableProductsPresence(Document document, Product product) {
        if (product.getUrlForValidating().contains("aquapolis.ua")) {
            Elements tableContentElements = document.select("#super-product-table tbody tr");
            if (!tableContentElements.isEmpty()) {
                boolean allUnavailable = true;
                for (Element row : tableContentElements) {
                    Elements unavailableElements = row.select(".stock.unavailable, .qty .stock.unavailable");
                    if (unavailableElements.isEmpty()) {
                        allUnavailable = false;
                        break;
                    }
                }
                return allUnavailable ? Presence.not_available : Presence.available;
            }
        }
        return Presence.not_available;
    }
//    public Presence tableProductsPresence(Document document, Product product) {
//        if(product.getUrlForValidating().contains("aquapolis.ua")) {
//            Elements tableContentElements = document.select("#super-product-table");
//            if(!tableContentElements.isEmpty()) return Presence.available;
//        }
//
//            return Presence.not_available;
//    }

    public Presence getPresence(Document document, Product product)
            throws IOException,
            NoSupplierResourceException,
            NotValidQueryException, MoreThenOneMatchingException, NoMatchingException {

        SupplierResource supplierResource = supplierResourceService.findByHostUrl(new URL(product.getUrlForValidating()).getHost())
                .orElseThrow(() -> new NoSupplierResourceException("No supplierResource for such host: " + product.getUrlForValidating()));

        List<PresenceMatcher> presenceMatchersList = new ArrayList<>();

        Set<PresenceMatcher> presenceMatcherSet;
        if (product.getAlternativePresenceMatchers().size() > 0) {
            presenceMatcherSet = product.getAlternativePresenceMatchers();
        } else {
            presenceMatcherSet = supplierResource.getPresenceMatchers();
        }

        for (PresenceMatcher presenceMatcher : presenceMatcherSet) {

            Optional<String> value = Optional.empty();

            if (presenceMatcher.getQueryType() == QueryType.X_PATH) {
                value = docQueryParser.getStringBuyXpath(document, presenceMatcher.getPresencePathQuery());
            } else if (presenceMatcher.getQueryType() == QueryType.CSS_QUERY) {
                value = docQueryParser.getFirstElementValue(document, presenceMatcher.getPresencePathQuery());
            }

            if (value.isPresent()) {
                if (value.get().contains(presenceMatcher.getContainString()))
                    presenceMatchersList.add(presenceMatcher);
            }

        }

        if (presenceMatchersList.size() == 1) {
            return presenceMatchersList.get(0).getPresence();
        } else if (presenceMatchersList.size() > 1) {
            Presence presence = presenceMatchersList.get(0).getPresence();
            boolean allMatch = presenceMatchersList.stream().allMatch(presenceMatcher -> presenceMatcher.getPresence().equals(presence));
            if (allMatch) {
                return presence;
            } else throw new MoreThenOneMatchingException("More then one status matching prodID: " + product.getId());
        } else throw new NoMatchingException("Have no one status matching prodID: " + product.getId());

    }


    public List<Product> validateOne(Product product) throws
            ImpossibleEntitySaveUpdateException, InterruptedException {

        Product syncedProduct;

        try {
            syncedProduct = dbRestSynchronizer.synchronizeOneDbProductWithRestApiModel(product);
        } catch (ImpossibleEntitySaveUpdateException e) {
            return Collections.emptyList();
        }

        if (product.isDataForValidatingExist()) {
            Document document;

            try {
                document = docQueryParser.getDocument(syncedProduct.getUrlForValidating())
                        .orElseThrow(() -> new NoDocumentException("Couldn't get document from url : " + syncedProduct.getUrlForValidating()));
            } catch (Exception e) {
                logger.error("Manual validation failed to fetch document for product id={} url={}: {}",
                        syncedProduct.getId(), syncedProduct.getUrlForValidating(), e.getMessage());
                return Collections.emptyList();
            }
            Optional<Float> newPrice = priceValidatorUtils.getValidPriceByCssQuery(document,
                    syncedProduct.getCssQueryForValidating());

            if (newPrice.isPresent()) {
                syncedProduct.setPrice(newPrice.get());
                syncedProduct.setValidationStatus(true);
                syncedProduct.updateLastValidationDate();
            } else {
                syncedProduct.setValidationStatus(false);
                logger.warn("Manual validation could not extract price for product id={} using cssQuery='{}'",
                        syncedProduct.getId(), syncedProduct.getCssQueryForValidating());
            }

            try {
                syncedProduct.setPresence(getPresence(document, syncedProduct));
                logger.info("Manual validation: product id={} presence={}", syncedProduct.getId(), syncedProduct.getPresence());
            } catch (NoSupplierResourceException | MoreThenOneMatchingException | NotValidQueryException |
                     IOException e) {
                logger.error("Manual validation failed to resolve presence for product id={}: {}",
                        syncedProduct.getId(), e.getMessage());
            } catch (NoMatchingException e) {
                syncedProduct.setPresence(tableProductsPresence(document, syncedProduct));
                logger.info("Manual validation: no presence matcher matched product id={}, fell back to presence={}",
                        syncedProduct.getId(), syncedProduct.getPresence());
            }

        } else syncedProduct.setValidationStatus(false);

        List<Product> prodListForVal = new ArrayList<>();

        prodListForVal.add(syncedProduct);


        return productRestService.postProducts(productService.save(prodListForVal));

    }


}

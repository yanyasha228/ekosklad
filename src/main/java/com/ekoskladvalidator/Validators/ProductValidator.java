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
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.*;
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


    @Scheduled(fixedDelay = 24000000)
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

        for (Product prFV : productListForValidation) {
            logger.error("Getting valid Price of product id : " + prFV.getId());
            Document document;

            try {
                document = docQueryParser.getDocument(prFV.getUrlForValidating())
                        .orElseThrow(() -> new NoDocumentException("Couldn't get document from url : " + prFV.getUrlForValidating()));
            } catch (Exception e) {
                logger.error(ExceptionUtils.getStackTrace(e));
                continue;
            }

            Optional<Float> newPrice = priceValidatorUtils.getValidPriceByCssQuery(document,
                    prFV.getCssQueryForValidating());

            if (newPrice.isPresent()) {
                logger.error("OK id : " + prFV.getId());
                prFV.setPrice(newPrice.get());
                prFV.setValidationStatus(true);
                prFV.updateLastValidationDate();
            } else prFV.setValidationStatus(false);

            try {
                logger.error("Setting PRESENCE for product : " + prFV.getUrlForValidating());
                prFV.setPresence(getPresence(document, prFV));
                logger.error("PRESENCE is : " + prFV.getPresence());
            } catch (NoSupplierResourceException | MoreThenOneMatchingException | NotValidQueryException |
                     IOException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            } catch (NoMatchingException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
                prFV.setPresence(tableProductsPresence(document, prFV));
                logger.error("PRESENCE is : " + prFV.getPresence());
            }

        }

        List<Product> productsThatHadntBeenValidatedAfterSaving = productService.save(productListForExSave);

        List<Product> productThatShouldBeValidatingAfterSaving = productService.save(productListForValidation);

        List<Product> productThatHaveBeenValidated = productRestService.postProducts(productThatShouldBeValidatingAfterSaving);

        logger.error("Products that should not have been validated amount : " + productListForExSave.size());
        logger.error("Products that should not have been validated after saving amount : " + productsThatHadntBeenValidatedAfterSaving.size());
        logger.error("Products that should have been validated amount :" + productListForValidation.size());
        logger.error("Products that should have been validated after saving amount :" + productThatShouldBeValidatingAfterSaving.size());
        logger.error("Products that have been validated amount :" + productThatHaveBeenValidated.size());

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
                logger.error(ExceptionUtils.getStackTrace(e));
                return Collections.emptyList();
            }
            Optional<Float> newPrice = priceValidatorUtils.getValidPriceByCssQuery(document,
                    syncedProduct.getCssQueryForValidating());

            if (newPrice.isPresent()) {
                syncedProduct.setPrice(newPrice.get());
                syncedProduct.setValidationStatus(true);
                syncedProduct.updateLastValidationDate();
            } else syncedProduct.setValidationStatus(false);

            try {
                logger.error("Setting PRESENCE for product : " + syncedProduct.getUrlForValidating());
                syncedProduct.setPresence(getPresence(document, syncedProduct));
                logger.error("PRESENCE is : " + syncedProduct.getPresence());
            } catch (NoSupplierResourceException | MoreThenOneMatchingException | NotValidQueryException |
                     IOException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            } catch (NoMatchingException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
                syncedProduct.setPresence(tableProductsPresence(document, syncedProduct));
                logger.error("PRESENCE is : " + syncedProduct.getPresence());
            }

        } else syncedProduct.setValidationStatus(false);

        List<Product> prodListForVal = new ArrayList<>();

        prodListForVal.add(syncedProduct);


        return productRestService.postProducts(productService.save(prodListForVal));

    }


}

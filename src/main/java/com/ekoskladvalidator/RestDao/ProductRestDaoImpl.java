package com.ekoskladvalidator.RestDao;

import com.ekoskladvalidator.Models.DTO.ProductDto;
import com.ekoskladvalidator.Models.HelpRestModels.EditProductsResponse;
import com.ekoskladvalidator.Models.HelpRestModels.ProductResponse;
import com.ekoskladvalidator.Models.HelpRestModels.ProductsListResponse;
import com.ekoskladvalidator.Models.PromApiKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductRestDaoImpl implements ProductRestDao {

    private static final Logger logger = LoggerFactory.getLogger(ProductRestDaoImpl.class);

    @Value("${rest.prom.api.token}")
    private String apiToken;

    @Value("${rest.prom.api.get.products.list.group_id}")
    private String getProductsByGroupIdUri;

    @Value("${rest.prom.api.post.products.edit}")
    private String postProductsEditUri;

    @Value("${rest.prom.api.get.products.id}")
    private String getProductByIdUri;

    @Value("${rest.prom.api.get.products.external_id}")
    private String getProductByExternalIdUri;

    private final RestTemplate restTemplate;

    public ProductRestDaoImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<ProductDto> getProductById(long id) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", String.format("Bearer %s", apiToken));
        HttpEntity<ProductResponse> entity = new HttpEntity<ProductResponse>(headers);

        logger.debug("Fetching Prom.ua product by id={}", id);
        ResponseEntity<ProductResponse> productResponseEntity = restTemplate.exchange(String.format(getProductByIdUri, id), HttpMethod.GET, entity, ProductResponse.class);

        if (productResponseEntity.getStatusCode() == HttpStatus.OK) {
            if (productResponseEntity.getBody() != null) {
                return Optional.of(productResponseEntity.getBody().getProduct());
            } else {
                logger.warn("Prom.ua returned OK with an empty body for product id={}", id);
            }
        } else {
            logger.warn("Prom.ua request failed with status={} for product id={}", productResponseEntity.getStatusCode(), id);
        }

        return Optional.empty();
    }

    @Override
    public Optional<ProductDto> getProductByIdAndApiToken(long id, PromApiKey promApiKey) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", String.format("Bearer %s", promApiKey.getApiKey()));
        HttpEntity<ProductResponse> entity = new HttpEntity<ProductResponse>(headers);

        logger.debug("Fetching Prom.ua product by id={} shop={}", id, promApiKey.getShopName());
        ResponseEntity<ProductResponse> productResponseEntity = restTemplate.exchange(String.format(getProductByIdUri, id), HttpMethod.GET, entity, ProductResponse.class);

        if (productResponseEntity.getStatusCode() == HttpStatus.OK) {
            if (productResponseEntity.getBody() != null) {
                return Optional.of(productResponseEntity.getBody().getProduct());
            } else {
                logger.warn("Prom.ua returned OK with an empty body for product id={} shop={}", id, promApiKey.getShopName());
            }
        } else {
            logger.warn("Prom.ua request failed with status={} for product id={} shop={}", productResponseEntity.getStatusCode(), id, promApiKey.getShopName());
        }

        return Optional.empty();
    }

    @Override
    public List<ProductDto> getProductsByGroupId(long groupId) {
        List<ProductDto> productDtos = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", String.format("Bearer %s", apiToken));
        HttpEntity<ProductsListResponse> entity = new HttpEntity<ProductsListResponse>(headers);

        ResponseEntity<ProductsListResponse> productsListResponseEntity = restTemplate.exchange(String.format(getProductsByGroupIdUri, groupId), HttpMethod.GET, entity, ProductsListResponse.class);

        if (productsListResponseEntity.getStatusCode() == HttpStatus.OK) {
            if (productsListResponseEntity.getBody() != null) {
                productDtos.addAll(productsListResponseEntity.getBody().getProducts());
                return productDtos;
            }
        } else {
            logger.warn("Prom.ua request failed with status={} for group id={}", productsListResponseEntity.getStatusCode(), groupId);
        }

        return Collections.emptyList();
    }


    @Override
    public List<ProductDto> postProducts(List<ProductDto> productDtos) {

        List<ProductDto> productDtoResponseList = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", String.format("Bearer %s", apiToken));

        HttpEntity<List<ProductDto>> entity = new HttpEntity<List<ProductDto>>(productDtos, headers);

        logger.debug("Posting {} products to Prom.ua, starting with id={}", productDtos.size(), productDtos.get(0).getId());
        ResponseEntity<EditProductsResponse> productsEditResponseEntity = restTemplate.exchange(postProductsEditUri, HttpMethod.POST, entity, EditProductsResponse.class);

        if (productsEditResponseEntity.getStatusCode() == HttpStatus.OK) {
            if (productsEditResponseEntity.getBody() != null) {
                List<Long> notProcessedIds = new ArrayList<>();
                for (ProductDto productDtoIter : productDtos) {
                    boolean processed = false;
                    for (Long prodUpId : productsEditResponseEntity.getBody().getProcessed_ids()) {
                        if (productDtoIter.getId() == prodUpId) {
                            productDtoResponseList.add(productDtoIter);
                            processed = true;
                            break;
                        }
                    }
                    if (!processed) notProcessedIds.add(productDtoIter.getId());
                }
                logger.debug("Prom.ua accepted {}/{} products; rejected ids={}",
                        productDtoResponseList.size(), productDtos.size(), notProcessedIds);
                return productDtoResponseList;
            } else {
                logger.warn("Prom.ua returned OK with an empty body while posting {} products", productDtos.size());
            }

        } else {
            logger.warn("Prom.ua request failed with status={} while posting {} products", productsEditResponseEntity.getStatusCode(), productDtos.size());
        }
        return Collections.emptyList();

    }

    @Override
    public List<ProductDto> postProductsByApiToken(PromApiKey promApiKey, List<ProductDto> productDtos) {

        List<ProductDto> productDtoResponseList = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", String.format("Bearer %s", promApiKey.getApiKey()));

        HttpEntity<List<ProductDto>> entity = new HttpEntity<>(productDtos, headers);

        logger.debug("Posting {} products to Prom.ua shop={}, starting with id={}", productDtos.size(), promApiKey.getShopName(), productDtos.get(0).getId());
        ResponseEntity<EditProductsResponse> productsEditResponseEntity = restTemplate.exchange(postProductsEditUri, HttpMethod.POST, entity, EditProductsResponse.class);

        if (productsEditResponseEntity.getStatusCode() == HttpStatus.OK) {
            if (productsEditResponseEntity.getBody() != null) {
                for (ProductDto productDtoIter : productDtos) {
                    for (Long prodUpId : productsEditResponseEntity
                            .getBody().getProcessed_ids()
                    ) {
                        if (productDtoIter.getId() == prodUpId) {
                            productDtoResponseList.add(productDtoIter);
                        }
                    }
                }
                logger.debug("Prom.ua shop={} accepted products with ids={}", promApiKey.getShopName(),
                        productDtoResponseList.stream().map(ProductDto::getId).collect(Collectors.toList()));
                return productDtoResponseList;
            } else {
                logger.warn("Prom.ua returned OK with an empty body while posting {} products to shop={}", productDtos.size(), promApiKey.getShopName());
            }

        } else {
            logger.warn("Prom.ua request failed with status={} while posting {} products to shop={}",
                    productsEditResponseEntity.getStatusCode(), productDtos.size(), promApiKey.getShopName());
        }
        return Collections.emptyList();

    }

    @Override
    public Optional<ProductDto> getProductByExternalIdAndApiToken(long external_id, PromApiKey promApiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", String.format("Bearer %s", promApiKey.getApiKey()));
        HttpEntity<ProductResponse> entity = new HttpEntity<ProductResponse>(headers);

        logger.debug("Fetching Prom.ua product by external_id={} shop={}", external_id, promApiKey.getShopName());
        ResponseEntity<ProductResponse> productResponseEntity = restTemplate.exchange(String.format(getProductByExternalIdUri, external_id), HttpMethod.GET, entity, ProductResponse.class);

        if (productResponseEntity.getStatusCode() == HttpStatus.OK) {
            if (productResponseEntity.getBody() != null) {
                return Optional.of(productResponseEntity.getBody().getProduct());
            } else {
                logger.warn("Prom.ua returned OK with an empty body for product external_id={} shop={}", external_id, promApiKey.getShopName());
            }
        } else {
            logger.warn("Prom.ua request failed with status={} for product external_id={} shop={}", productResponseEntity.getStatusCode(), external_id, promApiKey.getShopName());
        }

        return Optional.empty();
    }


}

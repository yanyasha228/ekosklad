package com.ekoskladvalidator.ExProductCheck;

import com.ekoskladvalidator.CustomExceptions.NoDocumentException;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.ParseUtils.DocQueryParser;
import com.ekoskladvalidator.Services.ProductService;
import com.ekoskladvalidator.Services.ProductServiceImpl;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class ExProductCheckService {

    @Autowired
    private ProductService productService;

    private RestTemplate restTemplate = new RestTemplateBuilder().build();

    @Autowired
    private DocQueryParser docQueryParser;

    private static final Logger log = LoggerFactory.getLogger(ExProductCheckService.class);

    public List<ProductCheck> checkProducts() {

        List<Product> products = productService.findAll()
                .stream()
                .filter(product -> Objects.nonNull(product.getUrlForValidating())).collect(Collectors.toList());

        log.info("Starting daily reachability check for {} products", products.size());

        List<ProductCheck> productChecks = new CopyOnWriteArrayList<>();

        for (Product product : products) {
            try {
                Optional<Document> document = docQueryParser.getDocument(product.getUrlForValidating());
                if (document.isEmpty()) {
                    log.warn("Product id={} unreachable: no document returned for url={}", product.getId(), product.getUrlForValidating());
                    productChecks.add(new ProductCheck(product, HttpStatus.NOT_FOUND, "---"));
                }
            } catch (Exception e) {
                log.warn("Product id={} unreachable: {} (url={})", product.getId(), e.getMessage(), product.getUrlForValidating());
                productChecks.add(new ProductCheck(product, HttpStatus.NOT_FOUND, e.getMessage()));
            }
        }

        log.info("Finished daily reachability check: {} of {} products unreachable", productChecks.size(), products.size());

        return productChecks;
    }


}

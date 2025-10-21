package com.ekoskladvalidator.ExProductCheck;

import com.ekoskladvalidator.CustomExceptions.NoDocumentException;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.ParseUtils.DocQueryParser;
import com.ekoskladvalidator.Services.ProductService;
import com.ekoskladvalidator.Services.ProductServiceImpl;
import com.ekoskladvalidator.Validators.ProductValidator;
import org.apache.commons.lang3.exception.ExceptionUtils;
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

    private static Logger logger = LoggerFactory.getLogger(ProductValidator.class);

    private RestTemplate restTemplate = new RestTemplateBuilder().build();

    @Autowired
    private DocQueryParser docQueryParser;


    private static final Logger log = LoggerFactory.getLogger(ExProductCheckService.class);

    public List<ProductCheck> checkProducts() {

        List<Product> products = productService.findAll()
                .stream()
                .filter(product -> Objects.nonNull(product.getUrlForValidating())).collect(Collectors.toList());

        List<ProductCheck> productChecks = new CopyOnWriteArrayList<>();

        for (Product product : products) {
            try {
                Optional<Document> document = docQueryParser.getDocument(product.getUrlForValidating());
                if (document.isEmpty()) productChecks.add(new ProductCheck(product, HttpStatus.NOT_FOUND, "---"));
            } catch (Exception e) {
                productChecks.add(new ProductCheck(product, HttpStatus.NOT_FOUND, e.getMessage()));
            }
        }

        return productChecks;
    }


}

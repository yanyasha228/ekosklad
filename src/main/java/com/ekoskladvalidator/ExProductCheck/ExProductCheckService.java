package com.ekoskladvalidator.ExProductCheck;

import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.Services.ProductService;
import com.ekoskladvalidator.Services.ProductServiceImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class ExProductCheckService {

    @Autowired
    private ProductService productService;

    private RestTemplate restTemplate = new RestTemplateBuilder().build();

    private static final Logger log = LoggerFactory.getLogger(ExProductCheckService.class);

    public List<ProductCheck> checkProducts() {

        List<Product> products = productService.findAll()
                .stream()
                .filter(product -> Objects.nonNull(product.getUrlForValidating())).collect(Collectors.toList());

        int size = products.size();
        List<ProductCheck> productChecks = new CopyOnWriteArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                + "AppleWebKit/537.36 (KHTML, like Gecko) "
                + "Chrome/113.0.0.0 Safari/537.36");
        headers.set("Accept-Language", "ru-RU,ru;q=0.9,en;q=0.8");
        headers.set("Accept", "text/html");

//        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://kratki.ua/ru/mba-17-gilotina-ru", String.class);


        products.forEach(product -> {
            try {

                log.error(product.getUrlForValidating());
//                restTemplate.getForEntity(product.getUrlForValidating(), String.class);
                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(
                        product.getUrlForValidating(),
                        HttpMethod.GET,
                        entity,
                        String.class
                );
                log.error("_____________________________________________VALID");
            } catch (Exception e) {
                productChecks.add(new ProductCheck(product, HttpStatus.NOT_FOUND, e.getMessage()));
                log.error("NOT_VALID_____________________________________________");
            } finally {
                log.error(size + "|" + counter.incrementAndGet()); // Увеличиваем счетчик после каждой итерации
            }
        });
//        for (Product product : products) {
//            try {
//                restTemplate.getForEntity(product.getUrlForValidating(), String.class);
//            } catch (HttpClientErrorException e) {
//                productChecks.add(new ProductCheck(product, e.getStatusCode()));
//            }
//        }
        return productChecks;
    }


}

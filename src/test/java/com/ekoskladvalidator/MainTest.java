package com.ekoskladvalidator;

import com.ekoskladvalidator.Models.DTO.ProductDto;
import com.ekoskladvalidator.Models.Enums.Presence;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.ObjectMappers.ProductMapper;
import com.ekoskladvalidator.ParseUtils.DocQueryParser;
import com.ekoskladvalidator.RestDao.ProductRestDao;
import com.ekoskladvalidator.RestServices.ProductRestService;
import com.ekoskladvalidator.Services.ProductService;
import com.ekoskladvalidator.Validators.ProductValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductValidator productValidator;

    @Autowired
    private DocQueryParser docQueryParser;

    @Autowired
    private ProductRestDao productRestDao;

    @Autowired
    private ProductRestService productRestService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductMapper productMapper;

    @Value("${rest.prom.api.token}")
    private String apiToken;

    @Test
    public void test() throws URISyntaxException {

        List<Product> productList = productService.findAll().stream().filter(Product::isDataForValidatingExist).collect(Collectors.toList());

        Map<String, List<Product>> stringListMap = new HashMap<>();

        for (Product product : productList) {
            String host = new URI(product.getUrlForValidating()).getHost();

            if (stringListMap.containsKey(host)) {
                stringListMap.get(host).add(product);
            } else {
                stringListMap.put(host, new ArrayList<>() {{
                    add(product);
                }});
            }
        }

        int i = 0;
    }

}

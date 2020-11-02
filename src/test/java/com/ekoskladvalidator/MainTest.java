package com.ekoskladvalidator;

import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.RestServices.ProductRestService;
import com.ekoskladvalidator.Services.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRestService productRestService;

    @Test
    public void test() throws InterruptedException {
        Product product = productService.findById(718028987).get();

        List<Product> prodList = productService.findAll().subList(0, 10);
        Product restProd = productRestService.getProduct(product).orElse(null);
        List<Product> restProdList = productRestService.getProducts(prodList);
        int i = 0;
    }

}

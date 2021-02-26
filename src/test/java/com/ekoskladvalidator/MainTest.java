package com.ekoskladvalidator;

import com.ekoskladvalidator.Models.DTO.ProductDto;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.RestDao.ProductRestDao;
import com.ekoskladvalidator.RestServices.ProductRestService;
import com.ekoskladvalidator.Services.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRestDao productRestDao;

    @Autowired
    private ProductRestService productRestService;

    @Test
    public void test() throws InterruptedException, MalformedURLException {
//        URL url = new URL("https://akvalavka.com.ua/product/assortiment:aksessuaryi_dlya_saun:rukavitsy/rukavichka-dlya-saunyi-kletka");
//        String host = url.getHost();
//        String query = url.getRef();
//        String auth = url.getAuthority();
//        String dich = "";

        ProductDto productDto = productRestDao.getProductById(1362117167).get();

        int i = 0;
    }

}

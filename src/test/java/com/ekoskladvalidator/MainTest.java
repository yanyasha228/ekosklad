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
    public void test(){

//        Product product = productService.findById(718039349).get();
//
//        ProductDto productDto = productMapper.toDto(product);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        headers.set("Authorization", String.format("Bearer %s", apiToken));
//
//        HttpEntity<ProductDto> entity = new HttpEntity<>(productDto,headers);
//        restTemplate.exchange("https://dichka.free.beeceptor.com" , HttpMethod.POST , entity , Object.class);
//        int i = 0;

        List<Product> products = productService.findAll();
    }

//    @Test
//    public void test() throws Exception {
//
//        List<Product> products = productService.findAll().stream()
//                .filter(Product::isDataForValidatingExist)
//                .filter(product -> {
//                    try {
//                        if (new URL(product.getUrlForValidating()).getHost().equalsIgnoreCase("aquapolis.ua"))
//                            return true;
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                        return false;
//                    }
//                    return false;
//                }).collect(Collectors.toList());
//
//        Map<Presence , List<Product>> presenceProductMap = new HashMap<>();
//        presenceProductMap.put(Presence.available , new ArrayList<>());
//        presenceProductMap.put(Presence.not_available , new ArrayList<>());
//        presenceProductMap.put(Presence.order , new ArrayList<>());
//        presenceProductMap.put(Presence.waiting , new ArrayList<>());
//
//
//        for (Product product : products
//        ) {
//            Presence presence = null;
//            try {
////                presence = productValidator.getPresence(product);
//            } catch (Exception e) {
//                presence = Presence.not_available;
//            }
//            presenceProductMap.get(presence).add(product);
//
//            System.out.println("Product : " + product.getUrlForValidating());
//            System.out.println("--Presence : " + presence);
//        }
////
////        Product productForVal = productService.findById(718006556).get();
////
////
////        Presence presence = productValidator.getPresence(productForVal);
//
////        URL url = new URL("https://aquapolis.ua/reshetka-dlja-dusha-aquaviva-iz-dereva.html");
////        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
////        DocumentBuilder builder = factory.newDocumentBuilder();
//////        org.w3c.dom.Document doc =  builder.newDocument();
////        Document document = queryParser.getDocument("https://store.pools.ua/catalog/osveshchenie/_par56_35_rgb_on_off_12v_ac/").get();
////
//////        W3CDom w3CDom = new W3CDom();
//////        org.w3c.dom.Document doc =  w3CDom.fromJsoup(document);
////
////        String val = queryParser.getStringBuyXpath(document, "/html/body/div[2]/div[1]/div/div/div/div/div/div[2]/ul/li/span/text()").orElse(null);
//        int i = 0;
//    }

}

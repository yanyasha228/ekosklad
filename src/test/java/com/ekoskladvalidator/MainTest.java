package com.ekoskladvalidator;

import com.ekoskladvalidator.CustomExceptions.NoSupplierResourceException;
import com.ekoskladvalidator.CustomExceptions.NotValidQueryException;
import com.ekoskladvalidator.ExProductCheck.ExProductCheckService;
import com.ekoskladvalidator.ExProductCheck.ExcelWriter;
import com.ekoskladvalidator.ExProductCheck.ProductCheck;
import com.ekoskladvalidator.Models.PresenceMatcher;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.Models.SupplierResource;
import com.ekoskladvalidator.ObjectMappers.ProductMapper;
import com.ekoskladvalidator.ParseUtils.DocQueryParser;
import com.ekoskladvalidator.RestDao.ProductRestDao;
import com.ekoskladvalidator.RestServices.ProductRestService;
import com.ekoskladvalidator.Services.ProductService;
import com.ekoskladvalidator.Services.SupplierResourceService;
import com.ekoskladvalidator.Validators.ProductValidator;
import com.ekoskladvalidator.out.EmailService;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTest {

    @Autowired
    private ExProductCheckService exProductCheckService;

    @Autowired
    private ExcelWriter excelWriter;

    @Autowired
    private ProductService productService;

    @Autowired
    private DocQueryParser docQueryParser;

    @Autowired
    private SupplierResourceService supplierResourceService;

    @Autowired
    private EmailService emailService;

    @Test
    public void check() throws IOException, MessagingException {
//        List<ProductCheck> productChecks = exProductCheckService.checkProducts();
//        excelWriter.writeToExcel(productChecks,"/home/ynddw/ekosklad/src/main/resources/dbscripts/checkProducts.xlsx");
        emailService.sendEmailWithAttachment(
                "ynddw.ynddw@gmail.com",
                "Vaidation Analisys",
                "",
                "/home/ynddw/ekosklad/src/main/resources/dbscripts/checkProducts.xlsx");
int i =0;
    }

    @Test
    public void test() throws URISyntaxException, IOException, NoSupplierResourceException, NotValidQueryException {

        Product product = productService.findById(1543444012).get();
//        Product product = productService.findById(1106697088).get();
//        Product product = productService.findById(1106697088).get();
        Document document = docQueryParser.getDocument(product.getUrlForValidating()).get();
        SupplierResource supplierResource = supplierResourceService.findByHostUrl(new URL(product.getUrlForValidating()).getHost())
                .orElseThrow(() -> new NoSupplierResourceException("No supplierResource for such host: " + product.getUrlForValidating()));
        Set<PresenceMatcher> presenceMatcherSet = supplierResource.getPresenceMatchers();

        for (PresenceMatcher presenceMatcher : presenceMatcherSet) {
            Optional<String> value = docQueryParser.getStringBuyXpath(document, presenceMatcher.getPresencePathQuery());
            int i = 0;
        }
    }
    @Test
    public void testSec() throws URISyntaxException, IOException, NoSupplierResourceException, NotValidQueryException {

//        Product product = productService.findById(1543444012).get();
////        Product product = productService.findById(1106697088).get();
////        Product product = productService.findById(1106697088).get();
        Document document = docQueryParser.getDocument("https://aquapolis.ua/flanec-svobodnyj-pvh-effast.html").get();
//        SupplierResource supplierResource = supplierResourceService.findByHostUrl(new URL(product.getUrlForValidating()).getHost())
//                .orElseThrow(() -> new NoSupplierResourceException("No supplierResource for such host: " + product.getUrlForValidating()));
//        Set<PresenceMatcher> presenceMatcherSet = supplierResource.getPresenceMatchers();

//        for (PresenceMatcher presenceMatcher : presenceMatcherSet) {
//        }
        Optional<String> value = docQueryParser.getFirstElementValue(document, "div.stock.available > span");

//        boolean dk = value.get().contains("Готов к отправке");
        int i = 0;
    }

}

package com.ekoskladvalidator.Controllers;


import com.ekoskladvalidator.CustomExceptions.ImpossibleEntitySaveUpdateException;
import com.ekoskladvalidator.ObjectMappers.ProductMapper;
import com.ekoskladvalidator.RestServices.ProductRestService;
import com.ekoskladvalidator.Services.ProductService;
import com.ekoskladvalidator.TemporaryUtils.RefactProductModelUtils;
import com.ekoskladvalidator.Validators.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    private final ProductValidator productValidator;

    public MainController(ProductValidator productValidator) {
        this.productValidator = productValidator;
    }

    @RequestMapping
    public String mainPage(Model model) {

        return "redirect:/products";

    }

    @GetMapping("sync")
    public String sync(Model model) throws InterruptedException {

        productValidator.validateProducts();

        return "redirect:/products";

    }

//    @GetMapping("ref")
//    public String refactEkoApiKeys() throws ImpossibleEntitySaveUpdateException {
////        refactProductModelUtils.setEkoskladApiKey();
//        refactProductModelUtils.tieRelatedProducts();
//        return "OK";
//    }



    @GetMapping("login")
    public String loginPage() {
        return "login";
    }




}

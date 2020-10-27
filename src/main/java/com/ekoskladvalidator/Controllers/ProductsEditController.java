package com.ekoskladvalidator.Controllers;

import com.ekoskladvalidator.CustomExceptions.ImpossibleEntitySaveUpdateException;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.Services.ProductService;
import com.ekoskladvalidator.Validators.ProductValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/products/{id}/edit")
public class ProductsEditController {

    private static final Logger logger = Logger.getLogger(ProductsEditController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductValidator productValidator;

    @GetMapping
    public String editProduct(Model model, @PathVariable Integer id) {

        Product product = productService.findById(id).orElse(null);

        model.addAttribute("product", product);

        return "editProduct";
    }

    @PostMapping("submit")
    public String editProductSubmit(Model model, @ModelAttribute(name = "product") Product product) {

        Optional<Product> appropriateProductFromDBOpt = productService.findById(product.getId());

        Product appropriateProductFromDB = appropriateProductFromDBOpt.orElse(null);

        if (appropriateProductFromDBOpt.isPresent()) {
            appropriateProductFromDB.setUrlForValidating(product.getUrlForValidating());
            appropriateProductFromDB.setCssQueryForValidating(product.getCssQueryForValidating());
        }

        try {


            productValidator.validateOne(productService.save(appropriateProductFromDB));
        } catch (ImpossibleEntitySaveUpdateException | InterruptedException e) {
            logger.warn(e);
        }

        return "redirect:/products";

    }


}

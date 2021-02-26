package com.ekoskladvalidator.Controllers;

import com.ekoskladvalidator.CustomExceptions.ImpossibleEntitySaveUpdateException;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.Services.ProductService;
import com.ekoskladvalidator.Services.PromApiKeyService;
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

    private final ProductService productService;

    private final ProductValidator productValidator;

    private final PromApiKeyService promApiKeyService;

    public ProductsEditController(ProductService productService, ProductValidator productValidator, PromApiKeyService promApiKeyService) {
        this.productService = productService;
        this.productValidator = productValidator;
        this.promApiKeyService = promApiKeyService;
    }

    @GetMapping
    public String editProduct(Model model, @PathVariable Integer id) {

        Product product = productService.findById(id).orElse(null);

        model.addAttribute("product", product);

        model.addAttribute("keys", promApiKeyService.findAll());

        return "editProduct";
    }

    @PostMapping("submit")
    public String editProductSubmit(Model model,
                                    @RequestParam Integer productId,
                                    @RequestParam String urlForValidation,
                                    @RequestParam String cssQueryForValidating,
                                    @RequestParam Integer[] keyId,
                                    @RequestParam Integer[] productApiId) throws ImpossibleEntitySaveUpdateException {

        Optional<Product> appropriateProductFromDBOpt = productService.findById(productId);

        Product savedProduct;

        if (appropriateProductFromDBOpt.isPresent()) {
            savedProduct = productService.updateValidationCredentials(productId, urlForValidation, cssQueryForValidating, keyId, productApiId);
            try {

                productValidator.validateOne(savedProduct);
            } catch (ImpossibleEntitySaveUpdateException | InterruptedException e) {
                logger.warn(e);
            }
        }

        return "redirect:/products";

    }


}

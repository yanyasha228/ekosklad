package com.ekoskladvalidator.Controllers;

import com.ekoskladvalidator.CustomExceptions.ImpossibleEntitySaveUpdateException;
import com.ekoskladvalidator.Models.Enums.Presence;
import com.ekoskladvalidator.Models.Enums.QueryType;
import com.ekoskladvalidator.Models.HelpServiceManipulationModels.EditSupplierResource;
import com.ekoskladvalidator.Models.PresenceMatcher;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.Services.ProductService;
import com.ekoskladvalidator.Services.PromApiKeyService;
import com.ekoskladvalidator.Validators.ProductValidator;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public String editProduct(Model model, @PathVariable Long id) {

        Product product = productService.findById(id).orElse(null);

        model.addAttribute("product", product);

        model.addAttribute("keys", promApiKeyService.findAll());

        return "editProduct";
    }

    @PostMapping("submit")
    public String editProductSubmit(Model model,
                                    @RequestParam Long productId,
                                    @RequestParam String urlForValidation,
                                    @RequestParam String cssQueryForValidating,
                                    @RequestParam Long[] keyId,
                                    @RequestParam Long[] productApiId,
                                    @RequestParam Long[] presenceMatcherId,
                                    @RequestParam Presence[] status,
                                    @RequestParam String[] xPathInput,
                                    @RequestParam String[] inputContainsString,
                                    @RequestParam QueryType[] queryType) throws ImpossibleEntitySaveUpdateException {

        Optional<Product> appropriateProductFromDBOpt = productService.findById(productId);

        Product savedProduct;

        if (appropriateProductFromDBOpt.isPresent()) {

            List<PresenceMatcher> presenceSet = new ArrayList<>();

            if ((presenceMatcherId.length == status.length &&
                    status.length == xPathInput.length &&
                    xPathInput.length == inputContainsString.length &&
                    inputContainsString.length == queryType.length)) {
                for (int pres = 0; pres < presenceMatcherId.length; pres++) {
                    presenceSet.add(new PresenceMatcher(presenceMatcherId[pres], status[pres], xPathInput[pres], inputContainsString[pres], queryType[pres]));
                }
            }

            savedProduct = productService.updateValidationCredentials(productId, urlForValidation, cssQueryForValidating, keyId, productApiId, presenceSet);
            try {

                productValidator.validateOne(savedProduct);
            } catch (ImpossibleEntitySaveUpdateException | InterruptedException e) {
                logger.warn(e);
            }
        }

        return "redirect:/products";

    }


}

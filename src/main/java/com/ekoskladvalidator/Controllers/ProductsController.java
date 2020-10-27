package com.ekoskladvalidator.Controllers;

import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.Services.GroupService;
import com.ekoskladvalidator.Services.ProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductsController {

    private static final Logger logger = Logger.getLogger(ProductsController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private GroupService groupService;

    @GetMapping
    public String productsList(Model model,
                               @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 5) Pageable pageable,
                               @RequestParam Optional<Boolean> validationStatus,
                               @RequestParam Optional<Integer> groupId,
                               @RequestParam Optional<String> nonFullProductName) {



        Page<Product> productsPage = productService.findProductsWithPagination(nonFullProductName.orElse(""),
                groupService.findById(groupId.orElse(0)).orElse(null),
                validationStatus.orElse(null),
                pageable);



        model.addAttribute("validationStatus", validationStatus.orElse(null));

        model.addAttribute("groupId", groupId.orElse(0));

        model.addAttribute("nonFullProductName", nonFullProductName.orElse(""));

        model.addAttribute("productsPage",
                productsPage);


        model.addAttribute("groups", groupService.findAll());

        return "products";
    }


}


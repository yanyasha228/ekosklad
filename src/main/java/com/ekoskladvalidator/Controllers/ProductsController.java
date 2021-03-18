package com.ekoskladvalidator.Controllers;

import com.ekoskladvalidator.Models.Enums.Presence;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.Models.SpecSearchModels.SearchCriteria;
import com.ekoskladvalidator.Models.SpecSearchModels.SearchSpecification;
import com.ekoskladvalidator.Services.GroupService;
import com.ekoskladvalidator.Services.ProductService;
import com.ekoskladvalidator.Services.PromApiKeyService;
import com.ekoskladvalidator.Services.SupplierResourceService;
import org.apache.log4j.Logger;
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

    private final ProductService productService;

    private final GroupService groupService;

    private final PromApiKeyService promApiKeyService;

    private final SupplierResourceService supplierResourceService;

    public ProductsController(ProductService productService, GroupService groupService, PromApiKeyService promApiKeyService, SupplierResourceService supplierResourceService) {
        this.productService = productService;
        this.groupService = groupService;
        this.promApiKeyService = promApiKeyService;
        this.supplierResourceService = supplierResourceService;
    }

    @GetMapping
    public String productsList(Model model,
                               @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 15) Pageable pageable,
                               @RequestParam Optional<Boolean> validationStatus,
                               @RequestParam Optional<Integer> groupId,
                               @RequestParam Optional<String> nonFullProductName,
                               @RequestParam Optional<Presence> presence,
                               @RequestParam Optional<String> supplierResourceHost) {


//        Page<Product> productsPage = productService.findProductsWithPagination(nonFullProductName.orElse(""),
//                groupService.findById(groupId.orElse(0)).orElse(null),
//                validationStatus.orElse(null),
//                pageable);

        SearchSpecification<Product> nameSpec = new SearchSpecification<>(new SearchCriteria("name", ":", nonFullProductName.orElse(null)));
        SearchSpecification<Product> groupSpec = new SearchSpecification<>(new SearchCriteria("group", "=", groupService.findById(groupId.orElse(0)).orElse(null)));
        SearchSpecification<Product> availabilitySpec = new SearchSpecification<>(new SearchCriteria("validationStatus", "=", validationStatus.orElse(null)));
        SearchSpecification<Product> reasonSpec = new SearchSpecification<>(new SearchCriteria("presence", "=", presence.orElse(null)));
        SearchSpecification<Product> suppResSpec = new SearchSpecification<>(new SearchCriteria("urlForValidating", ":", supplierResourceHost.orElse(null)));

        Page<Product> productsPage = productService.findAll(nameSpec.and(groupSpec).and(availabilitySpec).and(reasonSpec).and(suppResSpec), pageable);

        model.addAttribute("validationStatus", validationStatus.orElse(null));

        model.addAttribute("groupId", groupId.orElse(0));

        model.addAttribute("nonFullProductName", nonFullProductName.orElse(""));

        model.addAttribute("presence", presence.orElse(null));

        model.addAttribute("supplierResources", supplierResourceService.findAll());

        model.addAttribute("supplierResourceHost", supplierResourceHost.orElse(null));

        model.addAttribute("productsPage",
                productsPage);


        model.addAttribute("groups", groupService.findAll());

        model.addAttribute("keys", promApiKeyService.findAll());

        return "products";
    }


}


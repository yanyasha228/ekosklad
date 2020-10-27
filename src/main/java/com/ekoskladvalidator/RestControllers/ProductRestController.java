package com.ekoskladvalidator.RestControllers;

import com.ekoskladvalidator.CustomExceptions.ImpossibleEntitySaveUpdateException;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.RestServices.ProductRestService;
import com.ekoskladvalidator.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/products")
public class ProductRestController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRestService productRestService;


    @PostMapping("add")
    public void addProduct(@RequestParam Integer id) throws ImpossibleEntitySaveUpdateException {

        if (id > 0) {
            if (!productService.findById(id).isPresent()) {
                Optional<Product> productRest = productRestService.getProductById(id);
                if (productRest.isPresent()) {
                    productService.save(productRest.get());
                }
            }
        }
    }


    @GetMapping("all")
    public List<Product> productList() {
        return productService.findAll();
    }

    @GetMapping(params = "nonFullProductName")
    public List<Product> getProductsByNonFullName(@RequestParam(value = "nonFullProductName") String nonFullNameString) {

        return productService.findProductByNonFullProductNameRegardlessOfTheWordsOrder(nonFullNameString);

    }

    @GetMapping(params = "productName")
    public Product getProductByName(@RequestParam(value = "productName") String productName) {

        return productService.findProductByName(productName).orElse(null);
    }


    @GetMapping(value = "/{id}")
    public Product getProductById(@PathVariable Integer id) {

        return productService.findById(id).orElse(null);
    }

}

package com.ekoskladvalidator.RestControllers;

import com.ekoskladvalidator.CustomExceptions.ImpossibleEntitySaveUpdateException;
import com.ekoskladvalidator.Models.ModelIdApiKeyLine;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.Models.PromApiKey;
import com.ekoskladvalidator.RestServices.ProductRestService;
import com.ekoskladvalidator.Services.ModelIdApiKeyLineService;
import com.ekoskladvalidator.Services.ProductService;
import com.ekoskladvalidator.Services.PromApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/rest/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRestService productRestService;

    @Autowired
    private PromApiKeyService promApiKeyService;

    @Autowired
    private ModelIdApiKeyLineService modelIdApiKeyLineService;


    @PostMapping("add")
    @Transactional
    public void addProduct(@RequestParam Long id, @RequestParam Long keyId) throws ImpossibleEntitySaveUpdateException {

        if (Objects.nonNull(id) && id > 0 && Objects.nonNull(keyId) && keyId > 0) {
            if (!productService.findById(id).isPresent()) {
                Optional<PromApiKey> promApiKey = promApiKeyService.findById(keyId);
                if (promApiKey.isPresent()) {
                    Optional<Product> productRest = productRestService.getProductByIdAndApiToken(id, promApiKey.get());
                    if (productRest.isPresent()) {
                        Product prodToEd = productService.save(productRest.get());
                        ModelIdApiKeyLine modelIdApiKeyLine = new ModelIdApiKeyLine();
                        modelIdApiKeyLine.setProductApiId(prodToEd.getId());
                        modelIdApiKeyLine.setProduct(prodToEd);
                        modelIdApiKeyLine.setPromApiKey(promApiKey.get());
                        ModelIdApiKeyLine mod = modelIdApiKeyLineService.save(modelIdApiKeyLine);
                        prodToEd.getModelIdApiKeyLines().add(mod);
                        productService.save(prodToEd);


                    }
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
    public Product getProductById(@PathVariable Long id) {

        return productService.findById(id).orElse(null);
    }

    @PostMapping(value = "delete")
    public void deleteProductById(@RequestParam Long id) {
        productService.deleteById(id);
        int i = 0;
    }

}

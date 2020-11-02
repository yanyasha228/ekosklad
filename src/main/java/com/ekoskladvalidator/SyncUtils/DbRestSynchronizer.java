package com.ekoskladvalidator.SyncUtils;

import com.ekoskladvalidator.CustomExceptions.ImpossibleEntitySaveUpdateException;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.RestServices.ProductRestService;
import com.ekoskladvalidator.Services.ProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class DbRestSynchronizer {

    private static final Logger logger = Logger.getLogger(DbRestSynchronizer.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRestService productRestService;


    public List<Product> synchronizeDbProductsWithRestApiModels() {

        try {

            return synchronizeProducts();
        } catch (InterruptedException e) {
            logger.error(e);
        }

        return Collections.emptyList();
    }

    public Product synchronizeOneDbProductWithRestApiModel(Product productForSync) throws ImpossibleEntitySaveUpdateException {
        Optional<Product> productOpt = productRestService.getProduct(productForSync);
        if (productOpt.isPresent()) {
            return productService.save(productOpt.get());
        }
        return null;

    }


    private List<Product> synchronizeProducts() throws InterruptedException {

        return productService.save(productRestService.getAll());

    }


}

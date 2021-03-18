package com.ekoskladvalidator.RestServices;


import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.Models.PromApiKey;

import java.util.List;
import java.util.Optional;

public interface ProductRestService {

    Optional<Product> getProductById(int id);

    Optional<Product> getProductByIdAndApiToken(int id, PromApiKey promApiKey);

    List<Product> getAll() throws InterruptedException;

    List<Product> getProducts(List<Product> productList) throws InterruptedException;

    Optional<Product> getProduct(Product product);

    List<Product> getProductsByGroupId(int id);

    List<Product> postProducts(List<Product> productList) throws InterruptedException;

    Optional<Product> getProductByExternalIdAndApiToken(int external_id, PromApiKey promApiKey);


}

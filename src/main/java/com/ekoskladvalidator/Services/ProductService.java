package com.ekoskladvalidator.Services;


import com.ekoskladvalidator.CustomExceptions.ImpossibleEntitySaveUpdateException;
import com.ekoskladvalidator.Models.Group;
import com.ekoskladvalidator.Models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;


public interface ProductService {

    Product save(Product product) throws ImpossibleEntitySaveUpdateException;

    void deleteById(Long id);

    List<Product> save(List<Product> productList);

    List<Product> findAll();

    Optional<Product> findById(long id);

    Optional<Product> findProductByName(String name);


    Product updateValidationCredentials(Long productId,
                                        String urlForValidation,
                                        String cssQueryForValidating,
                                        Long[] keyId,
                                        Long[] productApiId) throws ImpossibleEntitySaveUpdateException;

    List<Product> findProductByNonFullProductNameRegardlessOfTheWordsOrder(String nonFullProductName);

    List<Product> findProductsByNameIgnoreCaseContaining(String nonFullName);

    Page<Product> findAllWithPagination(Pageable pageable);

    Page<Product> findProductsByNameIgnoreCaseContainingWithPagination(String nonFullName,
                                                                       Pageable pageable);

    Page<Product> findProductsByValidationStatusWithPagination(boolean validationStatus,
                                                               Pageable pageable);

    Page<Product> findProductsByGroupWithPagination(Group group,
                                                    Pageable pageable);

    Page<Product> findProductsByGroupAndValidationStatusWithPagination(Group group,
                                                                       boolean validationStatus,
                                                                       Pageable pageable);

    Page<Product> findProductsByNameIgnoreCaseContainingAndGroupWithPagination(String nonFullName,
                                                                               Group group,
                                                                               Pageable pageable);

    Page<Product> findProductsByNameIgnoreCaseContainingAndValidationStatusWithPagination(String nonFullName,
                                                                                          boolean validationStatus,
                                                                                          Pageable pageable);

    Page<Product> findProductsByNameIgnoreCaseContainingAndGroupAndValidationStatusWithPagination(String nonFullName,
                                                                                                  Group group,
                                                                                                  boolean validationStatus,
                                                                                                  Pageable pageable);

    Page<Product> findProductsWithPagination(String nonFullProductName,
                                             Group group,
                                             Boolean validationStatus,
                                             Pageable pageable);

    Page<Product> findAll(Specification<Product> specification, Pageable pageable);


}

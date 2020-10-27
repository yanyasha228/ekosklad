package com.ekoskladvalidator.Services;


import com.ekoskladvalidator.CustomExceptions.ImpossibleEntitySaveUpdateException;
import com.ekoskladvalidator.Models.Group;
import com.ekoskladvalidator.Models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface ProductService {

    Product save(Product product )throws ImpossibleEntitySaveUpdateException;

    List<Product> save(List<Product> productList);

    List<Product> findAll();

    Optional<Product> findById(int id);

    Optional<Product> findProductByName(String name);

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

    public Page<Product> findProductsWithPagination(String nonFullProductName,
                                                    Group group,
                                                    Boolean validationStatus,
                                                    Pageable pageable);


}

package com.ekoskladvalidator.Dao;

import com.ekoskladvalidator.Models.Group;
import com.ekoskladvalidator.Models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends JpaRepository<Product, Integer> {

    Product save(Product product);

    Optional<Product> findById(int id);

    Optional<Product> findProductByName(String name);

    List<Product> findProductsByNameIgnoreCaseContaining(String nonFullName);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findProductsByNameIgnoreCaseContaining(String nonFullName,
                                                         Pageable pageable);

    Page<Product> findProductsByGroup(Group group,
                                      Pageable pageable);

    Page<Product> findProductsByValidationStatus(boolean validationStatus,
                                                       Pageable pageable);

    Page<Product> findProductsByGroupAndValidationStatus(Group group,
                                                               boolean validationStatus,
                                                               Pageable pageable);

    Page<Product> findProductsByNameIgnoreCaseContainingAndGroup(String nonFullName,
                                                                 Group group,
                                                                 Pageable pageable);

    Page<Product> findProductsByNameIgnoreCaseContainingAndValidationStatus(String nonFullName,
                                                                                  boolean validationStatus,
                                                                                  Pageable pageable);

    Page<Product> findProductsByNameIgnoreCaseContainingAndGroupAndValidationStatus(String nonFullName,
                                                                                          Group group,
                                                                                          boolean validationStatus,
                                                                                          Pageable pageable);

}

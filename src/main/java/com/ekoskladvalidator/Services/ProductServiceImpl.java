package com.ekoskladvalidator.Services;

import com.ekoskladvalidator.CustomExceptions.ImpossibleEntitySaveUpdateException;
import com.ekoskladvalidator.Dao.ProductDao;
import com.ekoskladvalidator.Models.Group;
import com.ekoskladvalidator.Models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductDao productDao;

    @Override
    public Product save(Product product) throws ImpossibleEntitySaveUpdateException {

        if (product != null)
            if (product.getId() <= 0)
                throw new ImpossibleEntitySaveUpdateException("Attempt to update entity without ID!!!");


        return productDao.save(product);

    }

    @Override
    public List<Product> save(List<Product> productList) {

        List<Product> productListForSave = productList.stream().filter(product -> {
            if (product.getId() <= 0) {
                log.warn("Attempt to update entity without ID!!!");
                return false;
            }
            return true;

        }).collect(Collectors.toList());


        return productDao.saveAll(productListForSave);
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public Optional<Product> findById(int id) {
        return productDao.findById(id);
    }

    @Override
    public Optional<Product> findProductByName(String name) {
        return productDao.findProductByName(name);
    }

    @Override
    public List<Product> findProductByNonFullProductNameRegardlessOfTheWordsOrder(String nonFullProductName) {
        String[] searchingWords = nonFullProductName.split("\\s");

        List<Product> firstWordSearchFromDb;

        List<Product> productsThatMatch = new ArrayList<>();

        if (searchingWords.length != 0 && !searchingWords[0].equalsIgnoreCase("")) {
            firstWordSearchFromDb = productDao.findProductsByNameIgnoreCaseContaining(searchingWords[0]);
        } else return productsThatMatch;


        if (firstWordSearchFromDb.size() != 0) {
            out:
            for (Product prodForSearch : firstWordSearchFromDb) {
                for (int i = 1; i < searchingWords.length; i++) {
                    if (!prodForSearch.getName().toLowerCase().contains(searchingWords[i].toLowerCase())) continue out;
                }
                productsThatMatch.add(prodForSearch);
            }
        }


        return productsThatMatch;
    }

    @Override
    public List<Product> findProductsByNameIgnoreCaseContaining(String nonFullName) {

        return productDao.findProductsByNameIgnoreCaseContaining(nonFullName);
    }

    @Override
    public Page<Product> findAllWithPagination(Pageable pageable) {
        return productDao.findAll(pageable);
    }

    @Override
    public Page<Product> findProductsByNameIgnoreCaseContainingWithPagination(String nonFullName,
                                                                              Pageable pageable) {
        return productDao.findProductsByNameIgnoreCaseContaining(nonFullName,
                pageable);
    }

    @Override
    public Page<Product> findProductsByValidationStatusWithPagination(boolean validationStatus, Pageable pageable) {

        return productDao.findProductsByValidationStatus(validationStatus,
                pageable);
    }

    @Override
    public Page<Product> findProductsByGroupWithPagination(Group group, Pageable pageable) {

        return productDao.findProductsByGroup(group,
                pageable);
    }

    @Override
    public Page<Product> findProductsByGroupAndValidationStatusWithPagination(Group group, boolean validationStatus, Pageable pageable) {
        return productDao.findProductsByGroupAndValidationStatus(group,
                validationStatus,
                pageable);
    }

    @Override
    public Page<Product> findProductsByNameIgnoreCaseContainingAndGroupWithPagination(String nonFullName,
                                                                                      Group group,
                                                                                      Pageable pageable) {
        return productDao.findProductsByNameIgnoreCaseContainingAndGroup(nonFullName,
                group,
                pageable);
    }

    @Override
    public Page<Product> findProductsByNameIgnoreCaseContainingAndValidationStatusWithPagination(String nonFullName,
                                                                                                 boolean validationStatus,
                                                                                                 Pageable pageable) {

        return productDao.findProductsByNameIgnoreCaseContainingAndValidationStatus(nonFullName,
                validationStatus,
                pageable);
    }

    @Override
    public Page<Product> findProductsByNameIgnoreCaseContainingAndGroupAndValidationStatusWithPagination(String nonFullName, Group group, boolean validationStatus, Pageable pageable) {
        return productDao.findProductsByNameIgnoreCaseContainingAndGroupAndValidationStatus(nonFullName,
                group,
                validationStatus,
                pageable);
    }

    @Override
    public Page<Product> findProductsWithPagination(String nonFullProductName,
                                                    Group group,
                                                    Boolean validationStatus,
                                                    Pageable pageable) {

        if (nonFullProductName.replaceAll(" ", "").isEmpty()) {

            if (validationStatus == null && group != null) {

                return findProductsByGroupWithPagination(group, pageable);
            }

            if (validationStatus != null && group == null) {

                return findProductsByValidationStatusWithPagination(validationStatus, pageable);
            }
            if (validationStatus != null && group != null) {
                return findProductsByGroupAndValidationStatusWithPagination(group,
                        validationStatus,
                        pageable);
            }
        } else {

            if (validationStatus == null && group != null) {

                return findProductsByNameIgnoreCaseContainingAndGroupWithPagination(nonFullProductName, group, pageable);
            }

            if (validationStatus != null && group == null) {

                return findProductsByNameIgnoreCaseContainingAndValidationStatusWithPagination(nonFullProductName,
                        validationStatus,
                        pageable);
            }
            if (validationStatus != null && group != null) {
                return findProductsByNameIgnoreCaseContainingAndGroupAndValidationStatusWithPagination(nonFullProductName,
                        group,
                        validationStatus,
                        pageable);
            }

            return findProductsByNameIgnoreCaseContainingWithPagination(nonFullProductName, pageable);
        }

        return findAllWithPagination(pageable);
    }

}

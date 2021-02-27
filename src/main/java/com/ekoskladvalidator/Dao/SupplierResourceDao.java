package com.ekoskladvalidator.Dao;

import com.ekoskladvalidator.Models.SupplierResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierResourceDao extends JpaRepository<SupplierResource, Integer> {

    Optional<SupplierResource> findByHostUrl(String hostUrl);

}

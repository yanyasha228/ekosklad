package com.ekoskladvalidator.Services;

import com.ekoskladvalidator.Models.HelpServiceManipulationModels.CreateSupplierResource;
import com.ekoskladvalidator.Models.HelpServiceManipulationModels.EditSupplierResource;
import com.ekoskladvalidator.Models.SupplierResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

public interface SupplierResourceService {

    Page<SupplierResource> findAll(Pageable pageable);

    List<SupplierResource> findAll();

    Optional<SupplierResource> findById(Integer id);

    Optional<SupplierResource> findByHostUrl(String hostUrl);

    SupplierResource create(CreateSupplierResource createSupplierResource) throws MalformedURLException;

    SupplierResource edit(EditSupplierResource editSupplierResource) throws Exception;

    void delete(Integer id);
}

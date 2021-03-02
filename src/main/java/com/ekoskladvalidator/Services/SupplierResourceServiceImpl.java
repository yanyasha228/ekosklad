package com.ekoskladvalidator.Services;

import com.ekoskladvalidator.Dao.PresenceMatcherDao;
import com.ekoskladvalidator.Dao.SupplierResourceDao;
import com.ekoskladvalidator.Models.HelpServiceManipulationModels.CreateSupplierResource;
import com.ekoskladvalidator.Models.SupplierResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierResourceServiceImpl implements SupplierResourceService {

    private final PresenceMatcherDao presenceMatcherDao;

    private final SupplierResourceDao supplierResourceDao;

    public SupplierResourceServiceImpl(PresenceMatcherDao presenceMatcherDao, SupplierResourceDao supplierResourceDao) {
        this.presenceMatcherDao = presenceMatcherDao;
        this.supplierResourceDao = supplierResourceDao;
    }


    @Override
    public Page<SupplierResource> findAll(Pageable pageable) {
        return supplierResourceDao.findAll(pageable);
    }

    @Override
    public List<SupplierResource> findAll() {
        return supplierResourceDao.findAll();
    }

    @Override
    public Optional<SupplierResource> findById(Integer id) {
        return supplierResourceDao.findById(id);
    }

    @Override
    public Optional<SupplierResource> findByHostUrl(String hostUrl) {
        return supplierResourceDao.findByHostUrl(hostUrl);
    }

    @Override
    public SupplierResource create(CreateSupplierResource createSupplierResource) throws MalformedURLException {

        SupplierResource supplierResource = new SupplierResource();

        supplierResource.setName(createSupplierResource.getName());
        supplierResource.setHostUrl(new URL(createSupplierResource.getSomeUrlFromSource()).getHost());

        supplierResource.setPresenceMatchers(createSupplierResource.getPresenceMatchers());

        return supplierResourceDao.save(supplierResource);
    }

    @Override
    public void delete(Integer id) {
        supplierResourceDao.deleteById(id);
    }
}

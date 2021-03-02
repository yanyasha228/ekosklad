package com.ekoskladvalidator.RestControllers;


import com.ekoskladvalidator.Models.HelpServiceManipulationModels.CreateSupplierResource;
import com.ekoskladvalidator.Services.SupplierResourceService;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@RestController
@RequestMapping("/rest/supp_res")
public class SupplierResourcesRestController {

    private final SupplierResourceService supplierResourceService;

    public SupplierResourcesRestController(SupplierResourceService supplierResourceService) {
        this.supplierResourceService = supplierResourceService;
    }

    @PostMapping("/add")
    public void addResource(@RequestParam String link, @RequestParam String name) throws MalformedURLException {

        if (!Objects.isNull(name) && !name.isEmpty() && !Objects.isNull(link) && !link.isEmpty()) {
            if (supplierResourceService.findByHostUrl(new URL(link).getHost()).isEmpty()) {
                CreateSupplierResource createSupplierResource = new CreateSupplierResource();
                createSupplierResource.setSomeUrlFromSource(link);
                createSupplierResource.setName(name);
                supplierResourceService.create(createSupplierResource);
            }
        }
    }

    @DeleteMapping("delete")
    public void deleteResource(@RequestParam Integer id) {
        supplierResourceService.delete(id);
    }
}

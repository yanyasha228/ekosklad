package com.ekoskladvalidator.Controllers;

import com.ekoskladvalidator.Models.SupplierResource;
import com.ekoskladvalidator.Services.SupplierResourceService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings/supp_res")
public class SupplierResourcesController {

    private final SupplierResourceService supplierResourceService;

    public SupplierResourcesController(SupplierResourceService supplierResourceService) {
        this.supplierResourceService = supplierResourceService;
    }

    @GetMapping
    public String resSettings(Model model, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 15) Pageable pageable) {

        model.addAttribute("resPage", supplierResourceService.findAll(pageable));

        return "supplierResourcesSettings";
    }

    @GetMapping("{id}/edit")
    public String resEdit(Model model, @PathVariable Integer id) throws Exception {
        SupplierResource supplierResource = supplierResourceService.findById(id).orElseThrow(() -> new Exception("No resource with such id : " + id));
        model.addAttribute("resource", supplierResource);

        return "supplierResourcesSettingsEdit";
    }

}

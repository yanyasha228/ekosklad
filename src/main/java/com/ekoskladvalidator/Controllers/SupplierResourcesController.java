package com.ekoskladvalidator.Controllers;

import com.ekoskladvalidator.Models.Enums.Presence;
import com.ekoskladvalidator.Models.Enums.QueryType;
import com.ekoskladvalidator.Models.HelpServiceManipulationModels.EditSupplierResource;
import com.ekoskladvalidator.Models.PresenceMatcher;
import com.ekoskladvalidator.Models.SupplierResource;
import com.ekoskladvalidator.Services.SupplierResourceService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    public String resEdit(Model model, @PathVariable Long id) throws Exception {
        SupplierResource supplierResource = supplierResourceService.findById(id).orElseThrow(() -> new Exception("No resource with such id : " + id));
        model.addAttribute("resource", supplierResource);

        return "supplierResourcesSettingsEdit";
    }

    @PostMapping("{id}/edit/submit")
    public String resEditSubmit(@PathVariable Optional<Long> id,
                                @RequestParam Long[] presenceMatcherId,
                                @RequestParam Presence[] status,
                                @RequestParam String[] xPathInput,
                                @RequestParam String[] inputContainsString,
                                @RequestParam QueryType[] queryType) throws Exception {
        if (!((presenceMatcherId.length == status.length)
                && (presenceMatcherId.length == xPathInput.length)
                && (presenceMatcherId.length == inputContainsString.length)))
            throw new Exception("Не совпадают масивы правил проверки");

        Set<PresenceMatcher> presenceSet = new HashSet<>();

        for (int pres = 0; pres < presenceMatcherId.length; pres++) {
            presenceSet.add(new PresenceMatcher(presenceMatcherId[pres], status[pres], xPathInput[pres], inputContainsString[pres], queryType[pres]));
        }
        if (presenceSet.size() != 0) {
            EditSupplierResource editSupplierResource = new EditSupplierResource(id.orElseThrow(() -> new Exception("No Supplier Id")), presenceSet);
            supplierResourceService.edit(editSupplierResource);
        }

        return "redirect:/settings/supp_res";
    }


}

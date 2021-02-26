package com.ekoskladvalidator.Controllers;

import com.ekoskladvalidator.Services.PromApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("settings")
public class SettingsController {

    private final PromApiKeyService promApiKeyService;

    public SettingsController(PromApiKeyService promApiKeyService) {
        this.promApiKeyService = promApiKeyService;
    }

    @RequestMapping
    public String settings(Model model) {
        return "settings";
    }

    @RequestMapping("keys")
    public String keysSettings(Model model, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 15) Pageable pageable) {
        model.addAttribute("keysPage", promApiKeyService.findAll(pageable));
        return "keysSettings";
    }

}

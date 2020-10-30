package com.ekoskladvalidator.Controllers;

import com.ekoskladvalidator.Services.PromApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("settings")
public class SettingsController {

    @Autowired
    private PromApiKeyService promApiKeyService;

    @RequestMapping
    public String settings(Model model) {
        return "settings";
    }

    @RequestMapping
    public String shopsSettings(Model model){

        return "shopSettings";
    }

}

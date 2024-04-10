package com.ekoskladvalidator.RestControllers;

import com.ekoskladvalidator.Models.PromApiKey;
import com.ekoskladvalidator.Services.PromApiKeyService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/rest/keys")
public class KeysRestController {

    private final PromApiKeyService promApiKeyService;

    public KeysRestController(PromApiKeyService promApiKeyService) {
        this.promApiKeyService = promApiKeyService;
    }

    @GetMapping("{id}")
    public PromApiKey getKey(@PathVariable Long id) {
        return promApiKeyService.findById(id).orElse(null);
    }

    @PostMapping("/add")
    public void addKey(@RequestParam String key, @RequestParam String name) {

        if (!Objects.isNull(name) && !name.isEmpty() && !Objects.isNull(key) && !key.isEmpty()) {
            if (promApiKeyService.findByShopName(name).isEmpty() && promApiKeyService.findByApiKey(key).isEmpty()) {

                PromApiKey promApiKey = new PromApiKey();
                promApiKey.setApiKey(key);
                promApiKey.setShopName(name);
                promApiKeyService.save(promApiKey);

            }
        }
    }

    @PostMapping("/edit")
    public void editKey(@RequestParam Long id, @RequestParam String key, @RequestParam String name) {
        if (!Objects.isNull(id) && id != 0) {
            if (!Objects.isNull(name) && !name.isEmpty() && !Objects.isNull(key) && !key.isEmpty()) {
                Optional<PromApiKey> promApiKeyOptional = promApiKeyService.findById(id);
                if (promApiKeyOptional.isPresent()) {
                    PromApiKey promApiKey = promApiKeyOptional.get();
                    promApiKey.setApiKey(key);
                    promApiKey.setShopName(name);
                    promApiKeyService.save(promApiKey);

                }
            }
        }


    }

    @PostMapping("delete")
    public void deleteKey(@RequestParam Long id) {
        promApiKeyService.delete(id);
    }
}

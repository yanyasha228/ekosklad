package com.ekoskladvalidator.RestControllers;

import com.ekoskladvalidator.Validators.ValidatorUtils.ProductValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest/check")
public class CheckDataRestController {

    @Autowired
    private ProductValidatorUtils priceValidatorUtils;

    @PostMapping
    private Map<String , String> check(@RequestParam String url ,
                                      @RequestParam String cssQuery) throws IOException {

        HashMap<String,String> response = new HashMap<>();

        if(!url.isEmpty() && !cssQuery.isEmpty()){
            Optional<Float> pG = priceValidatorUtils.getValidPriceByCssQuery(url,cssQuery);
            pG.ifPresent(aFloat -> response.put("price", String.valueOf(aFloat)));
        }

        return response;

    }
}

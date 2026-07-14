package com.ekoskladvalidator.Models.HelpRestModels;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ErrorsResponse {

    // Prom.ua returns per-product field errors as arrays of messages,
    // e.g. {"<productId>": {"price": ["message", ...]}}
    @JsonAnySetter
    private Map<Long, Map<String, List<String>>> errors;

    @JsonAnyGetter
    public Map<Long, Map<String, List<String>>> getErrors() {
        return errors;
    }

    public void setErrors(Map<Long, Map<String, List<String>>> errors) {
        this.errors = errors;
    }

}

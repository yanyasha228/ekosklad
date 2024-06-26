package com.ekoskladvalidator.Models.HelpRestModels;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class ErrorsResponse {

    @JsonAnySetter
    private Map<Long, Map<String, String>> errors;

    @JsonAnyGetter
    public Map<Long, Map<String, String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<Long, Map<String, String>> errors) {
        this.errors = errors;
    }

}

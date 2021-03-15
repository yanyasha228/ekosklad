package com.ekoskladvalidator.Models.SpecSearchModels;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
public class SearchCriteria {

    public SearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    @NonNull
    private String key;

    @NonNull
    private String operation;

    private Object value;

}

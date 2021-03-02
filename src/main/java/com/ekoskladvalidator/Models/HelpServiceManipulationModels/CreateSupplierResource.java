package com.ekoskladvalidator.Models.HelpServiceManipulationModels;

import com.ekoskladvalidator.Models.PresenceMatcher;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateSupplierResource {

    public CreateSupplierResource(String name , String someUrlFromSource, List<PresenceMatcher> presenceMatchers) {
        this.someUrlFromSource = someUrlFromSource;
        this.presenceMatchers = presenceMatchers;
    }

    @NonNull
    private String someUrlFromSource;

    @NonNull
    private String name;

    @NonNull
    private List<PresenceMatcher> presenceMatchers;
}

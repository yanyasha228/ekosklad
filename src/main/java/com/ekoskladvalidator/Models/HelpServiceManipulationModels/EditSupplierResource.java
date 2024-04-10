package com.ekoskladvalidator.Models.HelpServiceManipulationModels;

import com.ekoskladvalidator.Models.PresenceMatcher;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Set;

@Data
@NoArgsConstructor
public class EditSupplierResource {

    public EditSupplierResource(@NonNull Long id,
                                @NonNull Set<PresenceMatcher> presenceMatchers) {
        this.id = id;
        this.presenceMatchers = presenceMatchers;
    }

    @NonNull
    private Long id;

    @NonNull
    private Set<PresenceMatcher> presenceMatchers;
}

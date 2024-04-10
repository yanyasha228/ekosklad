package com.ekoskladvalidator.Models.HelpRestModels;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EditProductsResponse {

    private List<Long> processed_ids;

    private ErrorsResponse errors;


}

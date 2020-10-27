package com.ekoskladvalidator.Models.HelpRestModels;

import com.ekoskladvalidator.Models.DTO.ProductDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class ProductResponse {

    private ProductDto product;

    private ErrorsResponse errors;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductResponse that = (ProductResponse) o;
        return Objects.equals(product, that.product) &&
                Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, errors);
    }
}

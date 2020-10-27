package com.ekoskladvalidator.Models.HelpRestModels;

import com.ekoskladvalidator.Models.DTO.ProductDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class ProductsListResponse implements Serializable {

    private List<ProductDto> products = new ArrayList<ProductDto>();

    private int group_id;

    private ErrorsResponse errors;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductsListResponse that = (ProductsListResponse) o;
        return group_id == that.group_id &&
                Objects.equals(products, that.products) &&
                Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products, group_id, errors);
    }
}

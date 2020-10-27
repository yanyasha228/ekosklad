package com.ekoskladvalidator.Models.HelpRestModels;

import com.ekoskladvalidator.Models.Group;
import com.ekoskladvalidator.Models.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class SynchronizeDto {

    private List<Product> products = new ArrayList<>();

    private List<Group> groups = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SynchronizeDto that = (SynchronizeDto) o;
        return Objects.equals(products, that.products) &&
                Objects.equals(groups, that.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products, groups);
    }
}

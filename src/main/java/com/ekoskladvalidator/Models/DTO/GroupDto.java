package com.ekoskladvalidator.Models.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
public class GroupDto implements Serializable {


    private int id;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupDto groupDTO = (GroupDto) o;
        return id == groupDTO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
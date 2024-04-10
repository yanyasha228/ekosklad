package com.ekoskladvalidator.Models.DTO;

import com.ekoskladvalidator.Models.Enums.Presence;
import com.ekoskladvalidator.Models.Enums.Status;
import com.ekoskladvalidator.Models.Group;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
public class ProductDto implements Serializable {

    private long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String name;

    private float price;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String external_id;

    private Presence presence;

    //    @JsonIgnore
//    private String apiToken;
//
//    @JsonIgnore
//    private List<ModelIdApiKeyLine> modelIdApiKeyLines =  new ArrayList<>();
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Status status;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String currency;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String main_image;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Group group;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto that = (ProductDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

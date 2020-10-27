package com.ekoskladvalidator.Models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "prom_api_keys")
public class PromApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Column(name = "api_key")
    private String apiKey;

    @NotNull
    @Column(name = "shop_name")
    private String shopName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PromApiKey that = (PromApiKey) o;
        return id == that.id &&
                apiKey.equals(that.apiKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, apiKey);
    }
}

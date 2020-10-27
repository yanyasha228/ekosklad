package com.ekoskladvalidator.Models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "id_apikey_lines")
public class ModelIdApiKeyLine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    @OneToOne
    @JoinColumn(name = "prom_api_key_id", referencedColumnName = "id")
    private PromApiKey promApiKey;

    @NotNull
    @Column(name = "product_api_id")
    private Integer productApiId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelIdApiKeyLine that = (ModelIdApiKeyLine) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(promApiKey, that.promApiKey) &&
                Objects.equals(productApiId, that.productApiId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, promApiKey, productApiId);
    }
}

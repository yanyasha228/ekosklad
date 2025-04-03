package com.ekoskladvalidator.Models;


import com.ekoskladvalidator.Models.Enums.Presence;
import com.ekoskladvalidator.Models.Enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    private long id;

    @Column
    private String name;

    @Column
    private Float price;

    @Column
    private String currency;

    @Column(name = "presence")
    @Enumerated(EnumType.STRING)
    private Presence presence;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "product", orphanRemoval = true)
    List<ModelIdApiKeyLine> modelIdApiKeyLines = new ArrayList<>();

    @Column
    private Status status;

    @Column
    private String main_image;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @Column(name = "last_validation_date")
    @DateTimeFormat(pattern = "dd-M-yyyy hh:mm")
    private LocalDateTime lastValidationDate;

    @Column(name = "url_for_validating")
    private String urlForValidating;

    @Column(name = "css_query_for_validating")
    private String cssQueryForValidating;

    @Column(name = "validation_status")
    @Type(type = "true_false")
    private boolean validationStatus;

    @Column(name = "url_for_validating_exist")
    @Type(type = "true_false")
    private boolean dataForValidatingExist;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PresenceMatcher> alternativePresenceMatchers = new HashSet<>();

    @PreUpdate
    @PrePersist
    protected void prePersistUpdate() {

        modifyDataForValidationFlag();

    }

    private void modifyDataForValidationFlag() {

        if (urlForValidating != null && cssQueryForValidating != null) {
            setDataForValidatingExist(!urlForValidating.replaceAll(" ", "").equalsIgnoreCase("") &&
                    !cssQueryForValidating.replaceAll(" ", "").equalsIgnoreCase(""));
        } else setDataForValidatingExist(false);


    }

    public void updateLastValidationDate() {
        lastValidationDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

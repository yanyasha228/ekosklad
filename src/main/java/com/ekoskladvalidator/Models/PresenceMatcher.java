package com.ekoskladvalidator.Models;

import com.ekoskladvalidator.Models.Enums.Presence;
import com.ekoskladvalidator.Models.Enums.QueryType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "presence_matchers")
public class PresenceMatcher {

    public PresenceMatcher(Long id,
                           Presence presence,
                           String presencePathQuery,
                           String containString,
                           QueryType queryType) {
        this.presence = presence;
        this.presencePathQuery = presencePathQuery;
        this.containString = containString;
        this.queryType = queryType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "presence")
    @Enumerated(EnumType.STRING)
    private Presence presence;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "presence_css_query")
    private String presencePathQuery;

    @Column(name = "contain_string")
    private String containString;

    @Column
    @Enumerated(EnumType.STRING)
    private QueryType queryType;

}

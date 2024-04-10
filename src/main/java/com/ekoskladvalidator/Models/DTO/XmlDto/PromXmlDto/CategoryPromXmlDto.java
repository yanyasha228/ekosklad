package com.ekoskladvalidator.Models.DTO.XmlDto.PromXmlDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@Data
@NoArgsConstructor
@XmlRootElement(name = "category")
@XmlAccessorType(XmlAccessType.NONE)
public class CategoryPromXmlDto {

    @XmlAttribute(name = "id")
    private long id;

    @XmlAttribute(name = "parentId")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long parentId;

    @XmlValue
    private String name;

    @XmlAttribute(name = "portal_id")
    private String portalId;

    @XmlAttribute(name = "portal_url")
    private String portalUrl;


}

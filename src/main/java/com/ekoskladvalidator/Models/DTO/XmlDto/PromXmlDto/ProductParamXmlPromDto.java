package com.ekoskladvalidator.Models.DTO.XmlDto.PromXmlDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@Data
@NoArgsConstructor
@XmlRootElement(name = "param")
@XmlAccessorType(XmlAccessType.NONE)
public class ProductParamXmlPromDto {
    public ProductParamXmlPromDto(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "unit")
    private String unit;

    @XmlValue
    private String value;


}

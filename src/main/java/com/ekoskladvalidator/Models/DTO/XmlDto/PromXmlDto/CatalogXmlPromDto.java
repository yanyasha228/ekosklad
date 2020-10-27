package com.ekoskladvalidator.Models.DTO.XmlDto.PromXmlDto;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = "yml_catalog")
@XmlAccessorType(XmlAccessType.NONE)
public class CatalogXmlPromDto {

    @XmlElement(name = "shop")
    private ShopXmlPromDto shopXmlPromDto;

}

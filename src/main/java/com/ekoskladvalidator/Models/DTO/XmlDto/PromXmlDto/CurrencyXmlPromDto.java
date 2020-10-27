package com.ekoskladvalidator.Models.DTO.XmlDto.PromXmlDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = "currency")
@XmlAccessorType(XmlAccessType.NONE)
public class CurrencyXmlPromDto {

    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "rate")
    private String rate;
}

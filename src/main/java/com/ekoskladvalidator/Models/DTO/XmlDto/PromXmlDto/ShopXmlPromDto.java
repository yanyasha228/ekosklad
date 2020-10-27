package com.ekoskladvalidator.Models.DTO.XmlDto.PromXmlDto;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@NoArgsConstructor
@XmlRootElement(name = "shop")
@XmlAccessorType(XmlAccessType.NONE)
public class ShopXmlPromDto {


    @XmlElementWrapper(name = "currencies")
    @XmlElement(name = "currency")
    private List<CurrencyXmlPromDto> currencies;

    @XmlElementWrapper(name = "categories")
    @XmlElement(name = "category")
    private List<CategoryPromXmlDto> categories;

    @XmlElementWrapper(name = "offers")
    @XmlElement(name = "offer")
    private List<ProductXmlPromDto> offersList;


}

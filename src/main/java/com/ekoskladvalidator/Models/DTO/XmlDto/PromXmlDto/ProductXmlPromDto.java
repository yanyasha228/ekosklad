package com.ekoskladvalidator.Models.DTO.XmlDto.PromXmlDto;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@XmlRootElement(name = "offer")
@XmlAccessorType(XmlAccessType.NONE)
public class ProductXmlPromDto {

    @XmlAttribute(name = "id")
    private long id;

    @XmlAttribute(name = "available")
    private boolean available;


    //    @XmlAttribute(name = "type")
//    private String type;
//
//    @XmlAttribute(name = "selling_type")
//    private String sellingType;
//
//    @XmlAttribute(name = "group_id")
//    private int groupId;
//

    @XmlElement(name = "name")
    private String name;

//    @XmlElement(name = "typePrefix")
//    private String typePrefix;

    @XmlElement(name = "categoryId")
    private int categoryId;

    @XmlElement(name = "portal_category_id")
    private int portalCategoryId;

    @XmlElement(name = "portal_category_url")
    private String portalCategoryUrl;

    @XmlElement(name = "price")
    private float price;

//    @XmlElement(name = "oldprice")
//    private float oldPrice;

    @XmlElement(name = "minimum_order_quantity")
    private int minimumOrderQuantity;

    @XmlElement(name = "quantity_in_stock")
    private int amount = 10;

//    @XmlElementWrapper(name = "prices")
//    @XmlElement(name = "price")
//    private List<ComplexPriceXmlPromDto> prices;

    @XmlElement(name = "url")
    private String url;

    @XmlElement(name = "discount")
    private int discount;

    //!!!!!
    @XmlElement(name = "currencyId")
    private String currency;

    @XmlElement(name = "picture")
    private List<String> pictures;

    //!!!!!
    @XmlElement(name = "vendor")
    private String vendor;

    @XmlElement(name = "vendorCode")
    private String vendorCode;

    @XmlElement(name = "country")
    private String country;

    @XmlElement(name = "param")
    private List<ProductParamXmlPromDto> params = new ArrayList<>();

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "country_of_origin")
    private String countryOfOrigin;

    @XmlElement(name = "pickup")
    private boolean pickup;

    @XmlElement(name = "delivery")
    private boolean delivery;

    @XmlElement(name = "keywords")
    private String keywords;



}

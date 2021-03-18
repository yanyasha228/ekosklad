package com.ekoskladvalidator.RestDao;

import com.ekoskladvalidator.Models.DTO.ProductDto;
import com.ekoskladvalidator.Models.PromApiKey;

import java.util.List;
import java.util.Optional;

public interface ProductRestDao {

    Optional<ProductDto> getProductById(int id);

    List<ProductDto> getProductsByGroupId(int id);

    List<ProductDto> postProducts(List<ProductDto> productDtos);

    Optional<ProductDto> getProductByExternalIdAndApiToken(int external_id, PromApiKey promApiKey);

    List<ProductDto> postProductsByApiToken(PromApiKey promApiKey, List<ProductDto> productDtos);

    Optional<ProductDto> getProductByIdAndApiToken(int id, PromApiKey promApiKey);


}

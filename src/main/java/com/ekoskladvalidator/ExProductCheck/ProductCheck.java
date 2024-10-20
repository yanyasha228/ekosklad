package com.ekoskladvalidator.ExProductCheck;

import com.ekoskladvalidator.Models.Product;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public final class ProductCheck {

    private final Product product;

    private final HttpStatus httpStatus;

    private final String msg;

//    enum CheckStatus {
//        NOT_FOUND, ERROR
//    }
}

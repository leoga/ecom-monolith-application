package com.leoga.ecom.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CartItemResponse {
    private Long id;
    private UserResponse user;
    private ProductResponse product;
    private Integer quantity;
    private BigDecimal price;
}

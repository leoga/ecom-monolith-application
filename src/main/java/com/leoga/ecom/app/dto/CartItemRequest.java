package com.leoga.ecom.app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CartItemRequest {
    private Long userId;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}

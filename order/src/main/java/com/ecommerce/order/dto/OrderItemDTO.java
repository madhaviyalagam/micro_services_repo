package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long Id;
    private String productId;
    private Integer quantiy;
    private BigDecimal price;
    private BigDecimal subbTotal;
}



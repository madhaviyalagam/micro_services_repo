package com.ecommerce.order.model;

import lombok.Data;

@Data
public class CartItemRequest {
   private String productId;
   private Integer quantity;
}

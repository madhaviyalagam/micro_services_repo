package com.ecommerce.order.controller;


import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.CartItemRequest;
import com.ecommerce.order.service.CartService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private CartService cartService;
    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest request){
       if (!cartService.addToCart(userId,request)){
            return ResponseEntity.badRequest().body("Product is out of Stock or User not found or product not found");
       }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/iterms/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-User-ID") String userId,
        @PathVariable Long productId){
            boolean result = cartService.deleteItemFromCart(userId,productId);
            return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok(cartService.getCart(userId));
    }

}

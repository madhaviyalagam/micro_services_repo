package com.ecommerce.order.service;

import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.CartItemRequest;
import com.ecommerce.order.repositary.CartRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor

public class CartService {

    public final CartRepository cartRepository;
    public boolean addToCart(String userId, CartItemRequest request) {

           CartItem item = new CartItem();

           item.setUserId(userId);
           item.setProductId(String.valueOf(request.getProductId()));
           item.setQuantity(request.getQuantity());
           item.setPrice(BigDecimal.valueOf(1000.00));
           cartRepository.save(item);
            return  true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {
        CartItem cartItem = cartRepository.findByUserIdAndProductId(userId, String.valueOf(productId));
        if(cartItem!= null){
            cartRepository.deleteByUserIdAndProductId(userId, String.valueOf(productId));
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
        return cartRepository.findByUserId(userId);
    }
}

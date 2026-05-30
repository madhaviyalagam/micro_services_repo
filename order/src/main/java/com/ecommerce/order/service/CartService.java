package com.ecommerce.order.service;

import com.ecommerce.order.client.ProductClient;
import com.ecommerce.order.client.UserClient;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.CartItemRequest;
import com.ecommerce.order.repositary.CartRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final UserClient userClient;
	private final ProductClient productClient;

	public boolean addToCart(String userId, CartItemRequest request) {
		try {
			userClient.getUserById(Long.valueOf(userId));
		} catch (FeignException.NotFound | NumberFormatException e) {
			return false;
		}

		ProductResponse product;
		try {
			product = productClient.getProductById(Long.valueOf(request.getProductId()));
		} catch (FeignException.NotFound | NumberFormatException e) {
			return false;
		}

		if (product.getActive() == null || !product.getActive()
				|| product.getStockQuantity() == null
				|| product.getStockQuantity() < request.getQuantity()) {
			return false;
		}

		CartItem existingItem = cartRepository.findByUserIdAndProductId(userId, request.getProductId());
		if (existingItem != null) {
			existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
			existingItem.setPrice(product.getPrice());
			cartRepository.save(existingItem);
			return true;
		}

		CartItem item = new CartItem();
		item.setUserId(userId);
		item.setProductId(request.getProductId());
		item.setQuantity(request.getQuantity());
		item.setPrice(product.getPrice());
		cartRepository.save(item);
		return true;
	}

	public boolean deleteItemFromCart(String userId, Long productId) {
		CartItem cartItem = cartRepository.findByUserIdAndProductId(userId, String.valueOf(productId));
		if (cartItem != null) {
			cartRepository.deleteByUserIdAndProductId(userId, String.valueOf(productId));
			return true;
		}
		return false;
	}

	public List<CartItem> getCart(String userId) {
		return cartRepository.findByUserId(userId);
	}
}

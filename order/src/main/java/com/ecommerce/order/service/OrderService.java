package com.ecommerce.order.service;

import com.ecommerce.order.client.UserClient;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.model.*;
import com.ecommerce.order.repositary.CartRepository;
import com.ecommerce.order.repositary.OrderRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {
	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;
	private final UserClient userClient;

	public Optional<OrderResponse> createOrder(String userId) {
		try {
			userClient.getUserById(Long.valueOf(userId));
		} catch (FeignException.NotFound | NumberFormatException e) {
			return Optional.empty();
		}

		List<CartItem> items = cartRepository.getCartItemsByUserId(userId);
		if (items.isEmpty()) {
			return Optional.empty();
		}

		BigDecimal totalPrice = items.stream()
				.map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		Order order = new Order();
		order.setUserId(userId);
		List<OrderItem> orderItems = items.stream().map(item -> new OrderItem(
				null,
				item.getProductId(),
				item.getQuantity(),
				item.getPrice(),
				order
		)).collect(Collectors.toList());
		order.setItems(orderItems);
		order.setTotalAmount(totalPrice);
		order.setStatus(OrderStatus.CONFIRMED);
		Order savedOrder = orderRepository.save(order);
		cartRepository.deleteByUserId(Long.valueOf(userId));

		return Optional.of(mapToOrderResponse(savedOrder));
	}

	private OrderResponse mapToOrderResponse(Order order) {
		List<OrderItemDTO> items = order.getItems()
				.stream()
				.map(orderItem -> new OrderItemDTO(
						orderItem.getId(),
						orderItem.getProductId(),
						orderItem.getQuantity(),
						orderItem.getPrice(),
						orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
				))
				.toList();

		return new OrderResponse(
				order.getId(),
				order.getTotalAmount(),
				order.getStatus(),
				items,
				order.getCreateAt()
		);
	}
}

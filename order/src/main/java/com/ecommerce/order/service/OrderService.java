package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.model.*;
import com.ecommerce.order.repositary.CartRepository;
import com.ecommerce.order.repositary.OrderRepository;
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
    public Optional<OrderResponse> createOrder(String userId) {
        //Validate items
        List<CartItem> items = cartRepository.getCartItemsByUserId(userId);
        if (items.isEmpty()){
            return Optional.empty();
        }
//        Optional<User> userbyId = userRepository.findById(Long.valueOf(userId));
//        if (userbyId.isEmpty()){
//            return Optional.empty();
//        }
//        User user = userbyId.get();
//        //Validate users
//        //caluculate price
        BigDecimal totalPrice = items.stream().map(CartItem::getPrice).reduce(
                BigDecimal.ZERO, BigDecimal::add);
        //create order
        Order order = new Order();
        order.setUserId(userId);
        List<OrderItem> ordderitmes = items.stream().map(item -> new OrderItem(
                null,
                item.getProductId(),
                item.getQuantity(),
                item.getPrice(),
                order
        )).collect(Collectors.toList());
        order.setItems(ordderitmes);
        order.setTotalAmount(totalPrice);
        order.setStatus(OrderStatus.CONFIRMED);
        Order savedOrder = orderRepository.save(order);
        //clear cart
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

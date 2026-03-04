package com.leoga.ecom.app.services;

import com.leoga.ecom.app.dto.CartItemResponse;
import com.leoga.ecom.app.dto.OrderItemDTO;
import com.leoga.ecom.app.dto.OrderResponse;
import com.leoga.ecom.app.model.*;
import com.leoga.ecom.app.model.*;
import com.leoga.ecom.app.repositories.CartItemRepository;
import com.leoga.ecom.app.repositories.OrderRepository;
import com.leoga.ecom.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;

    public OrderResponse createOrder(Long userId) {
        // Validate for cart items
        List<CartItemResponse> cartItems = cartItemService.getCartItems(userId);
        if (cartItems.isEmpty()) return null;

        // Calculated total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItemResponse::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        User user = userRepository.findById(userId).orElse(null);
        if (null == user) return null;
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalPrice);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setItems(cartItemRepository.findByUser(user).stream()
                .map(cartItem -> mapToOrderItem(cartItem, order)).toList());

        orderRepository.save(order);
        // Clear cart
        cartItemService.clearCart(userId);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setStatus(order.getStatus());
        List<OrderItemDTO> orderItems = order.getItems().stream()
                .map(this::mapToOrderItemDTO).toList();
        orderResponse.setItems(orderItems);
        orderResponse.setCreatedAt(order.getCreatedAt());

        return orderResponse;
    }

    private OrderItem mapToOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getPrice());
        orderItem.setOrder(order);
        return orderItem;
    }

    private OrderItemDTO mapToOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setProductId(orderItem.getProduct().getId());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setPrice(orderItem.getPrice());
        return orderItemDTO;
    }
}

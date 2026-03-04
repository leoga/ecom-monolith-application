package com.leoga.ecom.app.services;

import com.leoga.ecom.app.dto.CartItemRequest;
import com.leoga.ecom.app.dto.CartItemResponse;
import com.leoga.ecom.app.model.CartItem;
import com.leoga.ecom.app.model.Product;
import com.leoga.ecom.app.model.User;
import com.leoga.ecom.app.repositories.CartItemRepository;
import com.leoga.ecom.app.repositories.ProductRepository;
import com.leoga.ecom.app.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class CartItemService {

    private final UserService userService;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public boolean addToCart(CartItemRequest cartItemRequest) {

        // Look for a product
        Optional<Product> productOpt = productRepository.findByIdAndActive(cartItemRequest.getProductId(), true);
        if (productOpt.isEmpty()) return false;

        if (productOpt.get().getStockQuantity() < cartItemRequest.getQuantity()) return false;

        Optional<User> userOpt = userRepository.findById(cartItemRequest.getUserId());
        if (userOpt.isEmpty()) return false;

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(userOpt.get(), productOpt.get());
        if (null != existingCartItem) {
            // Update the quantity and price
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setPrice(existingCartItem.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        } else {
            // Create a new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUser(userOpt.get());
            cartItem.setProduct(productOpt.get());
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(productOpt.get().getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())));
            cartItemRepository.save(cartItem);
        }

        return true;
    }

    public boolean deleteItemFromCart(Long userId, Long productId) {

        Optional<Product> productOpt = productRepository.findById(productId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (productOpt.isPresent() && userOpt.isPresent()) {
            CartItem existingCartItem = cartItemRepository.findByUserAndProduct(userOpt.get(), productOpt.get());
            if (null != existingCartItem) {
                cartItemRepository.delete(existingCartItem);
                return true;
            }
        }

        return false;
    }

    public void clearCart(Long userId) {
        cartItemRepository.deleteByUser(userRepository.findById(userId).orElseThrow());
    }

    public List<CartItemResponse> getCartItems(Long userId) {
        return userRepository.findById(userId)
                .map(user -> cartItemRepository.findByUser(user).stream()
                .map(this::mapToCartItemResponse).toList()).orElseGet(List::of);
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setId(cartItem.getId());
        cartItemResponse.setUser(userService.findById(cartItem.getUser().getId()));
        cartItemResponse.setProduct(productService.getProductById(cartItem.getProduct().getId()));
        cartItemResponse.setQuantity(cartItem.getQuantity());
        cartItemResponse.setPrice(cartItem.getPrice());
        return cartItemResponse;
    }
}

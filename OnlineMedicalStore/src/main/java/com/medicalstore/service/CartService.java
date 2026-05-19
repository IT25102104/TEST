package com.medicalstore.service;

import com.medicalstore.entity.*;
import com.medicalstore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MedicineService medicineService;

    // Get or create a cart for a user
    public Cart getOrCreateCart(User user) {
        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        if (optionalCart.isPresent()) {
            // Already has a cart → return it
            return optionalCart.get();
        } else {
            // No cart yet → create a new one
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        }
    }

    // Add medicine to cart
    public Cart addToCart(User user, Long medicineId, int quantity) {
        Cart cart = getOrCreateCart(user);
        Medicine medicine = medicineService.getMedicineById(medicineId);

        // Look for this medicine already in the cart
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getMedicine().getId().equals(medicineId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Medicine already in cart → increase quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // Medicine not in cart → add new item
            CartItem newItem = new CartItem();
            newItem.setMedicine(medicine);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getCartItems().add(newItem);
        }

        // Save cart (which cascade saves cart items)
        return cartRepository.save(cart);
    }

    // Update quantity of an item
    public Cart updateCartItem(User user, Long cartItemId, int newQuantity) {
        Cart cart = getOrCreateCart(user);
        cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(newQuantity);
                    cartItemRepository.save(item);
                });
        return cart;
    }

    // Remove an item from cart
    public Cart removeCartItem(User user, Long cartItemId) {
        Cart cart = getOrCreateCart(user);
        cart.getCartItems().removeIf(item -> item.getId().equals(cartItemId));
        return cartRepository.save(cart);
    }

    // Clear entire cart
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    // Calculate total price
    public double getCartTotal(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(item -> item.getMedicine().getPrice() * item.getQuantity())
                .sum();
    }
}
package com.medicalstore.service;

import com.medicalstore.entity.*;
import com.medicalstore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private MedicineService medicineService;

    // Create an order from the user's cart
    public Order placeOrder(User user) {
        Cart cart = cartService.getOrCreateCart(user);

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot place order.");
        }

        // Create the order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        double total = 0.0;
        for (CartItem cartItem : cart.getCartItems()) {
            Medicine medicine = cartItem.getMedicine();

            // Check stock again at order time
            if (medicine.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock for " + medicine.getName());
            }

            // Build an OrderItem from the CartItem
            OrderItem orderItem = new OrderItem();
            orderItem.setMedicine(medicine);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(medicine.getPrice());   // snapshot price
            orderItem.setOrder(order);

            order.getOrderItems().add(orderItem);
            total += medicine.getPrice() * cartItem.getQuantity();

            // Reduce stock
            medicineService.reduceStock(medicine.getId(), cartItem.getQuantity());
        }

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);

        // Clear the cart after successful order
        cartService.clearCart(user);

        return savedOrder;
    }

    // Get all orders for a user
    public List<Order> getOrdersForUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    // Get a single order by its ID
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // Admin: update order status
    public void updateOrderStatus(Long orderId, String status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        orderRepository.save(order);
    }

    public long countOrders() {
        return orderRepository.count();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public double getTotalRevenue() {
        // Sum of all orders' totalAmount
        return orderRepository.findAll()
                .stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }
}
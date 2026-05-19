package com.medicalstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which medicine is added
    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    // How many units
    @Column(nullable = false)
    private int quantity;

    // Which cart does this item belong to
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // ===== Getters and Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Medicine getMedicine() { return medicine; }
    public void setMedicine(Medicine medicine) { this.medicine = medicine; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
}
package com.ninjashop.ninjashop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @JsonIgnore
    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    private String size;

    private int quentity;

    private BigDecimal descountedPrice;

    private Long userId;

    private BigDecimal price;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    private LocalDateTime createdAt;

    public OrderItem(Long id, Order order, Product product, String size, int quentity, BigDecimal descountedPrice, Long userId, LocalDateTime createdAt) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.size = size;
        this.quentity = quentity;
        this.descountedPrice = descountedPrice;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public OrderItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuentity() {
        return quentity;
    }

    public void setQuentity(int quentity) {
        this.quentity = quentity;
    }

    public BigDecimal getDescountedPrice() {
        return descountedPrice;
    }

    public void setDescountedPrice(BigDecimal descountedPrice) {
        this.descountedPrice = descountedPrice;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setQuantity(int quantity) {
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
    }
}

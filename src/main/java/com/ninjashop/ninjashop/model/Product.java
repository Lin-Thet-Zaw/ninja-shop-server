package com.ninjashop.ninjashop.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 255)
    private String title;

    @Lob
    private String description;


    private BigDecimal price;

    @Column(name = "discounted_price")
    private BigDecimal discountedPrice;

    @Column(name = "discounted_percent")
    private int discountedPercent;

    private int quantity;

    @Column(length = 255)
    private String brand;

    @Column(length = 255)
    private String color;

    @Embedded
    @ElementCollection
    @Column(name = "size")
    private Set<Size> sizes = new HashSet
            <>();

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getNumRatins() {
        return numRatins;
    }

    public void setNumRatins(int numRatins) {
        this.numRatins = numRatins;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<Size> getSizes() {
        return sizes;
    }

    public void setSizes(Set<Size> sizes) {
        this.sizes = sizes;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDiscountedPercent() {
        return discountedPercent;
    }

    public void setDiscountedPercent(int discountedPercent) {
        this.discountedPercent = discountedPercent;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "num_ratings")
    private  int numRatins;

    public Product(Long id, String title, String description, BigDecimal price, BigDecimal discountedPrice, int discountedPercent, int quantity, String brand, String color, Set<Size> sizes, String imageUrl, List<Rating> ratings, List<Review> reviews, int numRatins, Category category, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.discountedPrice = discountedPrice;
        this.discountedPercent = discountedPercent;
        this.quantity = quantity;
        this.brand = brand;
        this.color = color;
        this.sizes = sizes;
        this.imageUrl = imageUrl;
        this.ratings = ratings;
        this.reviews = reviews;
        this.numRatins = numRatins;
        this.category = category;
        this.createdAt = createdAt;
    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {
    }

    private LocalDateTime createdAt;
}

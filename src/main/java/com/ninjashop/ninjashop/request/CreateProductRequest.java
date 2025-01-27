package com.ninjashop.ninjashop.request;
import com.ninjashop.ninjashop.model.Size;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Set;

public class CreateProductRequest {

    private String title;

    @NotBlank(message = "Description is required")
    @jakarta.validation.constraints.Size(min = 5, max = 200, message = "Description must be between 5 and 200 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Discounted price is required")
    @DecimalMin(value = "0.01", message = "Discounted price must be greater than 0")
    private BigDecimal discountedPrice;

    @NotNull(message = "Discount percent is required")
    @Min(value = 0, message = "Discount percent must be at least 0")
    @Max(value = 100, message = "Discount percent cannot exceed 100")
    private Integer discountedPercent;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotBlank(message = "Brand is required")
    @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Brand must be between 1 and 50 characters")
    private String brand;

    @NotBlank(message = "Color is required")
    @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Color must be between 1 and 50 characters")
    private String color;

    @NotNull(message = "Sizes are required")
    @jakarta.validation.constraints.Size(min = 1, message = "At least one size must be provided")
    private Set<Size> sizes;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    @NotBlank(message = "Top-level category is required")
    @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Top-level category must be between 1 and 50 characters")
    private String topLevelCategory;

    @NotBlank(message = "Second-level category is required")
    @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Second-level category must be between 1 and 50 characters")
    private String secondLevelCategory;

    @NotBlank(message = "Third-level category is required")
    @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Third-level category must be between 1 and 50 characters")
    private String thirdLevelCategory;

    public @NotBlank(message = "Title is required") @jakarta.validation.constraints.Size(min = 5, max = 50, message = "Title must be between 5 and 50 characters") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is required") @jakarta.validation.constraints.Size(min = 5, max = 50, message = "Title must be between 5 and 50 characters") String title) {
        this.title = title;
    }

    public @NotBlank(message = "Description is required") @jakarta.validation.constraints.Size(min = 5, max = 200, message = "Description must be between 5 and 200 characters") String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank(message = "Description is required") @jakarta.validation.constraints.Size(min = 5, max = 200, message = "Description must be between 5 and 200 characters") String description) {
        this.description = description;
    }

    public @NotNull(message = "Price is required") @DecimalMin(value = "0.01", message = "Price must be greater than 0") BigDecimal getPrice() {
        return price;
    }

    public void setPrice(@NotNull(message = "Price is required") @DecimalMin(value = "0.01", message = "Price must be greater than 0") BigDecimal price) {
        this.price = price;
    }

    public @NotNull(message = "Discounted price is required") @DecimalMin(value = "0.01", message = "Discounted price must be greater than 0") BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(@NotNull(message = "Discounted price is required") @DecimalMin(value = "0.01", message = "Discounted price must be greater than 0") BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public @NotNull(message = "Discount percent is required") @Min(value = 0, message = "Discount percent must be at least 0") @Max(value = 100, message = "Discount percent cannot exceed 100") Integer getDiscountedPercent() {
        return discountedPercent;
    }

    public void setDiscountedPercent(@NotNull(message = "Discount percent is required") @Min(value = 0, message = "Discount percent must be at least 0") @Max(value = 100, message = "Discount percent cannot exceed 100") Integer discountedPercent) {
        this.discountedPercent = discountedPercent;
    }

    public @NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be at least 1") Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be at least 1") Integer quantity) {
        this.quantity = quantity;
    }

    public @NotBlank(message = "Brand is required") @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Brand must be between 1 and 50 characters") String getBrand() {
        return brand;
    }

    public void setBrand(@NotBlank(message = "Brand is required") @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Brand must be between 1 and 50 characters") String brand) {
        this.brand = brand;
    }

    public @NotBlank(message = "Color is required") @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Color must be between 1 and 50 characters") String getColor() {
        return color;
    }

    public void setColor(@NotBlank(message = "Color is required") @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Color must be between 1 and 50 characters") String color) {
        this.color = color;
    }

    public @NotBlank(message = "Image URL is required") String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@NotBlank(message = "Image URL is required") String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public @NotNull(message = "Sizes are required") @jakarta.validation.constraints.Size(min = 1, message = "At least one size must be provided") Set<Size> getSizes() {
        return sizes;
    }

    public void setSizes(@NotNull(message = "Sizes are required") @jakarta.validation.constraints.Size(min = 1, message = "At least one size must be provided") Set<Size> sizes) {
        this.sizes = sizes;
    }

    public @NotBlank(message = "Top-level category is required") @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Top-level category must be between 1 and 50 characters") String getTopLevelCategory() {
        return topLevelCategory;
    }

    public void setTopLevelCategory(@NotBlank(message = "Top-level category is required") @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Top-level category must be between 1 and 50 characters") String topLevelCategory) {
        this.topLevelCategory = topLevelCategory;
    }

    public @NotBlank(message = "Second-level category is required") @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Second-level category must be between 1 and 50 characters") String getSecondLevelCategory() {
        return secondLevelCategory;
    }

    public void setSecondLevelCategory(@NotBlank(message = "Second-level category is required") @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Second-level category must be between 1 and 50 characters") String secondLevelCategory) {
        this.secondLevelCategory = secondLevelCategory;
    }

    public @NotBlank(message = "Third-level category is required") @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Third-level category must be between 1 and 50 characters") String getThirdLevelCategory() {
        return thirdLevelCategory;
    }

    public void setThirdLevelCategory(@NotBlank(message = "Third-level category is required") @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Third-level category must be between 1 and 50 characters") String thirdLevelCategory) {
        this.thirdLevelCategory = thirdLevelCategory;
    }
}

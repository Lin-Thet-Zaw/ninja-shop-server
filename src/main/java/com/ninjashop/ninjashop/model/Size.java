package com.ninjashop.ninjashop.model;

import java.util.Objects;

public class Size {
    private String name;
    private int quantity;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Size size = (Size) o;
        return Objects.equals(name, size.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
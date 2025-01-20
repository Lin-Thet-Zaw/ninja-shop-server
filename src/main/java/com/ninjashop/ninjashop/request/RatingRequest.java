package com.ninjashop.ninjashop.request;

public class RatingRequest {
    private Long prodcutId;
    private double rating;

    public Long getProdcutId() {
        return prodcutId;
    }

    public void setProdcutId(Long prodcutId) {
        this.prodcutId = prodcutId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}

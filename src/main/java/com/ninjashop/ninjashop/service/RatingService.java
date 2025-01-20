package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.ProductException;
import com.ninjashop.ninjashop.model.Rating;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.request.RatingRequest;

import java.util.List;

public interface RatingService {
    public Rating createRating(RatingRequest req, User user) throws ProductException;
    public List<Rating> getProductRatings(Long productId);
}

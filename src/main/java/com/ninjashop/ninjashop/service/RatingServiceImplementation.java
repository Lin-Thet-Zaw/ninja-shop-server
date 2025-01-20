package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.ProductException;
import com.ninjashop.ninjashop.model.Product;
import com.ninjashop.ninjashop.model.Rating;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.repository.RatingRepository;
import com.ninjashop.ninjashop.request.RatingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServiceImplementation implements RatingService{
    @Autowired
    public RatingRepository ratingRepository;
    @Autowired
    public ProductService productService;

    public RatingServiceImplementation(RatingRepository ratingRepository, ProductService productService) {
        this.ratingRepository = ratingRepository;
        this.productService = productService;
    }

    @Override
    public Rating createRating(RatingRequest req, User user) throws ProductException {
        Product product = productService.findProductById(req.getProdcutId());

        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(req.getRating());
        rating.setCreatedAt(LocalDateTime.now());
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getProductRatings(Long productId) {
        return ratingRepository.getAllProductRating(productId);
    }
}

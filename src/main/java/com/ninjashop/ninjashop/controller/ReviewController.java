package com.ninjashop.ninjashop.controller;

import com.ninjashop.ninjashop.exception.ProductException;
import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.Review;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.request.ReviewRequest;
import com.ninjashop.ninjashop.service.ReviewService;
import com.ninjashop.ninjashop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private UserService userService;
    @Autowired
    ReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest req,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException{
        User user = userService.findUserProfileByJwt(jwt);
        Review review = reviewService.createReview(req, user);

        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductReviews(@PathVariable Long productId) throws UserException, ProductException{
        List<Review> reviews = reviewService.getAllReview(productId);

        return new ResponseEntity<>(reviews,HttpStatus.ACCEPTED);
    }

}

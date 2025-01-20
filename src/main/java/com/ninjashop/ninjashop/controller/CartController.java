package com.ninjashop.ninjashop.controller;

import com.ninjashop.ninjashop.exception.ProductException;
import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.Cart;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.request.AddItemRequest;
import com.ninjashop.ninjashop.response.ApiResponse;
import com.ninjashop.ninjashop.service.CartService;
import com.ninjashop.ninjashop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    @ManagedOperation(description = "find cart by user id")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserException{
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());

        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    @ManagedOperation(description = "add item to cart")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest req,
                                                     @RequestHeader("Authorization")String jwt) throws UserException, ProductException{
        User user = userService.findUserProfileByJwt(jwt);
        cartService.addItemCart(user.getId(), req);

        ApiResponse res = new ApiResponse();
        res.setMessage("item add to cart");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

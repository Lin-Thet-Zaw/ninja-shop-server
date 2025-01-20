package com.ninjashop.ninjashop.controller;

import com.ninjashop.ninjashop.exception.CartItemException;
import com.ninjashop.ninjashop.exception.ProductException;
import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.Cart;
import com.ninjashop.ninjashop.model.CartItem;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.response.ApiResponse;
import com.ninjashop.ninjashop.service.CartItemService;
import com.ninjashop.ninjashop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @DeleteMapping("/{cartItemId}")
    @ManagedOperation(description = "Remove cart item from Cart")
    public ResponseEntity<ApiResponse> removeCartItem(@PathVariable("cartItemId") Long cartItemId,
                                                      @RequestHeader("Authorization") String jwt)
            throws UserException, ProductException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse res = new ApiResponse();
        res.setMessage("Remove item from cart");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("{cartItemId}/update")
    @ManagedOperation(description = "Update cart item from Cart")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItem,
                                                      @PathVariable("cartItemId") Long cartItemId,
                                                      @RequestHeader("Authorization") String jwt)
            throws UserException, ProductException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        CartItem updatedCart = cartItemService.updateCartItem(user.getId(),cartItemId,cartItem);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }
}

package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.CartItemException;
import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.Cart;
import com.ninjashop.ninjashop.model.CartItem;
import com.ninjashop.ninjashop.model.Product;
import org.springframework.stereotype.Service;

public interface CartItemService {
    public CartItem createCartItem(CartItem cartItem);
    public CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem) throws CartItemException, UserException;
    public CartItem isCartItemExists(Cart cart, Product product, String size, Long userId);
    public  void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;
    public CartItem findCartItemById(Long CartItemId) throws CartItemException;
}

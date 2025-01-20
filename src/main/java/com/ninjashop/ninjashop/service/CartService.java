package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.ProductException;
import com.ninjashop.ninjashop.model.Cart;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.request.AddItemRequest;

public interface CartService {
    public Cart createCart(User user);
    public String addItemCart(Long userId, AddItemRequest req) throws ProductException;
    public  Cart findUserCart(Long userId);
}

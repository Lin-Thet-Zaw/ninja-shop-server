package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.CartItemException;
import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.Cart;
import com.ninjashop.ninjashop.model.CartItem;
import com.ninjashop.ninjashop.model.Product;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.repository.CartItemRepository;
import com.ninjashop.ninjashop.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartItemServiceImplementation implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CartRepository cartRepository;

    public CartItemServiceImplementation() {
    }

    public CartItemServiceImplementation(CartItemRepository cartItemRepository, UserService userService, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.cartRepository = cartRepository;
    }

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem) throws CartItemException, UserException {
        CartItem item = findCartItemById(cartItemId);
        User user = userService.findByUserId(item.getUserId());

        if (user.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            item.setDiscountedPrice(item.getProduct().getDiscountedPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return cartItemRepository.save(item);
    }

    @Override
    public CartItem isCartItemExists(Cart cart, Product product, String size, Long userId) {
        return cartItemRepository.isCartItemExists(cart, product, size, userId);
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        CartItem cartItem = findCartItemById(cartItemId);

        User user = userService.findByUserId(cartItem.getUserId());
        User reqUser = userService.findByUserId(userId);

        if (user.getId().equals(reqUser.getId())) {
            cartItemRepository.deleteById(cartItemId);
        } else {
            throw new UserException("You can't remove another user's item");
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemException("CartItem Not Found"));
    }
}


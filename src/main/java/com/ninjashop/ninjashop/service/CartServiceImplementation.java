package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.ProductException;
import com.ninjashop.ninjashop.model.Cart;
import com.ninjashop.ninjashop.model.CartItem;
import com.ninjashop.ninjashop.model.Product;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.repository.CartRepository;
import com.ninjashop.ninjashop.request.AddItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartServiceImplementation implements CartService{
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    public CartServiceImplementation() {
    }

    public CartServiceImplementation(CartRepository cartRepository, CartItemService cartItemService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return  cartRepository.save(cart);
    }

    @Override
    public String addItemCart(Long userId, AddItemRequest req) throws ProductException {
        Cart cart = cartRepository.findByUserId(userId);
        Product product = productService.findProductById(req.getProductId());
        CartItem isPresent = cartItemService.isCartItemExists(cart, product, req.getSize(), userId);

        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);

            // Fix: Use BigDecimal's multiply method
            BigDecimal price = product.getDiscountedPrice().multiply(BigDecimal.valueOf(req.getQuantity()));
            cartItem.setPrice(price);
            cartItem.setSize(req.getSize());

            CartItem createCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createCartItem);
        }
        return "Add Item to Cart";
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        BigDecimal totalPrice = BigDecimal.ZERO; // Use BigDecimal for totals
        BigDecimal totalDiscountedPrice = BigDecimal.ZERO;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice = totalPrice.add(cartItem.getPrice());
            totalDiscountedPrice = totalDiscountedPrice.add(cartItem.getDiscountedPrice());
            totalItem = totalItem + cartItem.getQuantity();
        }

        cart.setTotalDiscountedPrice(totalDiscountedPrice.intValue()); // Convert to int if needed
        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice.intValue()); // Convert to int if needed
        cart.setDiscounted(totalPrice.subtract(totalDiscountedPrice).intValue()); // Calculate discount and convert to int
        return cartRepository.save(cart);
    }
}

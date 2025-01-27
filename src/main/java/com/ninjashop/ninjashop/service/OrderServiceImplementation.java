package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.OrderException;
import com.ninjashop.ninjashop.model.*;
import com.ninjashop.ninjashop.repository.*;
import com.ninjashop.ninjashop.request.AddressRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderServiceImplementation implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderServiceImplementation(OrderRepository orderRepository, CartService cartService, AddressRepository addressRepository, UserRepository userRepository, OrderItemService orderItemService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
    }

    private String generateOrderTrackingCode() {
        Random random = new Random();
        String code;
        do {
            code = String.valueOf(random.nextInt(90000000) + 10000000); // Generates a random 8-digit number
        } while (orderRepository.findByTrackId(code).isPresent()); // Check if the code already exists
        return code;
    }

    @Override
    public Order orderTrack(String trackId) throws OrderException {
        try {
            Optional<Order> opt = orderRepository.findByTrackId(trackId);
            if (opt.isPresent()) {
                return opt.get();
            }
            throw new OrderException("Order Not Found");
        } catch (Exception e) {
            throw new OrderException("An error occurred while finding the order: " + e.getMessage());
        }
    }

    private Address convertToAddress(AddressRequest addressRequest, User user) {
        Address address = new Address();
        address.setFirstName(addressRequest.getFirstName());
        address.setLastName(addressRequest.getLastName());
        address.setStreetAddress(addressRequest.getStreetAddress());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setZipCode(addressRequest.getZipCode());
        address.setUser(user); // Set the user for the address
        return address;
    }

    @Override
    public Order createOrder(User user, @Valid AddressRequest shippingAddress) throws OrderException {
        try {
            // Convert AddressRequest to Address entity
            Address address = convertToAddress(shippingAddress, user);

            // Save the address
            address = addressRepository.save(address);

            // Add the address to the user's address list
            user.getAddresses().add(address);
            userRepository.save(user);

            // Fetch the user's cart
            Cart cart = cartService.findUserCart(user.getId());

            // Create order items from cart items
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem item : cart.getCartItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setPrice(item.getPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());
                orderItem.setDiscountedPrice(item.getDiscountedPrice());

                OrderItem createdOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(createdOrderItem);
            }

            // Create the order
            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setOrderItemList(orderItems);
            createdOrder.setTotalPrice(cart.getTotalPrice());
            createdOrder.setDiscounted(cart.getDiscounted());
            createdOrder.setTotalDiscountPrice(cart.getTotalDiscountedPrice());
            createdOrder.setTotalItem(cart.getTotalItem());
            createdOrder.setShippingAddress(address);
            createdOrder.setOrderDate(LocalDateTime.now());
            createdOrder.setOrderStatus("PENDING");
            createdOrder.getPaymentDetails().setStatus("PENDING");
            createdOrder.setCreatedAt(LocalDateTime.now());

            // Generate and set the order tracking code
            createdOrder.setTrackId(generateOrderTrackingCode());

            // Save the order
            Order savedOrder = orderRepository.save(createdOrder);

            // Update order items with the saved order
            for (OrderItem item : orderItems) {
                item.setOrder(savedOrder);
                orderItemRepository.save(item);
            }

            return savedOrder;
        } catch (Exception e) {
            throw new OrderException("An error occurred while creating the order: " + e.getMessage());
        }
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        try {
            Optional<Order> opt = orderRepository.findById(orderId);
            if (opt.isPresent()) {
                return opt.get();
            }
            throw new OrderException("Order Not Found");
        } catch (Exception e) {
            throw new OrderException("An error occurred while finding the order: " + e.getMessage());
        }
    }

    @Override
    public List<Order> userOrderHistoryLists(Long userId) throws OrderException {
        try {
            return orderRepository.getUsersOrders(userId);
        } catch (Exception e) {
            throw new OrderException("An error occurred while fetching the user's order history: " + e.getMessage());
        }
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        try {
            Order order = findOrderById(orderId);
            order.setOrderStatus("PLACED");
            order.getPaymentDetails().setStatus("COMPLETED");
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new OrderException("An error occurred while placing the order: " + e.getMessage());
        }
    }

    @Override
    public Order orderComfired(Long orderId) throws OrderException {
        try {
            Order order = findOrderById(orderId);
            order.setOrderStatus("CONFIRMED");
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new OrderException("An error occurred while confirming the order: " + e.getMessage());
        }
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        try {
            Order order = findOrderById(orderId);
            order.setOrderStatus("SHIPPED");
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new OrderException("An error occurred while shipping the order: " + e.getMessage());
        }
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        try {
            Order order = findOrderById(orderId);
            order.setOrderStatus("DELIVERED");
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new OrderException("An error occurred while delivering the order: " + e.getMessage());
        }
    }

    @Override
    public Order cancelOrder(Long orderId) throws OrderException {
        try {
            Order order = findOrderById(orderId);
            order.setOrderStatus("CANCELLED");
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new OrderException("An error occurred while canceling the order: " + e.getMessage());
        }
    }

    @Override
    public List<Order> getAllOrder() throws OrderException {
        try {
            return orderRepository.findAll();
        } catch (Exception e) {
            throw new OrderException("An error occurred while fetching all orders: " + e.getMessage());
        }
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        try {
            Order order = findOrderById(orderId);
            orderRepository.delete(order);
        } catch (Exception e) {
            throw new OrderException("An error occurred while deleting the order: " + e.getMessage());
        }
    }
}
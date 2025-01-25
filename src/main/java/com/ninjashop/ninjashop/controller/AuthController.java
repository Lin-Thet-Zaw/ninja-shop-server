package com.ninjashop.ninjashop.controller;

import com.ninjashop.ninjashop.config.JwtProvider;
import com.ninjashop.ninjashop.model.Cart;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.repository.UserRepository;
import com.ninjashop.ninjashop.request.LoginRequest;
import com.ninjashop.ninjashop.request.RegisterRequest;
import com.ninjashop.ninjashop.response.AuthResponse;
import com.ninjashop.ninjashop.service.CartService;
import com.ninjashop.ninjashop.service.CustomUserServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserServiceImplementation customUserServiceImplementation;
    private final CartService cartService;

    public AuthController(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder,
                          CustomUserServiceImplementation customUserServiceImplementation, CartService cartService) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.customUserServiceImplementation = customUserServiceImplementation;
        this.cartService = cartService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody RegisterRequest user, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                StringBuilder errorMessage = new StringBuilder("Signup validation failed: ");
                bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
                return new ResponseEntity<>(new AuthResponse(null, errorMessage.toString()), HttpStatus.BAD_REQUEST);
            }

            // Check if email already exists
            if (userRepository.findByEmail(user.getEmail()) != null) {
                return new ResponseEntity<>(new AuthResponse(null, "Email is already in use with another account."), HttpStatus.CONFLICT);
            }

            // Create and save user
            User newUser = new User();
            newUser.setEmail(user.getEmail());
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            User savedUser = userRepository.save(newUser);

            // Create a cart for the user
            Cart cart = cartService.createCart(savedUser);

            // Generate JWT token
            Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateToken(authentication);

            AuthResponse authResponse = new AuthResponse(token, "Signup successful");
            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthResponse(null, "An error occurred during signup: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                StringBuilder errorMessage = new StringBuilder("Signin validation failed: ");
                bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
                return new ResponseEntity<>(new AuthResponse(null, errorMessage.toString()), HttpStatus.BAD_REQUEST);
            }

            Authentication authentication = authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateToken(authentication);

            AuthResponse authResponse = new AuthResponse(token, "Signin successful");
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AuthResponse(null, "Invalid email or password."), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthResponse(null, "An error occurred during signin: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(username);
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid email or password.");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
package com.ninjashop.ninjashop.controller;

import com.ninjashop.ninjashop.config.JwtProvider;
import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.Cart;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.repository.UserRepository;
import com.ninjashop.ninjashop.request.LoginRequest;
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

@RestController
@RequestMapping("/auth")
public class AuthController {
    private  final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserServiceImplementation customUserServiceImplementation;
    private CartService cartService;

    public AuthController(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, CustomUserServiceImplementation customUserServiceImplementation, CartService cartService) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.customUserServiceImplementation = customUserServiceImplementation;
        this.cartService = cartService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user, BindingResult bindingResult) throws UserException {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Signup validation failed: ");
            bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new AuthResponse(null, errorMessage.toString()), HttpStatus.BAD_REQUEST);
        }

        // Validate required fields
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new UserException("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            throw new UserException("Password must be at least 8 characters");
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            throw new UserException("First name is required");
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            throw new UserException("Last name is required");
        }

        // Check if email already exists
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            throw new UserException("Email is already in use with another account");
        }

        // Create and save user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Create a cart for the user
        Cart cart = cartService.createCart(savedUser);

        // Generate JWT token
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        // Return success response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Signup successful");

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        // Validate email and password presence
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Signin validation failed: ");
            bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
            return new ResponseEntity<>(new AuthResponse(null, errorMessage.toString()), HttpStatus.BAD_REQUEST);
        }

        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
            return new ResponseEntity<>(new AuthResponse(null, "Email is required"), HttpStatus.BAD_REQUEST);
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            return new ResponseEntity<>(new AuthResponse(null, "Password is required"), HttpStatus.BAD_REQUEST);
        }

        // Authenticate user
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        try {
            Authentication authentication = authenticate(username, password);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateToken(authentication);

            // Return success response
            AuthResponse authResponse = new AuthResponse();
            authResponse.setJwt(token);
            authResponse.setMessage("Signin successful");

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            // Return invalid credentials response
            return new ResponseEntity<>(new AuthResponse(null, "Invalid email or password"), HttpStatus.UNAUTHORIZED);
        }
    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(username);
        if(userDetails == null){
            throw new BadCredentialsException("Invalid User");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}

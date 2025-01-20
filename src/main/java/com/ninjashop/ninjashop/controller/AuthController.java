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
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException{
        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        User isEmailExist = userRepository.findByEmail(email);

        if(isEmailExist != null){
            throw new UserException("Email is used already with another account");
        }

        User createUser = new User();
        createUser.setEmail(email);
        createUser.setPassword(passwordEncoder.encode(password));
        createUser.setFirstName(firstName);
        createUser.setLastName(lastName);

        User savedUser = userRepository.save(createUser);
        Cart cart = cartService.createCart(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Signup Success");

        return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest){

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Signin Success");

        return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
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

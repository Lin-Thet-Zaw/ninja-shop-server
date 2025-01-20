package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.config.JwtProvider;
import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService{

    private UserRepository userRepository;
    private JwtProvider jwtProvider;

    public UserServiceImplementation(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public User findByUserId(Long userId) throws UserException {
        Optional<User> user= userRepository.findById(userId);
        if(user.isPresent()){
            return user.get();
        }
        throw new UserException("User not found");
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);

        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserException("User not found"+email);
        }
        return user;
    }
}

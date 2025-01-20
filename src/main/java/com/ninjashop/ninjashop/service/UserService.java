package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.User;

public interface UserService {
    public User findByUserId(Long id) throws UserException;
    public  User findUserProfileByJwt(String jwt) throws UserException;
}

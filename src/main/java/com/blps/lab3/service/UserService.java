package com.blps.lab3.service;

import com.blps.lab3.model.User;
import com.blps.lab3.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User save(User user){
        if (userRepository.existsByEmail(user.getEmail())) return null;
        return userRepository.save(user);
    }
}

package com.blps.lab1.service;

import com.blps.lab1.model.*;
import com.blps.lab1.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException{
        com.blps.lab1.model.User user = userRepository.findByEmail(username); //username = email
        if(user == null) throw new UsernameNotFoundException("нет такого пользователя");
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER")//.roles(user.getRoles().toString())
                .authorities("CAN_PUBLISH")//.authorities(user.getAuthorities().toString())
                .build();
    }
}

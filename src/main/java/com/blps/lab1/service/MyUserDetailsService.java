package com.blps.lab1.service;

import com.blps.lab1.model.*;
import com.blps.lab1.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException{
        com.blps.lab1.model.User user = userRepository.findByEmail(username); //username = email
        if(user == null) throw new UsernameNotFoundException("нет такого пользователя");
        System.out.println("aaa");
        for(int i = 0; i < user.getStringRoles().length; i++) System.out.println("y");
        System.out.println(Arrays.toString(user.getStringRoles()));
        System.out.println(Arrays.toString(user.getAuthorities()));

        System.out.println("bbb");
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getStringRoles())
                .authorities(user.getAuthorities())//("VIEW_ALL_VACANCY", "POST_VACANCY", "VIEW_ALL_MODERATION")
                .build();
    }
}

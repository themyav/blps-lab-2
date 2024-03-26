package com.blps.lab1;

import com.blps.lab1.model.Role;
import com.blps.lab1.model.User;
import com.blps.lab1.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextListener;

import java.util.ArrayList;
import java.util.Collections;

@SpringBootApplication
public class Lab1Application {

	public static void main(String[] args) {
		//SpringApplication.run(Lab1Application.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(Lab1Application.class, args);
		UserService userService = context.getBean(UserService.class);


	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

}

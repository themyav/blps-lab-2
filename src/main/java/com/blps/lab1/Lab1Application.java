package com.blps.lab1;

import com.blps.lab1.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.blps.lab1.repo.UserRepository;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Lab1Application {

	public static void main(String[] args) {
		//SpringApplication.run(Lab1Application.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(Lab1Application.class, args);
		UserRepository userRepository = context.getBean(UserRepository.class);
		userRepository.save(new User("AA", "BB", "CCC"));
	}

}

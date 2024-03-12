package com.blps.lab1;

import com.blps.lab1.model.Privilege;
import com.blps.lab1.model.User;
import com.blps.lab1.repo.PrivilegeNames;
import com.blps.lab1.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.blps.lab1.repo.UserRepository;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class Lab1Application {

	public static void main(String[] args) {
		//SpringApplication.run(Lab1Application.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(Lab1Application.class, args);

		UserService userService = context.getBean(UserService.class);
		User user = new User("Иван", "Иванов", "Ivan@ivan.ru");
		userService.save(user);


	}

}

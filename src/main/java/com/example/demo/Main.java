package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

    //the problem is that the token is i think is generated after i the login method is called so i can't use it in the login-component probebly should i use it in the next components after login ,
	//the role is not correctly extracted from the token 
	//the files i modify : JWTService.java , JwtFilter.java , UserService.java , login-component.ts

}

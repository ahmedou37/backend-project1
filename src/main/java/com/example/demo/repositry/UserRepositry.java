package com.example.demo.repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface  UserRepositry extends JpaRepository<User,Integer> {

    public User findByName(String name);
    
}

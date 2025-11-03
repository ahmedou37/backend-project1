package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "users")   
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;
    private String name;
    private String password;
    private String role="USER";
    private String imageName;//in DB

    @Transient
    private String imageUrl; //not stored in DB — generated when returning dat

    @OneToMany(mappedBy ="assignedUser")
    private List<Task> tasks = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Notification> notifications=new ArrayList<>();

    public List<Task> getTasks() {
        return tasks;
    }
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public User() {} 

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public List<Notification> getNotifications() {
        return notifications;
    }
    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
     public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
}

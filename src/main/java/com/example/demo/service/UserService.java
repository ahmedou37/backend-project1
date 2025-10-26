package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.model.MyUserDetails;
import com.example.demo.model.Notification;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repositry.NotificationRepositry;
import com.example.demo.repositry.TaskRepository;
import com.example.demo.repositry.UserRepositry;


@Service
public class UserService {

    
    @Autowired
    UserRepositry userRepositry;

    @Autowired
    JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    NotificationRepositry notificationRepositry;

    BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(10);


    public void addUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        userRepositry.save(user);
    }
    
    public List<User> getUsers() {
        return userRepositry.findAll();
    }

    public String verify(User user) {
        Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
        //AuthenticationManager tries to authenticate using its providers.
        
        if (authentication.isAuthenticated()){

            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

            return jwtService.generateToken(user.getName(), userDetails.getAuthorities());
        }
        
        return "fail";

    }


    public List<Task> getTasks(String name) {
        User user = userRepositry.findByName(name);
        if (user != null) {
            return user.getTasks();  
        }
        return List.of();
    }


     public Task getTask(String name, int id) {
        User user = userRepositry.findByName(name);
        if (user != null) {
            return user.getTasks().stream()
                    .filter(t -> t.getId() == id)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public List<Task> getTasksByStatus(String name, String status){
        User user = userRepositry.findByName(name);
        if (user != null) {           
            return user.getTasks().stream()
                .filter(t -> t.getStatus().equals(status))
               .collect(Collectors.toList());
        } else {
            return null;
        }

    }

    public Task updateTaskStatus(Task task,String status){
        if(task!=null){
            task.setStatus(status);
            taskRepository.save(task);
        }
        return task;
    }
    
    public List<Notification> getNotifications(String name){
        User user =userRepositry.findByName(name);
        return user.getNotifications();
    }
    public List<Notification> getUnseenNotifications(String name){
        User user =userRepositry.findByName(name);
        List<Notification> unseenNotifications =new ArrayList<>();
        for(Notification notification :user.getNotifications()){
            if (notification.getSeen()==false) {
                unseenNotifications.add(notification);
            }
        };
        return unseenNotifications;
    }
    
    public void setToSeen(String name){
        User user =userRepositry.findByName(name);
        for(Notification not : user.getNotifications()){
            not.setSeen(true);
            notificationRepositry.save(not);
        }
    }
    public int getNotLenght(String name){
        User user =userRepositry.findByName(name);
        int number=0;
        for(Notification not : user.getNotifications()){
            if(not.getSeen()==false){
                number+=1;
            }
        }
        return number;
    }
}

package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Notification;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repositry.TaskRepository;
import com.example.demo.repositry.UserRepositry;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepositry userRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    
    @GetMapping("/token")
    public CsrfToken csrfToken(HttpServletRequest httpServlet) {
        return (CsrfToken) httpServlet.getAttribute("_csrf");
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }
    
    @PostMapping
    public User addUser(@RequestBody User user){
        userService.addUser(user);
        return user;
    }
    
    @PostMapping("/login")
    public String login(@RequestBody User user){
        return userService.verify(user);
    }

    @GetMapping("/tasks")
    public List<Task> getTasks(Authentication authentication) {
         String name=authentication.getName();
        return userService.getTasks(name);
        }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable int id, Authentication authentication) {
        // for(Task task:getTasks(authentication)){
        //  if (task.getId()==id){
        //     return task;
        //  }
        // }
        // return null;
        String name = authentication.getName();
        return userService.getTask(name, id);
    }

    @GetMapping("/tasks/filter")
    public List<Task> getTasksByStatus(@RequestParam String status, Authentication authentication,@RequestParam LocalDate deadline) {//(requiered)
        String name=authentication.getName();
        return userService.getTasksByStatus(name,status);
    }

    @PostMapping("tasks/{taskId}")
    public Task updateTaskStatus(@PathVariable int taskId ,Authentication authentication,@RequestParam String status){
        Task task =userService.getTask(authentication.getName(), taskId);
        return userService.updateTaskStatus(task, status);
    }
    
    @GetMapping("/tasks/not")
    public List<Notification> getNotifications(Authentication authentication){
        return userService.getNotifications(authentication.getName());
    }
    @GetMapping("/tasks/unseen")
    public List<Notification> getUseenNotifications(Authentication authentication){
        return userService.getUnseenNotifications(authentication.getName());
    }
    @GetMapping("/tasks/setSeen")
    public void setToSeen(Authentication authentication){
        userService.setToSeen(authentication.getName());
    }
    @GetMapping("tasks/notLenght")
    public int getNotLenght(Authentication authentication){
         int notLenght=userService.getNotLenght(authentication.getName());
         messagingTemplate.convertAndSend("/topic/notlenght",notLenght);
         return notLenght;
    }
    
}
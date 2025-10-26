package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationsController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @PostMapping("/send")
    public void sendNotification(@RequestBody String message) {
        System.out.println("New notification: " + message);
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
    
    @MessageMapping("/sendMessage")  // 👈 from Angular: /app/sendMessage
    @SendTo("/topic/messages")       // 👈 broadcast to: /topic/messages
    public String broadcastMessage(String message) {
        System.out.println("Received : " + message);
        return message; // This will be sent to all clients subscribed to /topic/messages
    }
}
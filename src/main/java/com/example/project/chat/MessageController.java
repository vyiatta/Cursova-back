package com.example.project.chat;

import com.example.project.chat.data.DTO.MessageDTO;
import com.example.project.chat.data.DTO.notificationToken;
import com.example.project.chat.data.Message;
import com.example.project.chat.services.MessageService;
import com.example.project.chat.services.notificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/chat")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private notificationService notificationService;
    @GetMapping("/messages-past-24")
    private List<Message> getAllUsersMessages(Authentication authentication){
        return messageService.getAllUserMessagesForPast24Hour(authentication.getPrincipal().toString());
    }
    @GetMapping("/manager/messages-past-24")
    private List<Message> getAllUsersMessages(@RequestParam String email){
        return messageService.getAllUserMessagesForPast24HourManager(email);
    }

    @PostMapping("/notification-token")
    private void setNotification(@RequestBody notificationToken notification){

        notificationService.setToken(notification);

    }

    @GetMapping("/manager/chats")
    private List<MessageDTO> findLast(){
        return messageService.getAllLastMessages();
    }


}

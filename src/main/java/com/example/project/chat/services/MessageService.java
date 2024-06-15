package com.example.project.chat.services;

import com.example.project.chat.data.DTO.MessageDTO;
import com.example.project.chat.data.DTO.MessageModel;
import com.example.project.chat.data.Message;
import com.example.project.users.Services.userRepo;
import com.example.project.users.data.DTO.userDTO;
import com.example.project.users.data.User;
import com.example.project.users.data.help.userRole;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private userRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;


    public List<Message> getAllUserMessagesForPast24Hour(String email){
        User user = userRepo.findByUserEmail(email);
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(24).minusDays(1);
        return messageRepo.findBySenderOrReceiverAndTimestampAfter(user,user,localDateTime);
    }
    public List<Message> getAllUserMessagesForPast24HourManager(String email){
        User user = userRepo.findByUserEmail(email);
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(24).minusDays(1);
        return messageRepo.findBySenderOrReceiverAndTimestampAfter(user,user,localDateTime);
    }

    public MessageDTO addNewMessage(MessageModel messageModel){
        Message message = new Message(userRepo.findByUserEmail(messageModel.getFrom()),userRepo.findByUserEmail(messageModel.getTo()),messageModel.getContent(),LocalDateTime.now(),false);
        messageRepo.save(message);
        return modelMapper.map(message,MessageDTO.class);
    }
    public List<MessageDTO> getAllLastMessages(){
        return messageRepo.findLastMessagesForUsers().stream().map(message -> modelMapper.map(message, MessageDTO.class)).collect(Collectors.toList());
    }
}

package com.example.project.chat.services;

import com.example.project.chat.data.DTO.notificationToken;
import com.example.project.chat.data.notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class notificationService {

    @Autowired
    private notificationRepo notificationRepo;
    public void setToken(notificationToken notificationToken){
        notification notification = notificationRepo.findByDeviceId(notificationToken.getDeviceId());
        if(notification == null){
            notification notification1 = new notification(notificationToken.getDeviceId(),notificationToken.getEmail());
            notificationRepo.save(notification1);
        }
        else if(!notification.getEmail().equals(notificationToken.getEmail())){
            notification.setEmail(notificationToken.getEmail());
            notificationRepo.save(notification);
        }
    }

    public List<notification> getAllByEmail(String email){
        return notificationRepo.findAllByEmail(email);
    }
}

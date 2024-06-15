package com.example.project.chat.services;

import com.example.project.chat.data.notification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface notificationRepo extends CrudRepository<notification,Long> {

    notification findByDeviceId(String deviceId);

    List<notification> findAllByEmail(String email);
}

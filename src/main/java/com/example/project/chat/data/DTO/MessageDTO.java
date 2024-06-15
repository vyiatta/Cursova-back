package com.example.project.chat.data.DTO;

import com.example.project.users.data.DTO.userDTO;
import com.example.project.users.data.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private Long id;


    private userDTO sender;


    private userDTO receiver;

    private String content;


    private LocalDateTime timestamp;
}

package com.example.project.users.data.DTO;

import com.example.project.users.data.help.userRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class userDTO {

    private Long userId;


    private String userName;


    private String userEmail;


    private String userPhoneNumber;


    private userRole userRole;
}

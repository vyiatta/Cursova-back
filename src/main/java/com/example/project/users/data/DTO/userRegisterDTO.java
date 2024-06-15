package com.example.project.users.data.DTO;

import com.example.project.users.data.help.userRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class userRegisterDTO {
    private String userName;

    private String userEmail;

    private String userPhoneNumber;


    private String userPassword;
}

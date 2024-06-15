package com.example.project.users.data.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class userLogin {
    private String userEmail;
    private String userPassword;
}

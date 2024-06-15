package com.example.project.users.data.DTO;

import com.example.project.users.data.help.registerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class userRegisterStatusDTO {
    private userDTO user;
    private registerStatus status;
}

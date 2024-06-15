package com.example.project.users.data.DTO;

import com.example.project.users.data.help.loginStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class loginResponseDTO {
    private loginStatus loginStatus;
    private String token;
    private userDTO user;
}

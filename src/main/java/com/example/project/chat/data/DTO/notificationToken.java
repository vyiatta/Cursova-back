package com.example.project.chat.data.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class notificationToken {
    private String deviceId;
    private String email;
}

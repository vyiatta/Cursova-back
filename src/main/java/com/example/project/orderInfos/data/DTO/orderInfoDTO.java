package com.example.project.orderInfos.data.DTO;

import com.example.project.orders.data.help.orderStatus;
import com.example.project.users.data.DTO.userDTO;
import com.example.project.users.data.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class orderInfoDTO {

    private userDTO user;

    private orderStatus previousStatus;
    private orderStatus NewStatus;

    private LocalDateTime changeTime;
}

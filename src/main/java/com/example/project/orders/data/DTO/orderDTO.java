package com.example.project.orders.data.DTO;

import com.example.project.orderInfos.data.orderInfo;
import com.example.project.orders.data.help.orderPaymentType;
import com.example.project.orders.data.help.orderStatus;
import com.example.project.users.data.DTO.userDTO;
import com.example.project.users.data.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class orderDTO {

    private Long orderId;

    private String customerName;

    private String chosenServices;

    private LocalDateTime dateOfCleaning;

    private String customerPhoneNumber;

    private String address;

    private Boolean usedDiscount;

    private orderStatus orderStatus;

    private Boolean goldenTicket;

    private Double orderSum;

    private userDTO user;

    private userDTO cleaner;

    private orderPaymentType orderPaymentType;
    private String orderNote;



}

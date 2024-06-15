package com.example.project.orders.data.DTO;

import com.example.project.orders.data.help.orderPaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class createOrderDTO {

    private String customerName;
    private String chosenServices;
    private LocalDateTime dateOfCleaning;
    private String customerPhoneNumber;
    private String address;
    private Boolean usedDiscount;
    private Boolean goldenTicket;
    private Double orderSum;
    private String orderNote;
    private orderPaymentType orderPaymentType;

}

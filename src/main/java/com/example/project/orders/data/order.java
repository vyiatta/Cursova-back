package com.example.project.orders.data;

import com.example.project.orderInfos.data.orderInfo;
import com.example.project.orders.data.help.orderPaymentType;
import com.example.project.orders.data.help.orderStatus;
import com.example.project.users.data.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="orders")
public class order {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Long orderId;

    @Column(name = "customerName")
    private String customerName;

    @Column(name = "chosenServices")
    private String chosenServices;

    @Column(name="dateOfCleaning")
    private LocalDateTime dateOfCleaning;

    @Column(name="customerPhoneNumber")
    private String customerPhoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name="usedDiscount")
    private Boolean usedDiscount;

    @Column(name = "orderStatus")
    private orderStatus orderStatus;

    @Column(name = "goldenTicket")
    private Boolean goldenTicket;

    @Column(name = "orderSum")
    private Double orderSum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cleaner_id")
    private User cleaner;

    @Column(name="orderNote",length = 1000)
    private String orderNote;

    @Column(name = "paymentType")
    private orderPaymentType orderPaymentType;



    public order(String customerName, String chosenServices, LocalDateTime dateOfCleaning, String customerPhoneNumber, String address, Boolean usedDiscount, com.example.project.orders.data.help.orderStatus orderStatus, Boolean goldenTicket, Double orderSum, User user, User cleaner, String orderNote, com.example.project.orders.data.help.orderPaymentType orderPaymentType) {
        this.customerName = customerName;
        this.chosenServices = chosenServices;
        this.dateOfCleaning = dateOfCleaning;
        this.customerPhoneNumber = customerPhoneNumber;
        this.address = address;
        this.usedDiscount = usedDiscount;
        this.orderStatus = orderStatus;
        this.goldenTicket = goldenTicket;
        this.orderSum = orderSum;
        this.user = user;
        this.cleaner = cleaner;
        this.orderNote = orderNote;
        this.orderPaymentType = orderPaymentType;
    }
}

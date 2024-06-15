package com.example.project.orderInfos.data;

import com.example.project.orders.data.help.orderStatus;
import com.example.project.orders.data.order;
import com.example.project.users.data.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="orderInfo")
public class orderInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "orderInfoId")
    private Long orderInfoId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private order order;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    private orderStatus previousStatus;
    private orderStatus NewStatus;

    private LocalDateTime changeTime;

    public orderInfo(com.example.project.orders.data.order order, User user, orderStatus previousStatus, orderStatus newStatus, LocalDateTime changeTime) {
        this.order = order;
        this.user = user;
        this.previousStatus = previousStatus;
        this.NewStatus = newStatus;
        this.changeTime = changeTime;
    }
}

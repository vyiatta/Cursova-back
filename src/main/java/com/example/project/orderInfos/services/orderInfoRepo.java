package com.example.project.orderInfos.services;

import com.example.project.orderInfos.data.orderInfo;
import com.example.project.orders.data.order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface orderInfoRepo extends CrudRepository<orderInfo,Long> {
    List<orderInfo> findAllByOrder(order order);
}

package com.example.project.orderInfos.services;

import com.example.project.orderInfos.data.DTO.orderInfoDTO;
import com.example.project.orderInfos.data.orderInfo;
import com.example.project.orders.data.DTO.orderDTO;
import com.example.project.orders.data.help.orderStatus;
import com.example.project.orders.data.order;
import com.example.project.orders.services.orderRepo;
import com.example.project.orders.services.orderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class orderInfoService {
    private final orderInfoRepo orderInfoRepo;
    private final ModelMapper modelMapper;
    private final orderRepo orderService;


    public orderInfoService(com.example.project.orderInfos.services.orderInfoRepo orderInfoRepo, ModelMapper modelMapper, orderRepo orderService) {
        this.orderInfoRepo = orderInfoRepo;
        this.modelMapper = modelMapper;
        this.orderService = orderService;
    }



    public void saveInfoAboutOrder(orderInfo orderInfo){
        orderInfoRepo.save(orderInfo);
    }

    public List<orderInfoDTO> findAllOrderInfo(Long orderId){
        order order = orderService.findById(orderId).get();
        return orderInfoRepo.findAllByOrder(order).stream().map(orderInfo -> modelMapper.map(orderInfo, orderInfoDTO.class)).collect(Collectors.toList());

    }
}

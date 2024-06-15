package com.example.project.orderInfos;

import com.example.project.orderInfos.data.DTO.orderInfoDTO;
import com.example.project.orderInfos.services.orderInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/order-info")
public class orderInfoController {
    private final orderInfoService orderInfoService;

    public orderInfoController(com.example.project.orderInfos.services.orderInfoService orderInfoService) {
        this.orderInfoService = orderInfoService;
    }

    @GetMapping("/order-logs/{orderId}")
    public List<orderInfoDTO> getAllOrderLogs(@PathVariable Long orderId){
        return orderInfoService.findAllOrderInfo(orderId);
    }
}

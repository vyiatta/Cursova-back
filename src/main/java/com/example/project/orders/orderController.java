package com.example.project.orders;

import com.example.project.orders.data.DTO.createOrderDTO;
import com.example.project.orders.data.DTO.orderDTO;
import com.example.project.orders.data.help.orderStatus;
import com.example.project.orders.services.orderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(path = "/order")
public class orderController {

    private final orderService orderService;

    public orderController(com.example.project.orders.services.orderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/manager/order/active")
    public ResponseEntity<List<orderDTO>> getAllActiveOrders(){
        try {
            return ResponseEntity.ok(orderService.getAllActiveOrders());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/manager/order/archived")
    public ResponseEntity<List<orderDTO>> getAllArchivedOrders(){
        try {
            return ResponseEntity.ok(orderService.getAllArchivedOrders());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/available-time-slots")
    public List<LocalTime[]> getAvailableTimeSlots(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateOfCleaning) {

        return orderService.findAvailableTimeSlots(dateOfCleaning);
    }

    @GetMapping("/countBeforeNextGoldenTicket")
    public Long getCountBeforeNextGoldenTicket(Authentication authentication) {
        return orderService.getCountOfOrdersAfterLastGoldenTicket(authentication.getPrincipal().toString());
    }

    @PostMapping("/create-order")
    public void createOrder(Authentication authentication,@RequestBody createOrderDTO createOrderDTO){
        orderService.createNewOrder(authentication.getPrincipal().toString(),createOrderDTO);
    }

    @PostMapping("/order-status")
    public orderDTO changeOrderStatus(Authentication authentication,@RequestParam(name = "orderId") Long orderId,@RequestParam(name = "orderStatus") orderStatus orderStatus){
        return orderService.changeOrderStatus(authentication.getPrincipal().toString(),orderId,orderStatus);
    }


    @GetMapping("/orders/getmy/{status}")
    public ResponseEntity<List<orderDTO>> getAllMyOrders(Authentication authentication,@PathVariable String status){
        try {
            return ResponseEntity.ok(orderService.getUserOrders(authentication.getPrincipal().toString(),status));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/confirm-order")
    public void   confirmTheOrder(
            Authentication authentication,
            @RequestParam Long orderId,
            @RequestParam String cleanerEmail
    ) {
        orderService.confirmTheOrder(authentication.getPrincipal().toString(),orderId,cleanerEmail);
    }

    @PostMapping("/cancel-order")
    public void   cancelTheOrder(
            Authentication authentication,
            @RequestParam Long orderId
    ) {
        orderService.cancelTheOrder(authentication.getPrincipal().toString(),orderId);
    }

    @GetMapping("/cleaner/orders")
    public List<orderDTO> getAllOrdersForCleaner(Authentication authentication){
        return orderService.getAllOrdersForCleaner(authentication.getPrincipal().toString());
    }

}

package com.example.project.orders.services;

import com.example.project.orderInfos.data.orderInfo;
import com.example.project.orderInfos.services.orderInfoService;
import com.example.project.orders.data.DTO.createOrderDTO;
import com.example.project.orders.data.DTO.orderDTO;
import com.example.project.orders.data.help.orderStatus;
import com.example.project.orders.data.order;
import com.example.project.users.Services.userRepo;
import com.example.project.users.data.User;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class orderService {
    private final orderRepo orderRepo;
    private final ModelMapper modelMapper;
    private final userRepo userRepo;
    @Getter
    private final orderInfoService orderInfoService;

    public orderService(com.example.project.orders.services.orderRepo orderRepo, ModelMapper modelMapper, com.example.project.users.Services.userRepo userRepo, com.example.project.orderInfos.services.orderInfoService orderInfoService) {
        this.orderRepo = orderRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
        this.orderInfoService = orderInfoService;
    }


    public List<orderDTO> getAllArchivedOrders(){
        List<order> activeOrders = orderRepo.findAllArchiveOrders(orderStatus.CANCELED,orderStatus.PAID);
        return activeOrders.stream().map(user -> modelMapper.map(user, orderDTO.class)).collect(Collectors.toList());
    }





    public List<LocalTime[]> findAvailableTimeSlots(LocalDate dateOfCleaning) {
        LocalTime openingTime = LocalTime.of(9, 0);  // Business hours opening time
        LocalTime closingTime = dateOfCleaning.getDayOfWeek().getValue() >= 6
                ? LocalTime.of(22, 1)  // Weekend closing time
                : LocalTime.of(20, 1); // Weekday closing time

        List<order> busyOrders = orderRepo.findAllByDateOfCleaningBetweenAndOrderStatusNot(
                dateOfCleaning.atStartOfDay(),
                dateOfCleaning.atTime(closingTime),orderStatus.CANCELED
        );

        List<LocalTime[]> availableTimeSlots = generateAllTimeSlots(openingTime, closingTime);
        List<LocalTime[]> validSlots = new ArrayList<>(availableTimeSlots);

        for (order busyOrder : busyOrders) {
            LocalTime busyStartTime = busyOrder.getDateOfCleaning().toLocalTime().minusHours(1);
            LocalTime busyEndTime = busyStartTime.plusHours(3);

            List<LocalTime[]> slotsToRemove = new ArrayList<>();

            for (LocalTime[] slot : validSlots) {
                LocalTime slotStartTime = slot[0];
                LocalTime slotEndTime = slot[1];

                if ((slotEndTime.isAfter(busyStartTime) && slotStartTime.isBefore(busyEndTime)) ||
                        (slotStartTime.equals(busyEndTime) && slotEndTime.equals(busyStartTime))) {
                    slotsToRemove.add(slot);
                }
            }

            validSlots.removeAll(slotsToRemove);
        }

        return validSlots;
    }

    private List<LocalTime[]> generateAllTimeSlots(LocalTime openingTime, LocalTime closingTime) {
        List<LocalTime[]> timeSlots = new ArrayList<>();
        LocalTime currentTime = openingTime;

        while (currentTime.plusHours(3).isBefore(closingTime)) {
            timeSlots.add(new LocalTime[]{currentTime, currentTime.plusHours(3)});
            currentTime = currentTime.plusMinutes(15);  // Adding 15 minutes interval
        }

        if (currentTime.isBefore(closingTime.minusHours(3))) {
            timeSlots.add(new LocalTime[]{currentTime, closingTime.minusHours(3)});
        }

        return timeSlots;
    }
    public Long getCountOfOrdersAfterLastGoldenTicket(String email) {
        User user = userRepo.findByUserEmail(email);
        List<order> userOrders = orderRepo.findByUserOrderByOrderIdDesc(user);

        int countAfterLastGoldenTicket = 0;

        for (order order : userOrders) {
            if (order.getGoldenTicket()) {
                break;
            }
            countAfterLastGoldenTicket++;
        }
        return 4 - (long) countAfterLastGoldenTicket;
    }
    public void createNewOrder(String email, createOrderDTO createOrderDTO){
        User user = userRepo.findByUserEmail(email);
        order newOrder = new order(createOrderDTO.getCustomerName(), createOrderDTO.getChosenServices(), createOrderDTO.getDateOfCleaning(), createOrderDTO.getCustomerPhoneNumber(), createOrderDTO.getAddress(),createOrderDTO.getUsedDiscount(),orderStatus.CREATED,createOrderDTO.getGoldenTicket(),createOrderDTO.getOrderSum(),user,null, createOrderDTO.getOrderNote(), createOrderDTO.getOrderPaymentType());
        orderRepo.save(newOrder);
        orderInfo orderInfo = new orderInfo(newOrder,user,null,orderStatus.CREATED,LocalDateTime.now());
        orderInfoService.saveInfoAboutOrder(orderInfo);
    }
    public orderDTO changeOrderStatus(String email,Long orderId,orderStatus orderStatus){
        order changedOrder = orderRepo.findById(orderId).get();
        orderStatus previousStatus = changedOrder.getOrderStatus();
        changedOrder.setOrderStatus(orderStatus);
        orderRepo.save(changedOrder);
        User initiator = userRepo.findByUserEmail(email);
        orderInfo orderInfo = new orderInfo(changedOrder,initiator,previousStatus,orderStatus,LocalDateTime.now());
        orderInfoService.saveInfoAboutOrder(orderInfo);
        return modelMapper.map(changedOrder,orderDTO.class);
    }


    public List<orderDTO> getUserOrders(String email,String status){
        User user = userRepo.findByUserEmail(email);
        switch (status) {
            case "Created":
                return orderRepo.findAllByUserAndOrderStatus(user,orderStatus.CREATED).stream().map(order -> modelMapper.map(order, orderDTO.class)).collect(Collectors.toList());
            case "InProcess":
               return orderRepo.findAllByUserAndOrderStatusNot(user,orderStatus.CANCELED,orderStatus.PAID,orderStatus.CREATED).stream().map(order -> modelMapper.map(order, orderDTO.class)).collect(Collectors.toList());
            case "Completed":
                return orderRepo.findAllByUserAndOrderStatus(user,orderStatus.PAID).stream().map(order -> modelMapper.map(order, orderDTO.class)).collect(Collectors.toList());
            default:
                return null;
        }
    }
    public List<orderDTO> getAllActiveOrders(){
         return orderRepo.findAllActiveOrders(orderStatus.PAID,orderStatus.CANCELED).stream().map(order -> modelMapper.map(order, orderDTO.class)).collect(Collectors.toList());

    }

    public void confirmTheOrder(String email, Long id,String cleanerEmail){
        order changedOrder = orderRepo.findById(id).get();
        changedOrder.setOrderStatus(orderStatus.CLEANER_APPOINTED);
        User cleaner = userRepo.findByUserEmail(cleanerEmail);
        changedOrder.setCleaner(cleaner);
        orderRepo.save(changedOrder);
        orderInfo orderInfo = new orderInfo(changedOrder,userRepo.findByUserEmail(email),orderStatus.CREATED,orderStatus.CLEANER_APPOINTED,LocalDateTime.now());
        orderInfoService.saveInfoAboutOrder(orderInfo);
    }
    public void cancelTheOrder(String email, Long id){
        order changedOrder = orderRepo.findById(id).get();
        orderStatus previousStatus = changedOrder.getOrderStatus();
        changedOrder.setOrderStatus(orderStatus.CANCELED);
        orderRepo.save(changedOrder);
        orderInfo orderInfo = new orderInfo(changedOrder,userRepo.findByUserEmail(email),previousStatus,orderStatus.CANCELED,LocalDateTime.now());
        orderInfoService.saveInfoAboutOrder(orderInfo);
    }

    public List<orderDTO> getAllOrdersForCleaner(String email){

        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        User specificCleaner = userRepo.findByUserEmail(email);

        List<orderStatus> scheduledStatuses = Arrays.asList(
                orderStatus.CLEANER_APPOINTED,
                orderStatus.ON_THE_WAY,
                orderStatus.STARTED,
                orderStatus.FINISHED
        );

        List<order> ordersForToday = orderRepo.findAllByCleanerAndDateOfCleaningBetweenAndOrderStatusIn(
                specificCleaner,
                startOfDay,
                endOfDay,
                scheduledStatuses
        );
        return ordersForToday.stream().map(order -> modelMapper.map(order, orderDTO.class)).collect(Collectors.toList());
    }
}
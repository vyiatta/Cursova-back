package com.example.project.orders.services;

import com.example.project.orders.data.help.orderStatus;
import com.example.project.orders.data.order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.project.users.data.User;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface orderRepo extends CrudRepository<order,Long> {

    List<order> findAllByOrderStatus(orderStatus status);

    List<order> findAllByDateOfCleaningBetweenAndOrderStatusNot(LocalDateTime startDay, LocalDateTime endDay,orderStatus orderStatus);

    List<order> findByUserOrderByOrderIdDesc(User user);
    @Query("SELECT o FROM order o WHERE o.user = ?1 AND o.orderStatus NOT IN (?2, ?3,?4)")
    List<order> findAllByUserAndOrderStatusNot(User user, orderStatus orderStatus1, orderStatus orderStatus2,orderStatus orderStatus3);

    List<order> findAllByUserAndOrderStatus(User user,orderStatus orderStatus);

    @Query("SELECT o FROM order o WHERE o.orderStatus NOT IN (?1, ?2)")
    List<order> findAllActiveOrders(orderStatus orderStatus1, orderStatus orderStatus2);

    @Query("SELECT o FROM order o WHERE o.orderStatus IN (?1, ?2)")
    List<order> findAllArchiveOrders(orderStatus orderStatus1, orderStatus orderStatus2);

    List<order> findAllByCleanerAndDateOfCleaningBetweenAndOrderStatusIn(
            User cleaner,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay,
            List<orderStatus> orderStatusList
    );
}

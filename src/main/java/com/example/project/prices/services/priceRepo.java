package com.example.project.prices.services;

import com.example.project.prices.data.price;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface priceRepo extends CrudRepository<price,String> {

    @Transactional
    @Modifying
    @Query("UPDATE price p SET p.price = :newPrice WHERE p.priceId = :priceId")
    void updatePriceValueById(String priceId, Double newPrice);
}

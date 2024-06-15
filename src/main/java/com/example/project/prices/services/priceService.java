package com.example.project.prices.services;

import com.example.project.prices.data.price;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class priceService {

    private final priceRepo priceRepo;

    public priceService(com.example.project.prices.services.priceRepo priceRepo) {
        this.priceRepo = priceRepo;
    }

    public Iterable<price> getAllprices()
    {
        return priceRepo.findAll();
    }

    public void updatePriceValueById(String priceId, Double newPrice) {
        priceRepo.updatePriceValueById(priceId, newPrice);
    }
}

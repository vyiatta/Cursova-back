package com.example.project.prices;

import com.example.project.prices.data.price;
import com.example.project.prices.services.priceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/price")
public class priceController {

    private  final priceService priceService;

    public priceController(com.example.project.prices.services.priceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<price>> getAll(){
        try {
            return ResponseEntity.ok(priceService.getAllprices());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{priceId}")
    public ResponseEntity<String> updatePriceValueById(
            @PathVariable String priceId,
            @RequestParam Double newPrice) {

        try {
            priceService.updatePriceValueById(priceId, newPrice);
            return ResponseEntity.ok("Price value updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating price value: " + e.getMessage());
        }
    }
}

package com.example.project.prices.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="prices")
public class price {
    @Id
    @Column(name = "priceId")
    private String priceId;


    @Column(name = "priceTittle")
    private String priceTittle;

    @Column(name="price")
    private Double price;
}

package com.garja.Garja.Model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String productName;

    private String price;

    private String originalPrice;

    private String discount;

    private int quantity;

    private String isActive;

    private String description;

    private String XS;

    private String M;

    private String L;

    private String XL;

    private String XXL;

    private String imageUrl;

    private String imagePublicId;


    private String category;

    private String date;

    private String time;


    @OneToMany
    private List<Review> reviews;

    
}

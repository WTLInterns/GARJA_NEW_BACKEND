package com.garja.Garja.DTO.requests;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequests {
    

    private String productName;

    private String price;

    private int quantity;

    private boolean isActive;

    private String description;

    private String XS;

    private String M;

    private String L;

    private String XL;

    private String XXL;

    private MultipartFile image;

    private String date;

    private String time;

    

    private String category;
}

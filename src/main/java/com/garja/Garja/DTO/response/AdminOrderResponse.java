package com.garja.Garja.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminOrderResponse {
    // Order details
    private int id;
    private String orderDate;
    private double totalAmount;
    private String status;
    private String productName;
    private int quantity;
    private String size;
    private String image;

    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}

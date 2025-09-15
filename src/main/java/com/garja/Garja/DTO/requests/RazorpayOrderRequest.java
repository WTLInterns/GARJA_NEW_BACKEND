package com.garja.Garja.DTO.requests;

import lombok.Data;

@Data
public class RazorpayOrderRequest {
    private double amount;
    private String currency = "INR";
    private String receipt;
}

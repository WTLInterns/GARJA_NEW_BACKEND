package com.garja.Garja.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garja.Garja.DTO.requests.ProductRequests;
import com.garja.Garja.DTO.response.ProductResponse;
import com.garja.Garja.Service.ProductService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private ProductService productService;

    @PostMapping("/addProduct")
    public ProductResponse addProduct(ProductRequests ProductRequests) {
        return productService.addProduct(ProductRequests);
    }
}

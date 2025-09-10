package com.garja.Garja.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.garja.Garja.DTO.requests.ProductRequests;
import com.garja.Garja.DTO.response.ProductResponse;
import com.garja.Garja.Model.Product;
import com.garja.Garja.Service.ProductService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private ProductService productService;

    @PostMapping("/addProduct")
    public ProductResponse addProduct(@RequestBody ProductRequests ProductRequests) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        return productService.addProduct(ProductRequests,email);
    }

    @GetMapping("/getAllProducts")
    public List<Product> getAllProducts(){
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        return productService.getAllProducts(email);
    }

    @GetMapping("/getProductByCategory")
    public List<Product> getAllProductsByCategory(@RequestParam String category){ 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        return this.productService.getProductsByCategory(category,email);
    }

    @GetMapping("/getLatestProducts")
    public List<Product> getLatestProducts(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        return this.productService.getLatestProducts(email);
    }




}

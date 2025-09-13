package com.garja.Garja.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.garja.Garja.Model.Product;
import com.garja.Garja.Service.ProductService;

@RestController
@RequestMapping("/public")  
public class PublicController {


    @Autowired
    private ProductService productService;
    

     @GetMapping("/getAllProducts")
    public List<Product> getAllProducts(){
         
        return productService.getAllProducts();
    }

    @GetMapping("/getProductByCategory")
    public List<Product> getAllProductsByCategory(@RequestParam String category){ 
        
        return this.productService.getProductsByCategory(category);
    }

    @GetMapping("/getLatestProducts")
    public List<Product> getLatestProducts(){
        
        return this.productService.getLatestProducts();
    }



    @GetMapping("/getProductById/{id}")
    public Product getProductById(@PathVariable int id){
        return this.productService.getByIdProductId(id);
    } 

    
}

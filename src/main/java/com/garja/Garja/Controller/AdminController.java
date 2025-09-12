package com.garja.Garja.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.garja.Garja.DTO.requests.ProductRequests;
import com.garja.Garja.DTO.response.ProductResponse;
import com.garja.Garja.DTO.response.AdminOrderResponse;
import com.garja.Garja.Model.Product;
import com.garja.Garja.Service.ProductService;
import com.garja.Garja.Service.OrderService;

@RestController
@RequestMapping("/admin")
public class  AdminController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/addProduct", consumes = "multipart/form-data")
    public ProductResponse addProduct(@ModelAttribute ProductRequests productRequests) throws IOException {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        return productService.addProduct(productRequests,email);
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable int id,
            @ModelAttribute ProductRequests request) throws IOException {

              Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();

        ProductResponse response = productService.updateProduct(id, request,email);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable int id) throws IOException {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        return ResponseEntity.ok(productService.deleteProduct(id,email));
    }

    @GetMapping("/getAllProducts")
    public List<Product> getAllProducts(){
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        return productService.getAllProducts();
    }

    @GetMapping("/getProductByCategory")
    public List<Product> getAllProductsByCategory(@RequestParam String category){ 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        return this.productService.getProductsByCategory(category);
    }

    @GetMapping("/getLatestProducts")
    public List<Product> getLatestProducts(){
      //   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			// String email = authentication.getName();
        return this.productService.getLatestProducts();
    }

    @GetMapping("/orders")
    public ResponseEntity<List<AdminOrderResponse>> getAllOrders() {
        List<AdminOrderResponse> orders = orderService.getAllOrdersForAdmin();
        return ResponseEntity.ok(orders);
    }
}

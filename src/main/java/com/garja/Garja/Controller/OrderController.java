package com.garja.Garja.Controller;

import com.garja.Garja.DTO.requests.BuyNowRequest;
import com.garja.Garja.DTO.response.OrderResponse;
import com.garja.Garja.Service.OrderService;
import com.garja.Garja.Repo.UserRepo;
import com.garja.Garja.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserRepo userRepository;

    @PostMapping("/buy-now")
    public ResponseEntity<OrderResponse> buyNow(@RequestBody BuyNowRequest request) {
        try {
            // Get user ID from JWT token instead of path parameter for security
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Integer userId = getUserIdFromEmail(email);
            
            OrderResponse response = orderService.buyNow(userId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            OrderResponse errorResponse = new OrderResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkoutCart() {
        try {
            // Get user ID from JWT token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Integer userId = getUserIdFromEmail(email);
            
            OrderResponse response = orderService.checkoutCart(userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            OrderResponse errorResponse = new OrderResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderResponse>> getPurchaseHistory() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Integer userId = getUserIdFromEmail(email);

            List<OrderResponse> history = orderService.getPurchaseHistory(userId);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Helper method to get userId from email
    private Integer getUserIdFromEmail(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new RuntimeException("Email is required");
            }
            
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            return user.getId();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user: " + e.getMessage());
        }
    }
}

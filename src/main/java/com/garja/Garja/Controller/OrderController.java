package com.garja.Garja.Controller;

import com.garja.Garja.DTO.requests.BuyNowRequest;
import com.garja.Garja.DTO.response.OrderResponse;
import com.garja.Garja.Service.OrderService;
import com.garja.Garja.Repo.UserRepo;
import com.garja.Garja.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.garja.Garja.DTO.requests.RazorpayOrderRequest;
import com.garja.Garja.DTO.requests.RazorpayPaymentVerificationRequest;
import org.json.JSONObject;
import com.razorpay.RazorpayException;

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
    public ResponseEntity<OrderResponse> checkoutCart(@RequestParam int addressId) {
        try {
            // Get user ID from JWT token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Integer userId = getUserIdFromEmail(email);
            
            OrderResponse response = orderService.checkoutCart(userId, addressId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            OrderResponse errorResponse = new OrderResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Create Razorpay Order (called before opening Razorpay checkout on frontend)
    @PostMapping("/create-razorpay-order")
    public ResponseEntity<?> createRazorpayOrder(@RequestBody RazorpayOrderRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Integer userId = getUserIdFromEmail(email);

            var order = orderService.createRazorpayOrder(request, userId);
            return ResponseEntity.ok(order);
        } catch (RazorpayException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create Razorpay order: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Verify payment signature and create orders from cart
    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody RazorpayPaymentVerificationRequest request, @RequestParam int addressId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Integer userId = getUserIdFromEmail(email);

            OrderResponse response = orderService.verifyAndSaveOrder(request, userId, addressId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
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

package com.garja.Garja.Controller;

import com.garja.Garja.DTO.response.CartResponse;
import com.garja.Garja.Service.CartService;
import com.garja.Garja.Repo.UserRepo;
import com.garja.Garja.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepo userRepository;

    @PostMapping("/add/{productId}")
    public ResponseEntity<CartResponse> addProductToCart(
            @PathVariable Integer productId,
            @RequestParam(defaultValue = "1") int quantity) {
        try {
            Integer userId = getUserIdFromJWT();
            return ResponseEntity.ok(cartService.addProductToCart(userId, productId, quantity));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        try {
            Integer userId = getUserIdFromJWT();
            return ResponseEntity.ok(cartService.getCart(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartResponse> removeProductFromCart(@PathVariable Integer productId) {
        try {
            Integer userId = getUserIdFromJWT();
            return ResponseEntity.ok(cartService.removeProductFromCart(userId, productId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<CartResponse> updateProductQuantity(
            @PathVariable Integer productId,
            @RequestParam int quantity) {
        try {
            Integer userId = getUserIdFromJWT();
            return ResponseEntity.ok(cartService.updateProductQuantity(userId, productId, quantity));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/size/{productId}")
    public ResponseEntity<CartResponse> updateProductSize(
            @PathVariable Integer productId,
            @RequestParam String size) {
        try {
            Integer userId = getUserIdFromJWT();
            return ResponseEntity.ok(cartService.updateProductSize(userId, productId, size));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {
        try {
            Integer userId = getUserIdFromJWT();
            cartService.clearCart(userId);
            return ResponseEntity.ok("Cart cleared successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to clear cart");
        }
    }

    // Helper method to get userId from JWT token
    private Integer getUserIdFromJWT() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getName() == null) {
                throw new RuntimeException("Authentication required");
            }
            
            String email = authentication.getName();
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            return user.getId();
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
}

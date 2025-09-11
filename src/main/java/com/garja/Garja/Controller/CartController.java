package com.garja.Garja.Controller;

import com.garja.Garja.Model.Cart;
import com.garja.Garja.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<Cart> addProductToCart(
            @PathVariable Integer userId,
            @PathVariable Integer productId,
            @RequestParam(defaultValue = "1") int quantity) {

        return ResponseEntity.ok(cartService.addProductToCart(userId, productId, quantity));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Integer userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<Cart> removeProductFromCart(
            @PathVariable Integer userId,
            @PathVariable Integer productId) {

        return ResponseEntity.ok(cartService.removeProductFromCart(userId, productId));
    }


    @PutMapping("/{userId}/update/{productId}")
    public ResponseEntity<Cart> updateProductQuantity(
            @PathVariable Integer userId,
            @PathVariable Integer productId,
            @RequestParam int quantity) {

        return ResponseEntity.ok(cartService.updateProductQuantity(userId, productId, quantity));
    }


    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable Integer userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }
}

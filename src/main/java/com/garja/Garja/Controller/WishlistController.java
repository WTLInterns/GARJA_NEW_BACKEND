package com.garja.Garja.Controller;

import com.garja.Garja.DTO.response.WishlistResponse;
import com.garja.Garja.Service.WishlistService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    //add product to wishlist
    @PostMapping("/{userId}/{productId}")
    public ResponseEntity<String> addToWishlist(@PathVariable int userId,@PathVariable int productId){
        return ResponseEntity.ok(wishlistService.addToWishlist(userId,productId));
    }


    @PostMapping("{userId}/{productId}")
    public ResponseEntity<String> removeProductFromWishlist(@PathVariable int userId,@PathVariable int productId){
        return ResponseEntity.ok(wishlistService.removeProductFromWishlist(userId,productId));
    }

    // remove the wishlist
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<String> removeFromWishlist(@PathVariable int wishlistId){
        return ResponseEntity.ok(wishlistService.removeFromWishlist(wishlistId));
    }

    // get user wishlist
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WishlistResponse>> getUserWishlist(@PathVariable int userId){
        return ResponseEntity.ok(wishlistService.getUserWishlist(userId));
    }
}

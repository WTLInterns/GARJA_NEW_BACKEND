package com.garja.Garja.Service;


import com.garja.Garja.DTO.response.WishlistResponse;
import com.garja.Garja.Model.Product;
import com.garja.Garja.Model.User;
import com.garja.Garja.Model.Wishlist;
import com.garja.Garja.Repo.ProductRepo;
import com.garja.Garja.Repo.UserRepo;
import com.garja.Garja.Repo.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepo userRepository;
    private final ProductRepo productRepository;

    public String addToWishlist(int userId,int productId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("Product not found"));

        if (wishlistRepository.findByUserIdAndProductId(userId,productId).isPresent()){
            return "Product Already in wishlist";
        }
        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .build();
        wishlistRepository.save(wishlist);
        return "Product added to Wishlist";
    }

    public String removeFromWishlist(int wishlistId){
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(()->new RuntimeException("Wishlist item not found"));
        wishlistRepository.delete(wishlist);
        return "Product removed from wishlist";
    }

    public List<WishlistResponse> getUserWishlist(int userId) {
        List<Wishlist> wishlistItems = wishlistRepository.findByUserId(userId);

        List<WishlistResponse> responseList = wishlistItems.stream()
                .map(item-> WishlistResponse.builder()
                        .wishlistId(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getProductName())
                        .productImage(item.getProduct().getImageUrl())
                        .productPrice(item.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());
        return responseList;
    }

    public String removeProductFromWishlist(int userId, int productId) {
        List<Wishlist> wishlistItems = wishlistRepository.findByUserId(userId);

        Wishlist wishlistItem = wishlistItems.stream()
                .filter(item -> item.getProduct().getId()==productId)
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Product not found in wishlist"));

        wishlistRepository.delete(wishlistItem);
        return "Product removed from wishlist successfully!";
    }
}

package com.garja.Garja.Service;

import com.garja.Garja.Model.Cart;
import com.garja.Garja.Model.CartItem;
import com.garja.Garja.Model.Product;
import com.garja.Garja.Model.User;

import com.garja.Garja.Repo.CartItemRepository;
import com.garja.Garja.Repo.CartRepository;
import com.garja.Garja.Repo.ProductRepo;
import com.garja.Garja.Repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepo productRepository;
    private final UserRepo userRepository;

    // for creating or getting the cart, we will find if the cart is present for that user if yes then we will fetch that cart if not
    // then we will assign him newCart
    // remember this is helper method for get or create a cart whi will use full in below apis
    @Transactional
    public Cart getOrCreateCart(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(null, user, List.of())));
    }


    @Transactional
    public Cart addProductToCart(Integer userId, Integer productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // check if product already exists in cart
        CartItem existingItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId() == (productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem(null, quantity, cart, product);
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        return cartRepository.save(cart);
    }


    public Cart getCart(Integer userId) {
        return getOrCreateCart(userId);
    }

    @Transactional
    public Cart removeProductFromCart(Integer userId, Integer productId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().removeIf(item -> item.getProduct().getId() == (productId));
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateProductQuantity(Integer userId, Integer productId, int quantity) {
        Cart cart = getOrCreateCart(userId);

        cart.getItems().forEach(item -> {
            if (item.getProduct().getId() == (productId)) {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        });

        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Integer userId) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
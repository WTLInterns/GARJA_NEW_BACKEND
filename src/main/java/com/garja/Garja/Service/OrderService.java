package com.garja.Garja.Service;

import com.garja.Garja.DTO.requests.BuyNowRequest;
import com.garja.Garja.DTO.response.OrderResponse;
import com.garja.Garja.Model.Cart;
import com.garja.Garja.Model.Product;
import com.garja.Garja.Model.User;
import com.garja.Garja.Model.UserOrders;
import com.garja.Garja.Repo.ProductRepo;
import com.garja.Garja.Repo.UserRepo;
import com.garja.Garja.Repo.UserOrdersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepo userRepository;
    private final ProductRepo productRepository;
    private final UserOrdersRepo orderRepository;
    private final CartService cartService;

    @Transactional
    public OrderResponse buyNow(Integer userId, BuyNowRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Validate product is active
        if (!product.isActive()) {
            throw new RuntimeException("Product is not available");
        }

        // Validate stock
        if (product.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock available");
        }

        // Calculate total amount
        double price;
        try {
            price = Double.parseDouble(product.getPrice());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid product price format");
        }
        double totalAmount = price * request.getQuantity();

        // Deduct stock
        product.setQuantity(product.getQuantity() - request.getQuantity());
        productRepository.save(product);

        // Create order
        UserOrders order = new UserOrders();
        order.setUser(user);
        order.setProductName(product.getProductName());
        order.setQuantity(request.getQuantity());
        order.setSize(request.getSize());
        order.setImage(product.getImageUrl());
        order.setTotalAmount(totalAmount);
        order.setStatus("CONFIRMED");
        order.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        UserOrders savedOrder = orderRepository.save(order);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderDate(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus(),
                savedOrder.getProductName(),
                savedOrder.getQuantity(),
                savedOrder.getSize(),
                savedOrder.getImage(),
                user.getId(),
                "Order placed successfully"
        );
    }

    @Transactional
    public OrderResponse checkoutCart(Integer userId) {
        Cart cart = cartService.getOrCreateCart(userId);
        
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        User user = cart.getUser();
        double totalAmount = 0;
        int totalQuantity = 0;
        int itemCount = cart.getItems().size();

        // Validate all items and calculate totals
        for (var item : cart.getItems()) {
            Product product = item.getProduct();
            
            if (!product.isActive()) {
                throw new RuntimeException("Product " + product.getProductName() + " is no longer available");
            }
            
            if (product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getProductName());
            }
            
            double price;
            try {
                price = Double.parseDouble(product.getPrice());
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid price format for product: " + product.getProductName());
            }
            totalAmount += price * item.getQuantity();
            totalQuantity += item.getQuantity();
        }

        // Deduct stock for all items
        for (var item : cart.getItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        // Create one order per cart item (purchase history: one row per product)
        for (var item : cart.getItems()) {
            Product product = item.getProduct();
            double price = Double.parseDouble(product.getPrice());
            double lineTotal = price * item.getQuantity();

            UserOrders lineOrder = new UserOrders();
            lineOrder.setUser(user);
            lineOrder.setProductName(product.getProductName());
            lineOrder.setQuantity(item.getQuantity());
            lineOrder.setSize(item.getSize() == null ? "NA" : item.getSize());
            lineOrder.setImage(product.getImageUrl());
            lineOrder.setTotalAmount(lineTotal);
            lineOrder.setStatus("CONFIRMED");
            lineOrder.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            orderRepository.save(lineOrder);
        }

        // Clear cart after successful order
        cartService.clearCart(userId);

        // Return a summary response (keeps API backward compatible)
        return new OrderResponse(
                0, // summary, not a single order id
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                totalAmount,
                "CONFIRMED",
                "MULTIPLE ITEMS (" + itemCount + ")",
                totalQuantity,
                "MIXED",
                null,
                user.getId(),
                "Order placed successfully from cart with " + itemCount + " items"
        );
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getPurchaseHistory(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserOrderByIdDesc(user).stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getOrderDate(),
                        order.getTotalAmount(),
                        order.getStatus(),
                        order.getProductName(),
                        order.getQuantity(),
                        order.getSize(),
                        order.getImage(),
                        user.getId(),
                        ""
                ))
                .collect(Collectors.toList());
    }
}


package com.garja.Garja.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garja.Garja.DTO.requests.ProductRequests;
import com.garja.Garja.DTO.response.ProductResponse;
import com.garja.Garja.Model.Product;
import com.garja.Garja.Model.User;
import com.garja.Garja.Repo.ProductRepo;
import com.garja.Garja.Repo.UserRepo;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    public ProductResponse addProduct(ProductRequests ProductRequests, String email) {
        User user = this.userRepo.findByEmail(email);

        Product products = new Product();
        LocalDateTime now = LocalDateTime.now();

        products.setProductName(ProductRequests.getProductName());
        products.setPrice(ProductRequests.getPrice());
        products.setQuantity(ProductRequests.getQuantity());
        products.setDescription(ProductRequests.getDescription());
        products.setXS(ProductRequests.getXS());
        products.setM(ProductRequests.getM());

        products.setL(ProductRequests.getL());
        products.setXL(ProductRequests.getXL());
        products.setXXL(ProductRequests.getXXL());
        products.setCategory(ProductRequests.getCategory());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = now.format(dateFormatter);
        products.setDate(date);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = now.format(timeFormatter);

        products.setTime(time);
        productRepo.save(products);

        return new ProductResponse(products.getId(), "Product Added Successfully", products.getProductName());

    }

    public List<Product> getAllProducts(String email) {
        User user = this.userRepo.findByEmail(email);

        return productRepo.findAll();
    }

    public List<Product> getProductsByCategory(String category, String email) {
        User user = this.userRepo.findByEmail(email);

        return productRepo.findAll().stream().filter(product -> product.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public List<Product> getLatestProducts(String email) {
        User user = this.userRepo.findByEmail(email);

        return productRepo.findTop4ByOrderByDateDescTimeDesc();
    }

}

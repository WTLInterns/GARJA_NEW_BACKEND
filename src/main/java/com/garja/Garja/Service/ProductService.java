package com.garja.Garja.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garja.Garja.DTO.requests.ProductRequests;
import com.garja.Garja.DTO.response.ProductResponse;
import com.garja.Garja.Model.Product;
import com.garja.Garja.Repo.ProductRepo;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;
    

    public ProductResponse addProduct(ProductRequests ProductRequests){
        Product products = new Product();
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
        productRepo.save(products);

        return new ProductResponse(products.getId(), "Product Added Successfully", products.getProductName());

    }


    
}

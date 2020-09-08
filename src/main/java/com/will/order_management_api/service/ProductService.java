package com.will.order_management_api.service;

import com.will.order_management_api.entities.Product;
import com.will.order_management_api.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepo theProductRepo;

    public Product post(Product theProduct) {
        return theProductRepo.save(theProduct);
    }

    public List<Product> getAll() {
        return theProductRepo.findAll();
    }

    //wrap search result in a optional
    public Optional<Product> getByName(String theProductName) {
        Optional result = Optional.of(theProductRepo.findByName(theProductName));
        return result;
    }
}

package com.will.order_management_api.controller;

import com.will.order_management_api.entities.Product;
import com.will.order_management_api.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductController provides endpoints to clients to add product items into database product table
 * as well as query functionality
 * @author Will
 */


@RestController
@RequestMapping("/product")
public class ProductController {

    //TODO:   httpstatus code

    @Autowired
    private ProductRepo theProductRepo;

    @PostMapping
    public Product createNewProduct(@RequestBody Product theProduct) {
        return theProductRepo.save(theProduct);
    }

    @GetMapping
    public List<Product> getAll() {
        return theProductRepo.findAll();
    }

}

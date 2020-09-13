package com.will.order_management_api.service;

import com.will.order_management_api.entities.Product;
import com.will.order_management_api.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * ProductService class provides a service layer to our API and it interact between product DAO layer and our product controller.
 * @author Will
 */


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
        Product temp = theProductRepo.findByName(theProductName);
        if(temp == null) {
            return Optional.empty();
        } else {
            return Optional.of(temp);
        }
    }

    public Optional<Product> getById(Long id) {
        return theProductRepo.findById(id);
    }

    public Product put(Product productToSave) {
        return theProductRepo.save(productToSave);
    }

    public void deleteById(Long id) {
        theProductRepo.deleteById(id);
    }
}

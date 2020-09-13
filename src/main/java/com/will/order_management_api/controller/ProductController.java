package com.will.order_management_api.controller;

import com.will.order_management_api.entities.Product;
import com.will.order_management_api.exception.IdNotFoundException;
import com.will.order_management_api.exception.InvalidItemNameException;
import com.will.order_management_api.exception.ProductCreationException;
import com.will.order_management_api.repository.ProductRepo;
import com.will.order_management_api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * ProductController provides endpoints to clients to add product items into database product table
 * as well as query functionality
 * @author Will
 */


@RestController
@RequestMapping("/product")
public class ProductController {


    @Autowired
    private ProductService theProductService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createNewProduct(@RequestBody Product theProduct) {
        //check if the item is already existing in db
        //if so throw a ProductCreationException
        if(theProductService.getByName(theProduct.getName()).isPresent()) {
            throw new ProductCreationException(theProduct.getName() + " already exist in the database");
        }
        System.out.println(theProduct.getName());
        System.out.println(theProduct.getPrice());
        return theProductService.post(theProduct);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAll() {
        return theProductService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product getById(@PathVariable Long id) throws IdNotFoundException{
        Optional<Product> optionalProduct = theProductService.getById(id);
        if(optionalProduct.isEmpty()) {
            throw new IdNotFoundException("there is no corresponding order record with this id in database");
        } else {
            return optionalProduct.get();
        }
    }
    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public Product getByName(@RequestParam(value = "name", required = false) String itemName) {

        Optional<Product> optionalProduct = theProductService.getByName(itemName);
        //check if name given is valid
        if(optionalProduct.isEmpty()) {
            throw new InvalidItemNameException("there is no such item: " + itemName + "!!!");
        } else {
            return optionalProduct.get();
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public  Product put(@RequestBody Product productToUpdate) throws IdNotFoundException{

        //check if the product is existing in db
        //if not exist, getById will throw exception
        getById(productToUpdate.getId());

        return theProductService.put(productToUpdate);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteById(@PathVariable Long id) throws IdNotFoundException{
        //check if product is existing or throw IdNotFoundException
        getById(id);

        theProductService.deleteById(id);
        return "deletion succeed!!!!";
    }
}

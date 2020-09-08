package com.will.order_management_api.repository;

import com.will.order_management_api.entities.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends CrudRepository<Product, Long> {
    @Override
    Product save(Product aProduct);

    @Override
    List<Product> findAll();

    @Query(nativeQuery = true,
            value = ("SELECT * FROM product p " +
                    "WHERE p.name = :aName"))
    Product findByName(@Param(value = "aName") String aName);
}

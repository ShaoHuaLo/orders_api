package com.will.order_management_api.repository;

import com.will.order_management_api.entities.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface OrderRepo extends CrudRepository<Order, Long> {

    @Override
    Order save(Order aOrder);


    @Override
    List<Order> findAll();


    @Query(nativeQuery = true,
            value = ("SELECT * FROM orders AS o " +
                    "WHERE o.orders_date = :date "))
    List<Order> findByDate(@Param(value = "date") Date date);


    @Query(nativeQuery = true,
            value = ("SELECT * FROM orders AS o " +
                    "WHERE o.orders_date BETWEEN :startDay AND :endDay "))
    List<Order> findByDateRange(@Param(value = "startDay") Date startDay, @Param(value = "endDay") Date endDay);

}

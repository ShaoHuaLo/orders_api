package com.will.order_management_api.service;

import com.will.order_management_api.entities.Order;
import com.will.order_management_api.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * OrderService class provides a service layer to our API and it interact between order DAO layer and our order controller.
 * @author Will
 */

@Service
public class OrderService {

    @Autowired
    private OrderRepo theOrderRepo;


    //call when post mapping, create a new order in database
    public Order post(Order theOrder) {
        return theOrderRepo.save(theOrder);
    }


    //below provide several searching functionality

    //search all
    public List<Order> getAll() {
        return theOrderRepo.findAll();
    }
    //search by single order_id
    public Optional<Order> getById(long id) {
        return theOrderRepo.findById(id);
    }

    //search by specific one date
    public List<Order> getByDate(Date date) {
        return theOrderRepo.findByDate(date);
    }

    //search by date range
    public List<Order> getByDateRange(Date start, Date end) {
        return theOrderRepo.findByDateRange(start, end);
    }

    public void delete(Order orderToBeDelete) {
        theOrderRepo.delete(orderToBeDelete);
    }
}

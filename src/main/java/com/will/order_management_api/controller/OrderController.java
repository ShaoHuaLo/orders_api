package com.will.order_management_api.controller;

import com.will.order_management_api.dto.JsonDto;
import com.will.order_management_api.entities.Order;
import com.will.order_management_api.entities.Product;
import com.will.order_management_api.exception.InvalidItemException;
import com.will.order_management_api.exception.ResultNotFoundException;
import com.will.order_management_api.repository.OrderRepo;
import com.will.order_management_api.repository.ProductRepo;
import com.will.order_management_api.service.OrderService;
import com.will.order_management_api.service.ProductService;
import org.hibernate.boot.model.relational.SimpleAuxiliaryDatabaseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {


    @Autowired
    OrderService theOrderService;

    @Autowired
    ProductService theProductService;

    @Autowired
    JsonDto dto;

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Order createNewOrder(@RequestBody JsonDto dto) throws ParseException {

        //get data from dto
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getDateString());
        Map<String, Integer> theItems = dto.getItems();

        //create a new order and set attributes based on data retrieved above
        Order orderToCreate = newOrder(date, theItems);

        //save the order created, and corresponding map table will be automatically cascade saved as well
        return theOrderService.post(orderToCreate);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getAll() {
        return theOrderService.getAll();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Order getById(@PathVariable long id) {
        Optional<Order> temp = theOrderService.getById(id);
        if(!temp.isPresent()) {
            throw new ResultNotFoundException("there is no correspoding order record with this id in database");
        }

        return temp.get();
    }


    //client can either use specific day to query orders or use range of date to query
    //if former argument is given, this api will apply thie specific day search/query.
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getByDate(@RequestParam(value = "date", required = false) String dateString,
                                 @RequestParam(value = "start", required = false) String startString,
                                 @RequestParam(value = "end", required = false) String endString)
            throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //if argument date is given, just use the argument to query specific day records
        //else if argument start && end are both given, use both these to query records by date range
        //else report a illegal argument exception
        if(dateString != null) {
            Date date = dateFormat.parse(dateString);
            return theOrderService.getByDate(date);
        } else if(startString != null && endString != null) {
            Date start = dateFormat.parse(startString);
            Date end = dateFormat.parse(endString);
            return theOrderService.getByDateRange(start, end);
        } else {
            throw new IllegalArgumentException("!!!!!!Wrong query param!!!!!!" +
                                                "you can either specify one specific day " +
                                                "or give two date to query by date interval");
        }
    }

    //
    @PutMapping
    public Order put(@RequestBody JsonDto dto) throws ParseException {
        //check if there exist this order (by id provided through json)
        Order originalOrder = getById(dto.getId());

        //initialize properties to be add in our new orders later on
        Date newDate = null;
        Map<String, Integer> newItems = null;

        //if requestbody doesn't conatain date update info, use the original one
        newDate = (dto.getDateString() == null)?
                originalOrder.getDate() : new SimpleDateFormat("yyyy-MM-dd").parse(dto.getDateString());

        //like date, if user doesn't specifiy new shopping list, keep original
        newItems = (dto.getItems() == null)?
                originalOrder.getItems() : dto.getItems();

        //based on info above, create a new order object and assign to the original id
        Order temp = newOrder(newDate, newItems);
        temp.setId(dto.getId());

        //finally save it in our database
        return theOrderService.post(temp);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        //check whether there is corresponding entity with this id
        //if not, it will throw an exception
        Order orderToDelete = getById(id);

        theOrderService.delete(orderToDelete);
    }




    //helper method
    //give date && a shopping list as argument and produce a order object
    private Order newOrder(Date date, Map<String, Integer> theItems) {

        Order orderToCreate = new Order();
        int subTotal = 0;
        orderToCreate.setDate(date);

        //loop over every item in the requestbody
        for(Map.Entry<String, Integer> entry : theItems.entrySet()) {
            String itemName = entry.getKey();
            int quantityToBuy = entry.getValue();

            //check if this itemName is valid, whether or not exist in our db
            Optional<Product> theProduct = theProductService.getByName(entry.getKey());

            if(!theProduct.isPresent()) {
                throw new InvalidItemException("there is no such item: " + itemName + "!!!");
            }

            subTotal += theProduct.get().getPrice() * quantityToBuy;
            orderToCreate.addItems(itemName, quantityToBuy);
        }

        orderToCreate.setSubtotal(subTotal);

        return orderToCreate;
    }


}

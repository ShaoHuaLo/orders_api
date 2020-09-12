package com.will.order_management_api.controller;

import com.will.order_management_api.dto.JsonDto;
import com.will.order_management_api.dto.Response;
import com.will.order_management_api.entities.Order;
import com.will.order_management_api.entities.Product;
import com.will.order_management_api.exception.InvalidDateFormatException;
import com.will.order_management_api.exception.InvalidItemNameException;
import com.will.order_management_api.exception.IdNotFoundException;
import com.will.order_management_api.service.OrderService;
import com.will.order_management_api.service.ProductService;
import com.will.order_management_api.util.HelperMethods;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.GenericValidator;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * this OrderController class provides several endpoints and different functionalities needed
 * for restaurants' ordering system, including adding a new order, updating/deleting existing orders,
 * and searching functionality. For searching, we provide searching by order_id, ordering date, and
 * date-range search, and it will show the purchase details  including item_name, item_quantity and subtotals.
 * @author Will
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {


    @Autowired
    @NonNull OrderService theOrderService;

    @Autowired
    @NonNull ProductService theProductService;

    @Autowired
    @NonNull JsonDto dto;

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Order createNewOrder(@RequestBody JsonDto dto) {

        //get data from dto
        Optional<Date> dateOptional = HelperMethods.StringToDate(dto.getDateString());
        Map<String, Integer> theItems = dto.getItems();

        if(dateOptional.isEmpty()) {
            throw new InvalidDateFormatException("Input date format is not valid!!!!!!");
        }

        //create a new order and set attributes based on data retrieved above
        Order orderToCreate = newOrder(dateOptional.get(), theItems);

        //save the order created, and corresponding map table will be automatically cascade saved as well
        return theOrderService.post(orderToCreate);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response getAll() {
        return Response.of(theOrderService.getAll());

    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Order getById(@PathVariable long id) {
        Optional<Order> temp = theOrderService.getById(id);
        if(!temp.isPresent()) {
            throw new IdNotFoundException("there is no corresponding order record with this id in database");
        }

        return temp.get();
    }


    //client can either use specific day to query orders or use range of date to query
    //if former argument is given, this api will apply thie specific day search/query.


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response getByDate(@RequestParam(value = "date", required = false) String dateString,
                                 @RequestParam(value = "start", required = false) String startString,
                                 @RequestParam(value = "end", required = false) String endString) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<Order> result = null;

        //if argument date is given, just use the argument to query specific day records
        //else if argument start && end are both given, use both these to query records by date range
        //else report a illegal argument exception
        if(dateString != null) {
            if(HelperMethods.isValidDateString(dateString)){
                Date date = HelperMethods.StringToDate(dateString).get();
                result =  theOrderService.getByDate(date);
            } else {
                throw new InvalidDateFormatException("Input date format is not valid!!!!!!");
            }

        } else if(startString != null && endString != null) {
            if(HelperMethods.isValidDateString(startString) && HelperMethods.isValidDateString(endString)){
                Date start = HelperMethods.StringToDate(startString).get();
                Date end = HelperMethods.StringToDate(endString).get();
                result = theOrderService.getByDateRange(start, end);
            } else {
                throw new InvalidDateFormatException("Input date format is not valid!!!!!!");
            }

        } else {
            throw new IllegalArgumentException("!!!!!!Wrong query param!!!!!!" +
                    "you can either specify one specific day " +
                    "or give two date to query by date interval");
        }
        return Response.of(result);
    }


    @PutMapping
    public Order put(@RequestBody JsonDto dto) {
        //check if there exist this order
        Order originalOrder = getById(dto.getId());

        //initialize properties to be add in our new orders later on
        Date newDate = null;
        Map<String, Integer> newItems = null;

        Optional<Date> temp = HelperMethods.StringToDate(dto.getDateString());

        if(dto.getDateString() != null && temp.isEmpty()) {
            throw new InvalidDateFormatException("Input date format is not valid!!!!!!");
        }

        //if requestbody doesn't conatain date update info, use the original one
        newDate = (temp.isPresent())? temp.get() : originalOrder.getDate();

        //like date, if user doesn't specifiy new shopping list, keep original
        newItems = (dto.getItems() == null)?
                originalOrder.getItems() : dto.getItems();

        //based on info above, create a new order object and assign to the original id
        Order result = newOrder(newDate, newItems);
        result.setId(dto.getId());

        //finally save it in our database
        return theOrderService.post(result);
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
    @VisibleForTesting
    protected Order newOrder(Date date, Map<String, Integer> theItems) {

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
                throw new InvalidItemNameException("there is no such item: " + itemName + "!!!");
            }

            subTotal += theProduct.get().getPrice() * quantityToBuy;
            orderToCreate.addItems(itemName, quantityToBuy);
        }

        orderToCreate.setSubtotal(subTotal);

        return orderToCreate;
    }


}

package com.will.order_management_api.dto;

import com.will.order_management_api.entities.Order;
import lombok.*;

import java.util.List;

/**
 *
 * Response class wrap our SQL manipulation results into an object and add some other information associated with
 * the SQL results, if our SQL return a list of orders, Response will include total number of orders, total money value of
 * these orders as well as the list of orders.
 * @author Will
 */



@Data
@Builder
@RequiredArgsConstructor
@NoArgsConstructor
public class Response {

    @NonNull private int numberOfTransaction;

    @NonNull private int totalPrice;

    @NonNull private List<Order> orders;

    public static Response of(final List<Order> theOrders) {
        return Response.builder()
                .orders(theOrders)
                .numberOfTransaction(theOrders.size())
                .totalPrice(theOrders.stream().mapToInt(Order::getSubtotal).sum())
                .build();
//        Response response = new Response();
//        response.setOrders(theOrders);
//        response.setNumberOfTransaction(theOrders.size());
//
//        int total = theOrders.stream().mapToInt(Order::getSubtotal).sum();
//        response.setTotalPrice(total);

//        return response;
    }
}

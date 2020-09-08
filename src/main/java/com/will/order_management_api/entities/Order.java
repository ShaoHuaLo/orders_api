package com.will.order_management_api.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "orders_date")
    private Date date;

    @Column(name = "subtotal")
    private int subtotal;

    @ElementCollection
    @CollectionTable(name = "orders_details",
                    joinColumns = @JoinColumn(name = "orders_id"))
    @MapKeyColumn(name = "item")
    @Column(name = "quantity")
    private Map<String, Integer> items = new HashMap<>();

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    public void addItems(String key, Integer value){
        items.put(key, value);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                ", subtotal=" + subtotal +
                ", items=" + items +
                '}';
    }
}

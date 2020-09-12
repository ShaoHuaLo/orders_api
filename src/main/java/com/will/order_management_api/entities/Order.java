package com.will.order_management_api.entities;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Order class serves as the basic entity unit for us to interact with database.
 * Attributes include order_id, ordering_date, subtotal, ordered_items' name and quantity
 * @author Will
 */


@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "orders_date")
    @Temporal(value = TemporalType.DATE)
    @NonNull private Date date;

    @Column(name = "subtotal")
    private int subtotal;

    @ElementCollection
    @CollectionTable(name = "orders_details",
                    joinColumns = @JoinColumn(name = "orders_id"))
    @MapKeyColumn(name = "item")
    @Column(name = "quantity")
    @Nullable
    private Map<String, Integer> items = new HashMap<>();

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

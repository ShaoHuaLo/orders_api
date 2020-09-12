package com.will.order_management_api.entities;

import lombok.*;

import javax.persistence.*;

/**
 * Product class serves as the basic entity unit for us to interact with database.
 * Attributes include product_id, product_name ,product_price
 * @author Will
 */

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull private long id;

    @Column(name = "name")
    @NonNull private String name;

    @Column(name = "price")
    @NonNull private int price;

    @Override
    public String toString() {
        return "product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

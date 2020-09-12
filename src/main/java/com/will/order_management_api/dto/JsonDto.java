package com.will.order_management_api.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * JsonDto class serve as a Data Transfer Object to transfer the data entered by clients
 * @autor Will
 */



@Component
@Scope(value = "prototype")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonDto {

    private int id;

    private String dateString;

    private Map<String, Integer> items = new HashMap<>();


    @JsonAnyGetter
    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    @JsonAnySetter
    public void addItems(String productName, Integer quantity) {
        items.put(productName, quantity);
    }


}

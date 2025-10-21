package com.fiordelisi.fiordelisiproduct.dto;

import com.fiordelisi.fiordelisiproduct.entity.Customer;
import com.fiordelisi.fiordelisiproduct.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Customer customer;
    private List<OrderItem> items;
    private double subtotal;
    private double shipping;
    private double total;
    private String status;
}

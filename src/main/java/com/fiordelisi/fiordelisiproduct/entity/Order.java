package com.fiordelisi.fiordelisiproduct.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Document(collection = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private String id;
    private List<OrderItem> items;
    private double total;
    private double subtotal;
    private Customer customer;
    private double shipping;
    private String status;
    private LocalDateTime createdAt = LocalDateTime.now();
}

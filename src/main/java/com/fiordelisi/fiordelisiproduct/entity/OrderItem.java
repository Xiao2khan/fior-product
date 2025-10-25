package com.fiordelisi.fiordelisiproduct.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    private String productName;
    private String productId;
    private String variantId;
    private String variantName;
    private Integer size;
    private Integer price;
    private String unit;
    private Integer quantity;
    private String image;
}

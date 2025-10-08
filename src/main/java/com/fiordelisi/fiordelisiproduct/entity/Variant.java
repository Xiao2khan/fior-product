package com.fiordelisi.fiordelisiproduct.entity;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variant {
    private String id;
    private String productId;
    private Integer sizeGram;
    private Integer price;
    private Integer quantity;
}
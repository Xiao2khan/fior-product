package com.fiordelisi.fiordelisiproduct.entity;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variant {
    private String id;
    private String productId;
    private Integer size;
    private Integer price;
    private boolean instock;
    private String unit;
}
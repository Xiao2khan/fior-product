package com.fiordelisi.fiordelisiproduct.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nutrition {
    private Double carbohydrate;
    private Double protein;
    private Double fat;
    private Double fiber;
    private Double salt;
    private Integer energy;
}

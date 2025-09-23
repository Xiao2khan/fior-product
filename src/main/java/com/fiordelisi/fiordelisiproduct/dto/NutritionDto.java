package com.fiordelisi.fiordelisiproduct.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionDto {
    @PositiveOrZero(message = "Carbohydrate must not be negative")
    private Double carbohydrate;
    @PositiveOrZero(message = "Protein must not be negative")
    private Double protein;
    @PositiveOrZero(message = "Fat must not be negative")
    private Double fat;
    @PositiveOrZero(message = "Fiber must not be negative")
    private Double fiber;
    @PositiveOrZero(message = "Salt must not be negative")
    private Double salt;
    @Min(value = 0, message = "Energy must not be negative")
    private Integer energy;
}



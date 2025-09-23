package com.fiordelisi.fiordelisiproduct.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantDto {
    private String id;

    @NotNull(message = "Please enter size (grams)")
    private Integer sizeGram;

    @NotNull(message = "Please enter price (VND)")
    @Min(value = 0, message = "Price must not be negative")
    private Integer priceCents;

    @NotNull(message = "Please enter quantity")
    @Min(value = 0, message = "Quantity must not be negative")
    private Integer quantity;
}



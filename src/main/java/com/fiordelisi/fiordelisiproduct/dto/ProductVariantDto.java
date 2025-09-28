package com.fiordelisi.fiordelisiproduct.dto;

import com.fiordelisi.fiordelisiproduct.entity.Variant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantDto {
    private String productId;
    private String productName;
    private String productImage;
    private Variant variant;
}
package com.fiordelisi.fiordelisiproduct.dto;

import com.fiordelisi.fiordelisiproduct.entity.Product;
import com.fiordelisi.fiordelisiproduct.entity.Variant;
import com.fiordelisi.fiordelisiproduct.entity.response.TranslationEntry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.fiordelisi.fiordelisiproduct.dto.CategoryDto.getLocalizedValue;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private String id;

    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Product name must be at most 150 characters")
    private String name;
    @Size(max = 2000, message = "Description must be at most 2000 characters")
    private String description;
    @Size(max = 1000, message = "Image URL must be at most 1000 characters")
    private List<String> images;
    @NotNull(message = "Please select a category")
    private List<String> categoryId;
    @Builder.Default
    private Integer defaultPrice = 0;
    private List<Variant> variants;
    private List<String> removedImages = new ArrayList<>();
    private List<String> removedVariantIds;
    private String language;
    private List<TranslationEntry> translations = new ArrayList<>();
    private boolean hasOtherTranslations;

    public static ProductDto toDto(Product product, String languageCode) {

        String name = getLocalizedValue(product.getName(), languageCode);
        String description = getLocalizedValue(product.getDescription(), languageCode);

//        if (name == null || name.isBlank()) {
//            name = getLocalizedValue(product.getName(), "vi");
//        }
//        if (description == null || description.isBlank()) {
//            description = getLocalizedValue(product.getDescription(), "vi");
//        }

        boolean hasOtherTranslations = product.getName() != null &&
                product.getName().stream()
                        .anyMatch(t -> !t.getLanguage().equalsIgnoreCase(languageCode));

        return ProductDto.builder()
                .id(product.getId())
                .name(name)
                .description(description)
                .images(product.getImages() != null ? new ArrayList<>(product.getImages()) : new ArrayList<>())
                .categoryId(product.getCategoryId() != null ? new ArrayList<>(product.getCategoryId()) : new ArrayList<>())
                .defaultPrice(product.getDefaultPrice() != null ? product.getDefaultPrice() : 0)
                .variants(product.getVariants() != null ? new ArrayList<>(product.getVariants()) : new ArrayList<>())
                .language(languageCode)
                .hasOtherTranslations(hasOtherTranslations)
                .translations(new ArrayList<>())
                .build();
    }



//    public static String getLocalizedValue(Set<LocalizedText> localizedTexts, String languageCode) {
//        if (localizedTexts == null || languageCode == null) return null;
//
//        return localizedTexts.stream()
//                .filter(t -> languageCode.equalsIgnoreCase(t.getLanguage()))
//                .map(LocalizedText::getValue)
//                .findFirst()
//                .or(() -> localizedTexts.stream()
//                        .filter(t -> Language.EN.getCode().equalsIgnoreCase(t.getLanguage()))
//                        .map(LocalizedText::getValue)
//                        .findFirst()
//                )
//                .orElse(null);
//    }
}
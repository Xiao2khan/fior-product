package com.fiordelisi.fiordelisiproduct.entity;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    private String id;
    private Set<@Valid LocalizedText> name;
    private Set<@Valid LocalizedText> description;
    private List<String> images;
    private List<String> categoryId;
    private List<Variant> variants;
    @Builder.Default
    private Integer defaultPrice = 0;

    public String getNameByLanguage(String lang) {
        return name.stream()
                .filter(n -> n.getLanguage().equalsIgnoreCase(lang))
                .map(LocalizedText::getValue)
                .findFirst()
                .orElse(null);
    }

    public String getDescriptionByLanguage(String lang) {
        return description.stream()
                .filter(n -> n.getLanguage().equalsIgnoreCase(lang))
                .map(LocalizedText::getValue)
                .findFirst()
                .orElse(null);
    }
}
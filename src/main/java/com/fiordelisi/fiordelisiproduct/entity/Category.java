package com.fiordelisi.fiordelisiproduct.entity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    private String id;

    @NotEmpty(message = "Name translations are required")
    private Set<@Valid LocalizedText> name;

    @NotEmpty(message = "Description translations are required")
    private Set<@Valid LocalizedText> description;

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

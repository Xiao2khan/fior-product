package com.fiordelisi.fiordelisiproduct.dto;

import com.fiordelisi.fiordelisiproduct.constant.Language;
import com.fiordelisi.fiordelisiproduct.entity.Category;
import com.fiordelisi.fiordelisiproduct.entity.LocalizedText;
import com.fiordelisi.fiordelisiproduct.entity.response.TranslationEntry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private String id;
    private String name;
    private String description;
    private String language;


    private List<TranslationEntry> translations = new ArrayList<>();
    private boolean hasOtherTranslations;

    public static CategoryDto toDto(Category category, String languageCode) {
        String name = getLocalizedValue(category.getName(), languageCode);
        String description = getLocalizedValue(category.getDescription(), languageCode);
        boolean hasOtherTranslations = category.getName().stream()
                .anyMatch(t -> !t.getLanguage().equalsIgnoreCase(languageCode));

        return new CategoryDto(
                category.getId(),
                name,
                description,
                languageCode,
                new ArrayList<>(),
                hasOtherTranslations
        );
    }

    public static String getLocalizedValue(Set<LocalizedText> localizedTexts, String languageCode) {
        if (localizedTexts == null || languageCode == null) return null;

        return localizedTexts.stream()
                .filter(t -> languageCode.equalsIgnoreCase(t.getLanguage()))
                .map(LocalizedText::getValue)
                .findFirst()
                .or(() -> localizedTexts.stream()
                        .filter(t -> Language.VI.getCode().equalsIgnoreCase(t.getLanguage()))
                        .map(LocalizedText::getValue)
                        .findFirst()
                )
                .orElse(null);
    }
}

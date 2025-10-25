package com.fiordelisi.fiordelisiproduct.dto;

import com.fiordelisi.fiordelisiproduct.constant.Language;

public record LanguageDto(String code, String displayName) {
    public static LanguageDto from(Language lang) {
        return new LanguageDto(lang.getCode(), lang.getDisplayName());
    }
}
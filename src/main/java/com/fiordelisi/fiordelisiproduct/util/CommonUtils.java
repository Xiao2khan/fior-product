package com.fiordelisi.fiordelisiproduct.util;

import com.fiordelisi.fiordelisiproduct.constant.Language;

public class CommonUtils {
    public static Language getLanguageFromParam(String lang) {
        if (lang == null || lang.isEmpty()) {
            return Language.VI;
        }
        try {
            return switch (lang.toLowerCase()) {
                case "en" -> Language.EN;
                case "vi" -> Language.VI;
                default -> Language.VI;
            };
        } catch (Exception e) {
            return Language.VI;
        }
    }
}

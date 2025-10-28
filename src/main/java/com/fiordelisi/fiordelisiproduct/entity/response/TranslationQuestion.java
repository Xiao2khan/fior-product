package com.fiordelisi.fiordelisiproduct.entity.response;

import lombok.Data;

@Data
public class TranslationQuestion {
    private String language;
    private String title;
    private String question;
    private String answer;
}

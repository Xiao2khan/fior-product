package com.fiordelisi.fiordelisiproduct.entity.response;

import lombok.Data;

@Data
public class TranslationEntry {
    private String language;
    private String name;
    private String description;
}

package com.fiordelisi.fiordelisiproduct.constant;

import lombok.Getter;
@Getter
public enum Language {
    EN("en", "English"),
    VI("vi", "VietNames"),;

    private final String code;
    private final String displayName;

    Language(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
}

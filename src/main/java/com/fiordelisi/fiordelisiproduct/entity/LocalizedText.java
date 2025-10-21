package com.fiordelisi.fiordelisiproduct.entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedText {
    @NotBlank(message = "Language code is required")
    @Size(min = 2, max = 5, message = "Language code must be 2–5 characters")
    private String language;

    @NotBlank(message = "Text value is required")
    @Size(max = 500, message = "Text value cannot exceed 500 characters")
    private String value;
}

package com.fiordelisi.fiordelisiproduct.entity;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    private String id;
    private Set<@Valid LocalizedText> title;
    private Set<@Valid LocalizedText>  question;
    private Set<@Valid LocalizedText>  answer;

    public String getTitleByLanguage(String lang) {
        return title.stream()
                .filter(n -> n.getLanguage().equalsIgnoreCase(lang))
                .map(LocalizedText::getValue)
                .findFirst()
                .orElse(null);
    }

    public String getQuestionByLanguage(String lang) {
        return question.stream()
                .filter(n -> n.getLanguage().equalsIgnoreCase(lang))
                .map(LocalizedText::getValue)
                .findFirst()
                .orElse(null);
    }

    public String getAnswerByLanguage(String lang) {
        return answer.stream()
                .filter(n -> n.getLanguage().equalsIgnoreCase(lang))
                .map(LocalizedText::getValue)
                .findFirst()
                .orElse(null);
    }
}

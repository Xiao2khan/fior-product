package com.fiordelisi.fiordelisiproduct.dto;

import com.fiordelisi.fiordelisiproduct.entity.Question;
import com.fiordelisi.fiordelisiproduct.entity.response.TranslationQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import static com.fiordelisi.fiordelisiproduct.dto.CategoryDto.getLocalizedValue;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {
    @Id
    private String id;
    private String title;
    private String  question;
    private String  answer;
    private boolean hasOtherTranslations;
    private String language;
    private List<TranslationQuestion> translations = new ArrayList<>();

    public static QuestionDto toDto(Question ques, String languageCode) {

        String title = getLocalizedValue(ques.getTitle(), languageCode);
        String questionDesc = getLocalizedValue(ques.getQuestion(), languageCode);
        String answer = getLocalizedValue(ques.getAnswer(), languageCode);

        boolean hasOtherTranslations = ques.getTitle() != null &&
                ques.getTitle().stream()
                        .anyMatch(t -> !t.getLanguage().equalsIgnoreCase(languageCode));

        return QuestionDto.builder()
                .id(ques.getId())
                .title(title)
                .question(questionDesc)
                .language(languageCode)
                .answer(answer)
                .hasOtherTranslations(hasOtherTranslations)
                .translations(new ArrayList<>())
                .build();
    }
}

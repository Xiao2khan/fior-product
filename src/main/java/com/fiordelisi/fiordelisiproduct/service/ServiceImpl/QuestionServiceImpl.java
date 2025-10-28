package com.fiordelisi.fiordelisiproduct.service.ServiceImpl;

import com.fiordelisi.fiordelisiproduct.constant.Language;
import com.fiordelisi.fiordelisiproduct.dto.QuestionDto;
import com.fiordelisi.fiordelisiproduct.entity.LocalizedText;
import com.fiordelisi.fiordelisiproduct.entity.Question;
import com.fiordelisi.fiordelisiproduct.entity.response.TranslationQuestion;
import com.fiordelisi.fiordelisiproduct.repository.QuestionRepository;
import com.fiordelisi.fiordelisiproduct.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public Optional<Question> findById(String id) {
        return questionRepository.findById(id);
    }

    @Override
    public Page<Question> search(String query, String languageCode, Pageable pageable) {
        if (query == null || query.isBlank()) {
            return questionRepository.findAll(pageable);
        }
        return questionRepository.findByNameContainingIgnoreCaseAndLanguageWithFallback(query.trim(), languageCode, pageable);
    }

    @Override
    public Question create(Question question) {
        question.setId(null);
        return questionRepository.save(question);
    }

    @Override
    public Question update(String id, Question question) {
        question.setId(id);
        return questionRepository.save(question);
    }

    @Override
    public void deleteById(String id) {
        questionRepository.deleteById(id);
    }

    @Override
    public Question saveFromDto(QuestionDto dto) {
        String lang = dto.getLanguage() != null ? dto.getLanguage() : Language.VI.getCode();
        Set<LocalizedText> titles = new HashSet<>();
        Set<LocalizedText> questions = new HashSet<>();
        Set<LocalizedText> answers = new HashSet<>();
        titles.add(new LocalizedText(lang, dto.getTitle()));
        questions.add(new LocalizedText(lang, dto.getQuestion()));
        answers.add(new LocalizedText(lang, dto.getAnswer()));

        if (dto.getTranslations() != null) {
            for (TranslationQuestion t : dto.getTranslations()) {
                if (t.getLanguage() != null && t.getTitle() != null && !t.getTitle().isBlank()) {
                    titles.add(new LocalizedText(t.getLanguage(), t.getTitle()));
                    questions.add(new LocalizedText(t.getLanguage(), t.getQuestion()));
                    answers.add(new LocalizedText(t.getLanguage(), t.getAnswer()));
                }
            }
        }

        Question question = Question.builder()
                .id(dto.getId())
                .title(titles)
                .question(questions)
                .answer(answers)
                .build();

        if (dto.getId() == null || dto.getId().isBlank()) {
            return create(question);
        }
        return update(dto.getId(), question);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
}

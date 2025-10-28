package com.fiordelisi.fiordelisiproduct.service;

import com.fiordelisi.fiordelisiproduct.dto.QuestionDto;
import com.fiordelisi.fiordelisiproduct.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    Optional<Question> findById(String id);
    Page<Question> search(String category, String languageCode, Pageable pageable);
    Question create(Question question);
    Question update(String id, Question question);
    void deleteById(String id);
    Question saveFromDto(QuestionDto dto);
    List<Question> getAllQuestions();
}

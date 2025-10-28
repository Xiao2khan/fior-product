package com.fiordelisi.fiordelisiproduct.controller.admin;

import com.fiordelisi.fiordelisiproduct.constant.Language;
import com.fiordelisi.fiordelisiproduct.dto.LanguageDto;
import com.fiordelisi.fiordelisiproduct.dto.QuestionDto;
import com.fiordelisi.fiordelisiproduct.entity.LocalizedText;
import com.fiordelisi.fiordelisiproduct.entity.Question;
import com.fiordelisi.fiordelisiproduct.entity.response.TranslationQuestion;
import com.fiordelisi.fiordelisiproduct.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin/questions")
@RequiredArgsConstructor
public class QuestionAdminController {
    private final QuestionService questionService;


    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       Model model) {

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Order.asc("name")));
        Page<Question> result = questionService.search(q,"vi", pageable);

        List<QuestionDto> dtoList = result.getContent().stream()
                .map(que -> QuestionDto.toDto(que, Language.VI.getCode()))
                .toList();

        model.addAttribute("page", result);
        model.addAttribute("questions", dtoList);
        model.addAttribute("q", q);
        return "admin/question/list";
    }

    @GetMapping({"/create", "/edit/{id}"})
    public String showForm(@PathVariable(required = false) String id, Model model) {

        List<LanguageDto> languageOptions = Arrays.stream(Language.values())
                .map(LanguageDto::from)
                .toList();
        model.addAttribute("languages", languageOptions);

        if (id == null) {
            QuestionDto dto = new QuestionDto();
            model.addAttribute("question", dto);
            return "admin/question/form";
        }

        try {
            Optional<Question> questionOpt = questionService.findById(id);
            if (questionOpt.isEmpty()) {
                return "redirect:/admin/questions";
            }

            Question question = questionOpt.get();

            String defaultLang = Language.VI.getCode();
            String titleDefault = question.getTitleByLanguage(defaultLang);
            String questionDefault = question.getQuestionByLanguage(defaultLang);
            String answerDefault = question.getAnswerByLanguage(defaultLang);

            List<TranslationQuestion> translations = new ArrayList<>();
            for (LocalizedText nameEntry : question.getTitle()) {
                if (nameEntry.getLanguage().equalsIgnoreCase(defaultLang)) continue;
                TranslationQuestion entry = new TranslationQuestion();
                entry.setLanguage(nameEntry.getLanguage());
                entry.setTitle(nameEntry.getValue());

                question.getQuestion().stream()
                        .filter(desc -> desc.getLanguage().equalsIgnoreCase(nameEntry.getLanguage()))
                        .findFirst()
                        .ifPresent(desc -> entry.setQuestion(desc.getValue()));

                question.getAnswer().stream()
                        .filter(desc -> desc.getLanguage().equalsIgnoreCase(nameEntry.getLanguage()))
                        .findFirst()
                        .ifPresent(desc -> entry.setAnswer(desc.getValue()));

                translations.add(entry);
            }

            QuestionDto dto = QuestionDto.builder()
                    .id(question.getId())
                    .title(titleDefault)
                    .question(questionDefault)
                    .answer(answerDefault)
                    .translations(translations)
                    .build();

            model.addAttribute("question", dto);
            model.addAttribute("translations", translations);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/questions";
        }

        return "admin/question/form";
    }

    @PostMapping({"/create", "/edit/{id}"})
    public String submit(@PathVariable(required = false) String id,
                         @Valid @ModelAttribute("question") QuestionDto dto,
                         BindingResult bindingResult,
                         RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "admin/question/form";
        }
        dto.setId(id);
        questionService.saveFromDto(dto);
        ra.addFlashAttribute("success", id == null ? "Câu hỏi tạo thành công" : "Câu hỏi cập nhật thành công");
        return "redirect:/admin/questions";
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        questionService.deleteById(id);
        ra.addFlashAttribute("success", "Câu hỏi xóa thành công");
        return "redirect:/admin/questions";
    }
}

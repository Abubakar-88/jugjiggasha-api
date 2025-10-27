package com.jugjiggasha.controller;

import com.jugjiggasha.dto.question.AnswerRequestDTO;
import com.jugjiggasha.dto.question.QuestionRequestDTO;
import com.jugjiggasha.dto.question.QuestionResponseDTO;
import com.jugjiggasha.services.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public List<QuestionResponseDTO> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> getQuestionById(@PathVariable Long id) {
        Optional<QuestionResponseDTO> question = questionService.getQuestionById(id);
        return question.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public QuestionResponseDTO createQuestion(@Valid @RequestBody QuestionRequestDTO questionRequest) {
        return questionService.createQuestion(questionRequest);
    }
    @PostMapping("/admin/create")
    public ResponseEntity<QuestionResponseDTO> createQuestionWithAnswer(
            @Valid @RequestBody QuestionRequestDTO questionRequest) {
        try {
            QuestionResponseDTO createdQuestion = questionService.createQuestionWithAnswer(questionRequest);
            return ResponseEntity.ok(createdQuestion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> updateQuestion(@PathVariable Long id,
                                                              @Valid @RequestBody QuestionRequestDTO questionRequest) {
        QuestionResponseDTO updatedQuestion = questionService.updateQuestion(id, questionRequest);
        return updatedQuestion != null ? ResponseEntity.ok(updatedQuestion)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        boolean deleted = questionService.deleteQuestion(id);
        return deleted ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public List<QuestionResponseDTO> searchQuestions(@RequestParam String q) {
        return questionService.searchQuestions(q);
    }

    @GetMapping("/category/{categoryId}")
    public List<QuestionResponseDTO> getQuestionsByCategory(@PathVariable Long categoryId) {
        return questionService.getQuestionsByCategory(categoryId);
    }

    @GetMapping("/answered")
    public List<QuestionResponseDTO> getAnsweredQuestions() {
        return questionService.getAnsweredQuestions();
    }

    @GetMapping("/unanswered")
    public List<QuestionResponseDTO> getUnansweredQuestions() {
        return questionService.getUnansweredQuestions();
    }

    @PostMapping("/{id}/answer")
    public ResponseEntity<QuestionResponseDTO> answerQuestion(@PathVariable Long id,
                                                              @Valid @RequestBody AnswerRequestDTO answerRequest) {
        QuestionResponseDTO answeredQuestion = questionService.answerQuestion(id, answerRequest.getAnswer());
        return answeredQuestion != null ? ResponseEntity.ok(answeredQuestion)
                : ResponseEntity.notFound().build();
    }
}
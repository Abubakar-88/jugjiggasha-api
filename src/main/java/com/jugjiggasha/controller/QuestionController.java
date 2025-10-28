package com.jugjiggasha.controller;

import com.jugjiggasha.dto.PaginatedResponseDTO;
import com.jugjiggasha.dto.PaginationDTO;
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
@CrossOrigin(origins = "https://jugjiggasha.netlify.app/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    // Get all questions with pagination
    @GetMapping("/paginated")
    public ResponseEntity<PaginatedResponseDTO<QuestionResponseDTO>> getAllQuestionsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        PaginationDTO pagination = new PaginationDTO(page, size, sortBy, sortDirection);
        PaginatedResponseDTO<QuestionResponseDTO> response = questionService.getAllQuestions(pagination);
        return ResponseEntity.ok(response);
    }

    // Search questions with pagination
    @GetMapping("/search/paginated")
    public ResponseEntity<PaginatedResponseDTO<QuestionResponseDTO>> searchQuestionsPaginated(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        PaginationDTO pagination = new PaginationDTO(page, size, sortBy, sortDirection);
        PaginatedResponseDTO<QuestionResponseDTO> response = questionService.searchQuestions(q, pagination);
        return ResponseEntity.ok(response);
    }

    // Get questions by category with pagination
    @GetMapping("/category/{categoryId}/paginated")
    public ResponseEntity<PaginatedResponseDTO<QuestionResponseDTO>> getQuestionsByCategoryPaginated(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        PaginationDTO pagination = new PaginationDTO(page, size, sortBy, sortDirection);
        PaginatedResponseDTO<QuestionResponseDTO> response = questionService.getQuestionsByCategory(categoryId, pagination);
        return ResponseEntity.ok(response);
    }

    // Get answered questions with pagination
    @GetMapping("/answered/paginated")
    public ResponseEntity<PaginatedResponseDTO<QuestionResponseDTO>> getAnsweredQuestionsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        PaginationDTO pagination = new PaginationDTO(page, size, sortBy, sortDirection);
        PaginatedResponseDTO<QuestionResponseDTO> response = questionService.getAnsweredQuestions(pagination);
        return ResponseEntity.ok(response);
    }

    // Get unanswered questions with pagination
    @GetMapping("/unanswered/paginated")
    public ResponseEntity<PaginatedResponseDTO<QuestionResponseDTO>> getUnansweredQuestionsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        PaginationDTO pagination = new PaginationDTO(page, size, sortBy, sortDirection);
        PaginatedResponseDTO<QuestionResponseDTO> response = questionService.getUnansweredQuestions(pagination);
        return ResponseEntity.ok(response);
    }
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
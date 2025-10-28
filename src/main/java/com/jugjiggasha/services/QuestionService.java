package com.jugjiggasha.services;

import com.jugjiggasha.dto.PaginatedResponseDTO;
import com.jugjiggasha.dto.PaginationDTO;
import com.jugjiggasha.dto.category.CategoryDTO;
import com.jugjiggasha.dto.question.QuestionRequestDTO;
import com.jugjiggasha.dto.question.QuestionResponseDTO;
import com.jugjiggasha.entity.Category;
import com.jugjiggasha.entity.Question;
import com.jugjiggasha.repository.CategoryRepository;
import com.jugjiggasha.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    // Get all questions with pagination
    public PaginatedResponseDTO<QuestionResponseDTO> getAllQuestions(PaginationDTO pagination) {
        Pageable pageable = createPageable(pagination);
        Page<Question> questionPage = questionRepository.findAll(pageable);

        return convertToPaginatedResponse(questionPage, pagination);
    }

    // Search questions with pagination
    public PaginatedResponseDTO<QuestionResponseDTO> searchQuestions(String query, PaginationDTO pagination) {
        Pageable pageable = createPageable(pagination);
        Page<Question> questionPage = questionRepository.searchQuestions(query, pageable);

        return convertToPaginatedResponse(questionPage, pagination);
    }

    // Get questions by category with pagination
    public PaginatedResponseDTO<QuestionResponseDTO> getQuestionsByCategory(Long categoryId, PaginationDTO pagination) {
        Pageable pageable = createPageable(pagination);
        Page<Question> questionPage = questionRepository.findByCategoryId(categoryId, pageable);

        return convertToPaginatedResponse(questionPage, pagination);
    }

    // Get answered questions with pagination
    public PaginatedResponseDTO<QuestionResponseDTO> getAnsweredQuestions(PaginationDTO pagination) {
        Pageable pageable = createPageable(pagination);
        Page<Question> questionPage = questionRepository.findByIsAnsweredTrue(pageable);

        return convertToPaginatedResponse(questionPage, pagination);
    }

    // Get unanswered questions with pagination
    public PaginatedResponseDTO<QuestionResponseDTO> getUnansweredQuestions(PaginationDTO pagination) {
        Pageable pageable = createPageable(pagination);
        Page<Question> questionPage = questionRepository.findByIsAnsweredFalse(pageable);

        return convertToPaginatedResponse(questionPage, pagination);
    }

    // Helper method to create Pageable
    private Pageable createPageable(PaginationDTO pagination) {
        Sort.Direction direction = Sort.Direction.fromString(pagination.getSortDirection());
        Sort sort = Sort.by(direction, pagination.getSortBy());

        return PageRequest.of(
                pagination.getPage(),
                pagination.getSize(),
                sort
        );
    }

    // Helper method to convert Page to PaginatedResponseDTO
    private PaginatedResponseDTO<QuestionResponseDTO> convertToPaginatedResponse(
            Page<Question> questionPage, PaginationDTO pagination) {

        List<QuestionResponseDTO> content = questionPage.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(
                content,
                questionPage.getNumber() + 1, // Spring Data pages are 0-indexed
                questionPage.getTotalPages(),
                questionPage.getTotalElements(),
                pagination.getSize()
        );
    }
    public List<QuestionResponseDTO> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<QuestionResponseDTO> getQuestionById(Long id) {
        return questionRepository.findById(id)
                .map(this::convertToDTO);
    }

    public QuestionResponseDTO createQuestion(QuestionRequestDTO questionRequest) {
        Question question = convertToEntity(questionRequest);

        // If answer is provided in request, set it
        if (questionRequest.getAnswer() != null && !questionRequest.getAnswer().trim().isEmpty()) {
            question.setAnswer(questionRequest.getAnswer());
        }

        Question savedQuestion = questionRepository.save(question);
        return convertToDTO(savedQuestion);
    }

    public QuestionResponseDTO updateQuestion(Long id, QuestionRequestDTO questionRequest) {
        Optional<Question> optionalQuestion = questionRepository.findById(id);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            question.setTitle(questionRequest.getTitle());
            question.setDescription(questionRequest.getDescription());
            question.setUserEmail(questionRequest.getUserEmail());
            question.setUserPhone(questionRequest.getUserPhone());

            // Update category if provided
            if (questionRequest.getCategoryId() != null) {
                Optional<Category> category = categoryRepository.findById(questionRequest.getCategoryId());
                category.ifPresent(question::setCategory);
            }

            Question updatedQuestion = questionRepository.save(question);
            return convertToDTO(updatedQuestion);
        }
        return null;
    }

    public boolean deleteQuestion(Long id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<QuestionResponseDTO> searchQuestions(String query) {
        return questionRepository.searchQuestions(query)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<QuestionResponseDTO> getQuestionsByCategory(Long categoryId) {
        return questionRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<QuestionResponseDTO> getAnsweredQuestions() {
        return questionRepository.findByIsAnsweredTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<QuestionResponseDTO> getUnansweredQuestions() {
        return questionRepository.findByIsAnsweredFalse()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public QuestionResponseDTO answerQuestion(Long id, String answer) {
        Optional<Question> optionalQuestion = questionRepository.findById(id);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            question.setAnswer(answer);
            Question answeredQuestion = questionRepository.save(question);
            return convertToDTO(answeredQuestion);
        }
        return null;
    }
    // Add this method for admin to create question with answer
    public QuestionResponseDTO createQuestionWithAnswer(QuestionRequestDTO questionRequest) {
        Question question = convertToEntity(questionRequest);

        // Set answer if provided
        if (questionRequest.getAnswer() != null && !questionRequest.getAnswer().trim().isEmpty()) {
            question.createWithAnswer(questionRequest.getAnswer());
        }

        Question savedQuestion = questionRepository.save(question);
        return convertToDTO(savedQuestion);
    }
    // Update the convertToEntity method
    private Question convertToEntity(QuestionRequestDTO dto) {
        Question question = new Question();
        question.setTitle(dto.getTitle());
        question.setDescription(dto.getDescription());
        question.setUserEmail(dto.getUserEmail());
        question.setUserPhone(dto.getUserPhone());
        question.setUserName(dto.getUserName());

        // Set category if categoryId is provided
        if (dto.getCategoryId() != null) {
            Optional<Category> category = categoryRepository.findById(dto.getCategoryId());
            category.ifPresent(question::setCategory);
        }

        return question;
    }
    // Convert Entity to DTO
    private QuestionResponseDTO convertToDTO(Question question) {
        QuestionResponseDTO dto = new QuestionResponseDTO();
        dto.setId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setDescription(question.getDescription());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setIsAnswered(question.getIsAnswered());
        dto.setAnswer(question.getAnswer());
        dto.setAnsweredAt(question.getAnsweredAt());
        dto.setUserEmail(question.getUserEmail());
        dto.setUserPhone(question.getUserPhone());

        // Convert Category to CategoryDTO
        if (question.getCategory() != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(question.getCategory().getId());
            categoryDTO.setName(question.getCategory().getName());
            categoryDTO.setDescription(question.getCategory().getDescription());
            dto.setCategory(categoryDTO);
        }

        return dto;
    }


}
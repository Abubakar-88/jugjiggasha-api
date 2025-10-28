package com.jugjiggasha.repository;

import com.jugjiggasha.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Search questions with pagination
    @Query("SELECT q FROM Question q WHERE " +
            "LOWER(q.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(q.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Question> searchQuestions(@Param("query") String query, Pageable pageable);
    // Find questions by category with pagination
    Page<Question> findByCategoryId(Long categoryId, Pageable pageable);

    // Find answered questions with pagination
    Page<Question> findByIsAnsweredTrue(Pageable pageable);

    // Find unanswered questions with pagination
    Page<Question> findByIsAnsweredFalse(Pageable pageable);

    // Find all questions with pagination and sorting
    Page<Question> findAll(Pageable pageable);

    // Search questions by title or description
    @Query("SELECT q FROM Question q WHERE " +
            "LOWER(q.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(q.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Question> searchQuestions(@Param("query") String query);

    // Find questions by category ID
    List<Question> findByCategoryId(Long categoryId);

    // Find answered questions
    List<Question> findByIsAnsweredTrue();

    // Find unanswered questions
    List<Question> findByIsAnsweredFalse();

    // Count questions by category
    @Query("SELECT COUNT(q) FROM Question q WHERE q.category.id = :categoryId")
    Long countByCategoryId(@Param("categoryId") Long categoryId);
}

package com.jugjiggasha.repository;

import com.jugjiggasha.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

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

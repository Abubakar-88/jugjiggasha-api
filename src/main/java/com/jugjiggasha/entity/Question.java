package com.jugjiggasha.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Question title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Question description is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    private String userName;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_answered")
    private Boolean isAnswered = false;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_phone")
    private String userPhone;

    // Constructors
    public Question() {
        this.createdAt = LocalDateTime.now();
    }
    // Update the setAnswer method
    public void setAnswer(String answer) {
        this.answer = answer;
        this.isAnswered = answer != null && !answer.trim().isEmpty();
        if (this.isAnswered) {
            this.answeredAt = LocalDateTime.now();
        }
    }

    // Add this method for admin to create question with answer directly
    public void createWithAnswer(String answer) {
        setAnswer(answer);
        if (this.isAnswered) {
            this.answeredAt = LocalDateTime.now();
        }
    }


}
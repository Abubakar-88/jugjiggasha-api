package com.jugjiggasha.dto.question;

import com.jugjiggasha.dto.category.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDTO {
    private Long id;
    private String title;
    private String description;
    private CategoryDTO category;
    private LocalDateTime createdAt;
    private Boolean isAnswered;
    private String answer;
    private LocalDateTime answeredAt;
    private String userEmail;
    private String userPhone;
}
package com.jugjiggasha.dto.question;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDTO {

    @NotBlank(message = "Question title is required")
    private String title;

    @NotBlank(message = "Question description is required")
    private String description;

    private Long categoryId;

    @Email(message = "Invalid email format")
    private String userEmail;

    private String userPhone;


}
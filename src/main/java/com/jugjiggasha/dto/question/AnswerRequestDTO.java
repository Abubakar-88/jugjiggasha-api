package com.jugjiggasha.dto.question;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerRequestDTO {

    @NotBlank(message = "Answer cannot be empty")
    private String answer;
}

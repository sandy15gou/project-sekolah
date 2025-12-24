package com.sandy.project.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScoreCreateDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotBlank(message = "Student ID is required")
    private String studentId;
    
    @NotBlank(message = "Subject ID is required")
    private String subjectId;
    
    @NotBlank(message = "Score is required")
    @Pattern(regexp = "^([0-9]|[1-9][0-9]|100)$", message = "Score must be between 0-100")
    private String score;
    
    @NotBlank(message = "Semester is required")
    @Pattern(regexp = "^(1|2)$", message = "Semester must be 1 or 2")
    private String semester;
}


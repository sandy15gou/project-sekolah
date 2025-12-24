package com.sandy.project.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClassRequestDTO {
    private String className;        // e.g., "X IPA 1"
    private String gradeLevel;       // e.g., "10", "11", "12"
    private String homeroomTeacher;  // e.g., teacher's secureId or name
    private String academicYear;     // e.g., "2024/2025"
}
package com.sandy.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreResponseDTO {
    
    private String secureId;  // Sesuai dengan field di entity
    
    private String studentId;
    private String studentName;
    
    private String subjectId;
    private String subjectName;
    
    private Integer score;
    
    private String semester;
    
    // Tambahan info dari business logic
    private String grade;  // Dari method getGrade()
    
    private Boolean isPassing;  // Dari method isPassing()
}
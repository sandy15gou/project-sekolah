package com.sandy.project.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class StudentDetailDTO implements Serializable {
    
    private static final long serialVersionUID = 814361125017230499L;
    
    private String secureId;
    private String studentName;
    private String studentId;
    private Long studentBirthDate;
    private String studentGender;
    private String studentAddress;
}
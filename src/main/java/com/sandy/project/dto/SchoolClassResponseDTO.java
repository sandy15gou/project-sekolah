package com.sandy.project.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SchoolClassResponseDTO implements Serializable {
    
    private static final long serialVersionUID = 5168474109080743092L;
    private String secureId;
    private String className;
    private String academicYear;
    private String students;
    
}

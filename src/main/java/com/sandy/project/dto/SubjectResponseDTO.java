package com.sandy.project.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class SubjectResponseDTO implements Serializable {
    
    
    private static final long serialVersionUID = -7413377727108511546L;
    
    private String secureId;
    private String name;
}

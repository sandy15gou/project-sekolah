package com.sandy.project.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class ScheduleResponseDTO implements Serializable {
    
    
    private static final long serialVersionUID = -5471722046222974697L;
    private String secureId;
    private String className;
    private String subjectName;
    private String teacherName;
    private String day;
    private String time;
}

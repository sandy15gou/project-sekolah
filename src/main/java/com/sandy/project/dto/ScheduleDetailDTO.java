package com.sandy.project.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class ScheduleDetailDTO implements Serializable {
    
    
    private static final long serialVersionUID = 6915573265799690501L;
    private String secureId;
    private SchoolClassResponseDTO schoolClass;
    private SubjectResponseDTO subject;
    private TeacherResponseDTO teacher;
    private String day;
    private String startTime;
    private String endTime;
    private String semester;
}

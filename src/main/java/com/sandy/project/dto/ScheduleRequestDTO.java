package com.sandy.project.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class ScheduleRequestDTO implements Serializable {
    
    
    private static final long serialVersionUID = -3838618292446299275L;
    private String classId; // secureId kelas
        private String subjectId; // secureId mata pelajaran
        private String teacherId; // secureId guru
        private String day;
        private String startTime;
        private String endTime;
        private String semester;
    }
    


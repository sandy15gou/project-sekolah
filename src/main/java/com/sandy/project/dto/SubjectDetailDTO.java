package com.sandy.project.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubjectDetailDTO {
    private String secureId;
    private String name;
    private String description;
    private List<TeacherDetailDTO> eligibleTeachers;
    private List<String> eligibleTeacherIds;
}

package com.sandy.project.dto;

import lombok.Data;
import java.util.List;

@Data
public class SubjectDetailDTO {
    private String secureId;
    private String name;
    private String description;
    private List<TeacherDetailDTO> eligibleTeachers;
}

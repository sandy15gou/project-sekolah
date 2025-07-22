package com.sandy.project.dto;

import lombok.Data;
import java.util.List;

@Data
public class ClassDetailDTO {
    private String secureId;
    private String className;
    private String gradeLevel;
    private String academicYear;
    private TeacherDetailDTO homeroomTeacher;
    private List<StudentDetailDTO> students;
}
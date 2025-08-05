package com.sandy.project.dto;

import lombok.Data;
import java.util.List;

@Data
public class ClassDetailDTO {
    private String secureId;
    private String className;
    private String gradeLevel;
    private String academicYear;
    private Integer maxCapacity;
    private String description;
    
    // Homeroom Teacher
    private TeacherDetailDTO homeroomTeacher;
    
    // All Students in this class
    private List<StudentDetailDTO> students;
    
    // All Schedules for this class
    private List<ScheduleDetailDTO> schedules;
    
    // All Subjects taught in this class (derived from schedules)
    private List<SubjectDetailDTO> subjects;
    
    // All Teachers teaching in this class (derived from schedules)
    private List<TeacherDetailDTO> teachers;
    
    // Statistics
    private Integer currentStudentCount;
    private Integer totalSubjects;
    private Integer totalTeachers;
}
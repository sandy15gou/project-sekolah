package com.sandy.project.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "classes")
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "secure_id", nullable = false, unique = true)
    private String secureId;
    
    @Column(name = "class_name", nullable = false)
    private String className;
    
    @Column(name = "grade_level", nullable = false)
    private String gradeLevel;
    
    @Column(name = "academic_year", nullable = false)
    private String academicYear;
    
    @ManyToOne
    @JoinColumn(name = "homeroom_teacher_id")
    private Teacher homeroomTeacher;
    
    @ManyToMany
    @JoinTable(
            name = "class_students",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;
    
    @PrePersist
    public void prePersist() {
        this.secureId = UUID.randomUUID().toString();
    }
}
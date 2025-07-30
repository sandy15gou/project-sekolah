package com.sandy.project.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.UUID;

@Data
@DynamicUpdate
@SQLDelete(sql = "UPDATE classes SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
@Table(name = "classes")
public class Class extends AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "class_generator")
    @SequenceGenerator(name = "class_generator", sequenceName = "class_seq")
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
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private List<Student> students;
    
    @Column(name = "max_capacity", columnDefinition = "integer default 30")
    private Integer maxCapacity = 30;
    
    @Column(name = "description")
    private String description;
    
    @PrePersist
    public void prePersist() {
        this.secureId = UUID.randomUUID().toString();
    }
}
package com.sandy.project.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeroom_teacher_id")
    private Teacher homeroomTeacher;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "class_students",
        joinColumns = @JoinColumn(name = "class_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;
    
    @OneToMany(mappedBy = "clazz", fetch = FetchType.LAZY)
    private List<Schedule> schedules;
    
    @Column(name = "max_capacity", columnDefinition = "integer default 30")
    private Integer maxCapacity = 30;
    
    @Column(name = "description")
    private String description;
    
    @Override
    protected void onCreate() {
        super.onCreate();
        if (this.secureId == null) {
            this.secureId = UUID.randomUUID().toString();
        }
    }
}
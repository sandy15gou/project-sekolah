package com.sandy.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeroom_teacher_id")
    private Teacher homeroomTeacher;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "class_students", joinColumns = @JoinColumn(name = "class_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students;

    @JsonIgnore
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

    @Override
    public String toString() {
        return "Class{id=" + id + ", secureId=" + secureId
                + ", className=" + className + ", gradeLevel=" + gradeLevel + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Class other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
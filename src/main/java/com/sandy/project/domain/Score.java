package com.sandy.project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "scores")
@SQLDelete(sql = "UPDATE scores SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Score extends AbstractBaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "score_generator")
    @SequenceGenerator(name = "score_generator", sequenceName = "score_seq")
    private Long id;
    
    @Column(name = "secure_id", nullable = false, unique = true)
    private String secureId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
    
    @Column(name = "score", nullable = false)
    private Integer score;
    
    @Column(name = "semester", nullable = false, length = 1)
    private String semester;
    
    @Override
    protected void onCreate() {
        super.onCreate();
        if (this.secureId == null) {
            this.secureId = UUID.randomUUID().toString();
        }
    }



public void setScore(Integer score) {
    if (score == null || score < 0 || score > 100) {
        throw new IllegalArgumentException("Score must be between 0 and 100");
    }
    this.score = score;
}


public boolean isPassing() {
    return this.score != null && this.score >= 75;
}

// Business logic method untuk mendapatkan grade
public String getGrade() {
    if (this.score == null) return "N/A";
    if (this.score >= 90) return "A";
    if (this.score >= 75) return "B";
    if (this.score >= 60) return "C";
    if (this.score >= 50) return "D";
    return "E";
}
}
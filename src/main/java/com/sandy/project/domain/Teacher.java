package com.sandy.project.domain;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "teachers")
@SQLDelete(sql = "UPDATE teachers SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Teacher extends AbstractBaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teacher_generator")
    @SequenceGenerator(name = "teacher_generator", sequenceName = "teacher_seq")
    private Long id;
    
    @Column(name = "secure_id", nullable = false, unique = true)
    private String secureId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Column(name = "gender", length = 1, nullable = false)
    private String gender;
    
    @Column(name = "address")
    private String address;
    
    @PrePersist
    public void prePersist() {
        if (this.secureId == null) {
            this.secureId = UUID.randomUUID().toString();
        }
    }
}

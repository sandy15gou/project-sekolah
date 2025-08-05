package com.sandy.project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subjects")
public class Subject extends AbstractBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "secure_id", nullable = false, unique = true)
    private String secureId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    // Guru yang eligible untuk mengajar subject ini
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "subject_eligible_teachers",
        joinColumns = @JoinColumn(name = "subject_id"),
        inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<Teacher> eligibleTeachers;

    @PrePersist
    public void prePersist() {
        if (this.secureId == null) {
            this.secureId = UUID.randomUUID().toString();
        }
    }
}

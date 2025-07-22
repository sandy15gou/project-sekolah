package com.sandy.project.domain;

import java.util.List;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain class untuk merepresentasikan kelas di sekolah
 * Kelas ini menyimpan informasi tentang kelas seperti nama kelas, tingkat,
 * wali kelas, dan siswa yang tergabung dalam kelas tersebut
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "school_classes")
@SQLDelete(sql = "UPDATE school_classes SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class SchoolClass extends AbstractBaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "class_generator")
    @SequenceGenerator(name = "class_generator", sequenceName = "class_seq")
    private Long id;
    
    /**
     * Nama kelas (contoh: X IPA 1, XI IPS 2, XII Bahasa 1)
     */
    @Column(name = "class_name", nullable = false)
    private String className;
    
    /**
     * Tingkat kelas (contoh: 10, 11, 12 untuk SMA)
     */
    @Column(name = "grade_level", nullable = false)
    private String gradeLevel;
    
    /**
     * Tahun ajaran (contoh: 2024/2025)
     */
    @Column(name = "academic_year", nullable = false)
    private String academicYear;
    
    /**
     * Wali kelas - satu kelas memiliki satu wali kelas
     * Menggunakan ManyToOne karena satu guru bisa menjadi wali kelas beberapa kelas
     * di tahun ajaran yang berbeda
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeroom_teacher_id", referencedColumnName = "id")
    private Teacher homeroomTeacher;
    
    /**
     * Daftar siswa dalam kelas ini
     * Menggunakan ManyToMany karena siswa bisa pindah kelas dan kelas bisa memiliki banyak siswa
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private List<Student> students;
    
    /**
     * Kapasitas maksimal siswa dalam kelas
     */
    @Column(name = "max_capacity", columnDefinition = "integer default 30")
    private Integer maxCapacity = 30;
    
    /**
     * Deskripsi atau catatan tambahan tentang kelas
     */
    @Column(name = "description")
    private String description;
}
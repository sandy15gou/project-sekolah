package com.sandy.project.repository;

import com.sandy.project.domain.Class;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {
    
    // NOTE: findById(Long id) sudah otomatis ada dari JpaRepository
    // Digunakan untuk internal system (join table, cascade operations)
    
    // Custom query methods:
    
    // Untuk API/External - AMAN diexpose ke public
    Optional<Class> findBySecureId(String secureId);
    
    // Cari class berdasarkan wali kelas (menggunakan secureId teacher)
    List<Class> findByHomeroomTeacherSecureId(String teacherSecureId);
    
    // Cari class berdasarkan tahun ajaran
    List<Class> findByAcademicYear(String academicYear);
}
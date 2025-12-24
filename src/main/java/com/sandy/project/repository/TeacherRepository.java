package com.sandy.project.repository;

import com.sandy.project.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    // NOTE: findById(Long id) sudah otomatis ada dari JpaRepository
    // Digunakan untuk internal system (join table, cascade operations)
    
    // Custom query methods:
    
    // Untuk API/External - AMAN diexpose ke public
    Optional<Teacher> findBySecureId(String secureId);
}

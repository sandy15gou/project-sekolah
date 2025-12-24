package com.sandy.project.repository;
import java.util.List;
import java.util.Optional;

import com.sandy.project.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // NOTE: findById(Long id) sudah otomatis ada dari JpaRepository
    // Digunakan untuk internal system (join table, cascade operations)
    
    // Custom query methods:
    
    // Untuk API/External - AMAN diexpose ke public
    Optional<Student> findBySecureId(String id);
    
    // Cari students berdasarkan list secureId (untuk bulk operations)
    List<Student> findBySecureIdIn(List<String> studentIdList);
    
    // Cari student by ID dengan pengecekan soft delete manual
    Optional<Student> findByIdAndDeletedFalse(Long id);
    
    // Cari students berdasarkan nama (LIKE query)
    // Contoh usage: findByNameLike("%John%")
    List<Student> findByNameLike(String studentName);
}




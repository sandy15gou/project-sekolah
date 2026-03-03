package com.sandy.project.repository;
import java.util.List;
import java.util.Optional;

import com.sandy.project.domain.Student;
import com.sandy.project.dto.query.StudentQueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    
    // ========== JPA PROJECTION QUERIES - AVOID N+1 PROBLEM ==========
    
    /**
     * Find student by secureId dengan JPA Projection - 1 query JOIN
     * Mengambil Student + Class info sekaligus tanpa lazy loading
     */
    @Query("SELECT new com.sandy.project.dto.query.StudentQueryDTO(" +
            "s.secureId, s.name, s.birthDate, s.gender, s.address, " +
            "c.secureId, c.className, c.gradeLevel) " +
            "FROM Student s LEFT JOIN s.studentClass c " +
            "WHERE s.secureId = :secureId AND s.deleted = false")
    Optional<StudentQueryDTO> findStudentQueryDTOBySecureId(String secureId);
    
    /**
     * Fetch ALL students dengan JPA Projection - SOLUSI N+1 Problem
     *
     * Menggunakan 1 query JOIN untuk ambil Student + Class sekaligus
     * tanpa lazy loading terpisah
     */
    @Query("SELECT new com.sandy.project.dto.query.StudentQueryDTO(" +
            "s.secureId, s.name, s.birthDate, s.gender, s.address, " +
            "c.secureId, c.className, c.gradeLevel) " +
            "FROM Student s LEFT JOIN s.studentClass c " +
            "WHERE s.deleted = false")
    List<StudentQueryDTO> findAllStudentQueryDTO();
    
    /**
     * Fetch students dengan pagination dan JPA Projection
     * Gunakan ini untuk list view dengan banyak data
     */
    @Query("SELECT new com.sandy.project.dto.query.StudentQueryDTO(" +
            "s.secureId, s.name, s.birthDate, s.gender, s.address, " +
            "c.secureId, c.className, c.gradeLevel) " +
            "FROM Student s LEFT JOIN s.studentClass c " +
            "WHERE s.deleted = false")
    Page<StudentQueryDTO> findAllStudentQueryDTOPaged(Pageable pageable);
    
    // ========== ORIGINAL ENTITY METHODS ==========
    
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
    
    // ========== PAGINATION METHODS ==========
    
    // Pagination: Get all students dengan soft delete check
    Page<Student> findByDeletedFalse(Pageable pageable);
    
    // Pagination: Search by name dengan LIKE dan soft delete check
    // Contoh usage: findByNameContainingAndDeletedFalse("John", pageable)
    Page<Student> findByNameContainingAndDeletedFalse(String name, Pageable pageable);
}

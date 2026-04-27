package com.sandy.project.repository;

import com.sandy.project.domain.Teacher;
import com.sandy.project.dto.query.TeacherQueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long>,
                                          JpaSpecificationExecutor<Teacher> {
    
    // ========================================
    // JPA PROJECTION - Solusi N+1 Problem
    // ========================================
    
    /**
     * Fetch single Teacher dengan JPA Projection - Menghindari N+1
     * Query hanya ambil data yang dibutuhkan dalam 1 query
     */
    @Query("SELECT new com.sandy.project.dto.query.TeacherQueryDTO(" +
            "t.secureId, t.name, t.address, t.birthDate, t.gender) " +
            "FROM Teacher t " +
            "WHERE t.secureId = :secureId AND t.deleted = false")
    Optional<TeacherQueryDTO> findTeacherQueryDTOBySecureId(@Param("secureId") String secureId);
    
    /**
     * Fetch ALL Teachers dengan JPA Projection - SOLUSI N+1 Problem
     * Menggunakan 1 query untuk ambil semua Teacher tanpa lazy loading terpisah
     */
    @Query("SELECT new com.sandy.project.dto.query.TeacherQueryDTO(" +
            "t.secureId, t.name, t.address, t.birthDate, t.gender) " +
            "FROM Teacher t " +
            "WHERE t.deleted = false")
    List<TeacherQueryDTO> findAllTeacherQueryDTO();
    
    /**
     * Fetch Teachers dengan pagination dan JPA Projection
     */
    @Query("SELECT new com.sandy.project.dto.query.TeacherQueryDTO(" +
            "t.secureId, t.name, t.address, t.birthDate, t.gender) " +
            "FROM Teacher t " +
            "WHERE t.deleted = false")
    Page<TeacherQueryDTO> findAllTeacherQueryDTOPaged(Pageable pageable);
    
    /**
     * Search Teachers by name dengan JPA Projection
     */
    @Query("SELECT new com.sandy.project.dto.query.TeacherQueryDTO(" +
            "t.secureId, t.name, t.address, t.birthDate, t.gender) " +
            "FROM Teacher t " +
            "WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) AND t.deleted = false")
    List<TeacherQueryDTO> findTeacherQueryDTOByNameContaining(@Param("name") String name);
 
    // ========================================
    // BASIC QUERY - Cari berdasarkan secureId
    // ========================================
    Optional<Teacher> findBySecureId(String secureId);
    
    // ========== PAGINATION METHODS ==========
    
    // Pagination: Get all teachers dengan soft delete check
    Page<Teacher> findByDeletedFalse(Pageable pageable);
    
    // Pagination: Search by name dengan LIKE dan soft delete check
    Page<Teacher> findByNameContainingAndDeletedFalse(String name, Pageable pageable);
}

package com.sandy.project.repository;

import com.sandy.project.domain.Class;
import com.sandy.project.dto.query.ClassQueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository untuk Class entity
 * Extends JpaSpecificationExecutor untuk support dynamic query dengan Specification
 *
 * Tujuan: Menyediakan method untuk akses database Class
 * Method dari JpaSpecificationExecutor yang otomatis tersedia:
 * - Page<Class> findAll(Specification<Class> spec, Pageable pageable)
 */
public interface ClassRepository extends JpaRepository<Class, Long>, JpaSpecificationExecutor<Class> {
    
    // ========================================
    // JPA PROJECTION - Solusi N+1 Problem
    // ========================================
    
    /**
     * Fetch single Class dengan JPA Projection - Menghindari N+1
     *
     * Query JOIN Class + Teacher dalam 1 query
     * Tidak ada lazy loading terpisah untuk homeroomTeacher
     */
    @Query("SELECT new com.sandy.project.dto.query.ClassQueryDTO(" +
            "c.secureId, c.className, c.gradeLevel, c.academicYear, " +
            "c.maxCapacity, c.description, " +
            "h.secureId, h.name) " +
            "FROM Class c " +
            "LEFT JOIN c.homeroomTeacher h " +
            "WHERE c.secureId = :secureId AND c.deleted = false")
    Optional<ClassQueryDTO> findClassQueryDTOBySecureId(String secureId);
    
    /**
     * Fetch ALL Classes dengan JPA Projection - SOLUSI N+1 Problem
     *
     * Menggunakan 1 query JOIN untuk ambil Class + Teacher sekaligus
     * tanpa lazy loading terpisah
     */
    @Query("SELECT new com.sandy.project.dto.query.ClassQueryDTO(" +
            "c.secureId, c.className, c.gradeLevel, c.academicYear, " +
            "c.maxCapacity, c.description, " +
            "h.secureId, h.name) " +
            "FROM Class c " +
            "LEFT JOIN c.homeroomTeacher h " +
            "WHERE c.deleted = false")
    List<ClassQueryDTO> findAllClassQueryDTO();
    
    /**
     * Fetch Classes dengan pagination dan JPA Projection
     * Gunakan ini untuk list view dengan banyak data
     */
    @Query("SELECT new com.sandy.project.dto.query.ClassQueryDTO(" +
            "c.secureId, c.className, c.gradeLevel, c.academicYear, " +
            "c.maxCapacity, c.description, " +
            "h.secureId, h.name) " +
            "FROM Class c " +
            "LEFT JOIN c.homeroomTeacher h " +
            "WHERE c.deleted = false")
    Page<ClassQueryDTO> findAllClassQueryDTOPaged(Pageable pageable);
    
    // ========================================
    // BASIC ENTITY METHODS
    // ========================================
    
    Optional<Class> findBySecureId(String secureId);
    
    List<Class> findByHomeroomTeacherSecureId(String teacherSecureId);
   
    List<Class> findByAcademicYear(String academicYear);
    
    // ========== PAGINATION METHODS ==========
    
    Page<Class> findByDeletedFalse(Pageable pageable);
    
    Page<Class> findByClassNameContainingAndDeletedFalse(String name, Pageable pageable);
    
    Page<Class> findByGradeLevelContainingAndDeletedFalse(String gradeLevel, Pageable pageable);
    
    Page<Class> findByAcademicYearContainingAndDeletedFalse(String academicYear, Pageable pageable);
    
}
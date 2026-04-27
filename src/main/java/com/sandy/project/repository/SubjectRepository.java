package com.sandy.project.repository;

import com.sandy.project.domain.Subject;
import com.sandy.project.dto.query.SubjectQueryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository untuk Subject entity
 * Extends JpaSpecificationExecutor untuk support dynamic query dengan Specification
 */
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>, JpaSpecificationExecutor<Subject> {
    Subject findBySecureId(String secureId);
    
    // ========================================
    // JPA PROJECTION - Solusi N+1 Problem
    // ========================================
    
    /**
     * CATATAN PENTING TENTANG SUBJECT:
     *
     * Subject memiliki relasi @ManyToMany dengan Teacher (eligibleTeachers)
     * Untuk menghindari N+1 problem, kita TIDAK BISA menggunakan constructor projection
     * dengan List<Teacher> karena JPA tidak support subquery yang return collection.
     *
     * SOLUSI untuk Subject:
     * 1. Gunakan JOIN FETCH (method di bawah) - return full entity
     * 2. Atau ambil Subject dulu, lalu query teachers terpisah di Service layer
     * 3. Atau gunakan @EntityGraph (alternatif JOIN FETCH)
     *
     * Untuk saat ini, gunakan JOIN FETCH method yang sudah ada di bawah.
     */
    
    /**
     * JPA Projection untuk Subject tanpa eager loading teachers
     * Ambil hanya data subject, teachers akan di-handle terpisah di service layer
     */
    @Query("SELECT new com.sandy.project.dto.query.SubjectQueryDTO(" +
            "s.secureId, s.name, s.description) " +
            "FROM Subject s " +
            "WHERE s.deleted = false")
    List<SubjectQueryDTO> findAllSubjectQueryDTO();
    
    /**
     * Fetch single Subject by secureId dengan JPA Projection
     */
    @Query("SELECT new com.sandy.project.dto.query.SubjectQueryDTO(" +
            "s.secureId, s.name, s.description) " +
            "FROM Subject s " +
            "WHERE s.secureId = :secureId AND s.deleted = false")
    Optional<SubjectQueryDTO> findSubjectQueryDTOBySecureId(@Param("secureId") String secureId);
    
    /**
     * Fetch eligible teachers untuk sebuah Subject
     * Method terpisah untuk ambil teachers
     */
    @Query("SELECT new com.sandy.project.dto.query.TeacherQueryDTO(" +
            "t.secureId, t.name, t.address, t.birthDate, t.gender) " +
            "FROM Subject s " +
            "JOIN s.eligibleTeachers t " +
            "WHERE s.secureId = :subjectSecureId AND s.deleted = false AND t.deleted = false")
    List<com.sandy.project.dto.query.TeacherQueryDTO> findEligibleTeachersBySubjectSecureId(@Param("subjectSecureId") String subjectSecureId);
    
    // ========================================
    // LEGACY - JOIN FETCH (untuk backward compatibility)
    // ========================================
    
    /**
     * Query optimasi untuk mengambil semua Subject dengan eligible teachers
     * Menggunakan JOIN FETCH - return full entity
     */
    @Query("""
            SELECT DISTINCT s
            FROM Subject s
            LEFT JOIN FETCH s.eligibleTeachers
            WHERE s.deleted = false
            """)
    List<Subject> findAllWithTeachers();
    
    /**
     * Query optimasi by secureId dengan eligible teachers
     * Menggunakan JOIN FETCH - return full entity
     *
     * @param secureId ID unik subject
     * @return Optional Subject dengan teachers di-load
     */
    @Query("""
            SELECT DISTINCT s
            FROM Subject s
            LEFT JOIN FETCH s.eligibleTeachers t
            WHERE s.secureId = :secureId AND s.deleted = false
            """)
    Optional<Subject> findBySecureIdWithTeachers(@Param("secureId") String secureId);
}

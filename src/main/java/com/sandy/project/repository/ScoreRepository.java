package com.sandy.project.repository;

import com.sandy.project.domain.Score;
import com.sandy.project.domain.Student;
import com.sandy.project.dto.query.ScoreQueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository untuk Score entity
 * Extends JpaSpecificationExecutor untuk support dynamic query dengan Specification
 *
 * Tujuan: Menyediakan method untuk akses database Score
 * Method dari JpaSpecificationExecutor yang otomatis tersedia:
 * - Page<Score> findAll(Specification<Score> spec, Pageable pageable)
 */
@Repository
public interface ScoreRepository extends JpaRepository<Score, Long>, JpaSpecificationExecutor<Score> {
    
    // ========================================
    // JPA PROJECTION - Solusi N+1 Problem
    // ========================================
    
    /**
     * Fetch single Score dengan JPA Projection - Menghindari N+1
     *
     * Query JOIN Score + Student + Subject dalam 1 query
     * Tidak ada lazy loading terpisah
     */
    @Query("SELECT new com.sandy.project.dto.query.ScoreQueryDTO(" +
            "s.secureId, s.score, s.semester, " +
            "st.secureId, st.name, " +
            "sub.secureId, sub.name) " +
            "FROM Score s " +
            "JOIN s.student st " +
            "JOIN s.subject sub " +
            "WHERE s.secureId = :secureId AND s.deleted = false")
    Optional<ScoreQueryDTO> findScoreQueryDTOBySecureId(String secureId);
    
    /**
     * Fetch ALL Scores dengan JPA Projection - SOLUSI N+1 Problem
     *
     * Menggunakan 1 query JOIN untuk ambil Score + Student + Subject sekaligus
     * tanpa lazy loading terpisah
     */
    @Query("SELECT new com.sandy.project.dto.query.ScoreQueryDTO(" +
            "s.secureId, s.score, s.semester, " +
            "st.secureId, st.name, " +
            "sub.secureId, sub.name) " +
            "FROM Score s " +
            "JOIN s.student st " +
            "JOIN s.subject sub " +
            "WHERE s.deleted = false")
    List<ScoreQueryDTO> findAllScoreQueryDTO();
    
    /**
     * Fetch Scores by Student dengan JPA Projection
     */
    @Query("SELECT new com.sandy.project.dto.query.ScoreQueryDTO(" +
            "s.secureId, s.score, s.semester, " +
            "st.secureId, st.name, " +
            "sub.secureId, sub.name) " +
            "FROM Score s " +
            "JOIN s.student st " +
            "JOIN s.subject sub " +
            "WHERE st.secureId = :studentSecureId AND s.deleted = false")
    List<ScoreQueryDTO> findScoreQueryDTOByStudentSecureId(String studentSecureId);
    
    /**
     * Fetch Scores by Student and Semester dengan JPA Projection
     */
    @Query("SELECT new com.sandy.project.dto.query.ScoreQueryDTO(" +
            "s.secureId, s.score, s.semester, " +
            "st.secureId, st.name, " +
            "sub.secureId, sub.name) " +
            "FROM Score s " +
            "JOIN s.student st " +
            "JOIN s.subject sub " +
            "WHERE st.secureId = :studentSecureId AND s.semester = :semester AND s.deleted = false")
    List<ScoreQueryDTO> findScoreQueryDTOByStudentSecureIdAndSemester(String studentSecureId, String semester);
    
    /**
     * Fetch Scores by Subject dengan JPA Projection
     */
    @Query("SELECT new com.sandy.project.dto.query.ScoreQueryDTO(" +
            "s.secureId, s.score, s.semester, " +
            "st.secureId, st.name, " +
            "sub.secureId, sub.name) " +
            "FROM Score s " +
            "JOIN s.student st " +
            "JOIN s.subject sub " +
            "WHERE sub.secureId = :subjectSecureId AND s.deleted = false")
    List<ScoreQueryDTO> findScoreQueryDTOBySubjectSecureId(String subjectSecureId);
    
    // ========================================
    // BASIC QUERY - Cari berdasarkan secureId
    // ========================================
    Optional<Score> findBySecureId(String secureId);
    
    // ========================================
    // QUERY - Cari semua nilai berdasarkan student
    // ========================================
    // SQL: SELECT s.* FROM scores s
    //      JOIN students st ON s.student_id = st.id
    //      WHERE st.secure_id = ?
    List<Score> findByStudent_SecureId(String studentSecureId);
    
    // ========================================
    // QUERY - Cari berdasarkan student entity (alternatif)
    // ========================================
    List<Score> findByStudent(Student student);
    
    // ========================================
    // QUERY - Cari berdasarkan subject
    // ========================================
    List<Score> findBySubject_SecureId(String subjectSecureId);
    
    // ========================================
    // QUERY - Cari berdasarkan student DAN semester
    // ========================================
    List<Score> findByStudent_SecureIdAndSemester(String studentSecureId, String semester);
    
    // NOTE: Method save(), saveAll(), delete(), findAll()
    // sudah otomatis ada dari JpaRepository!
    
    // ========== PAGINATION METHODS ==========
    
    // General: Get all scores dengan soft delete check
    Page<Score> findByDeletedFalse(Pageable pageable);
    
    // Filter by Student (using student secureId)
    Page<Score> findByStudent_SecureIdAndDeletedFalse(String studentSecureId, Pageable pageable);
    
    // Filter by Subject (using subject secureId)
    Page<Score> findBySubject_SecureIdAndDeletedFalse(String subjectSecureId, Pageable pageable);
    
    // Filter by Semester
    Page<Score> findBySemesterAndDeletedFalse(String semester, Pageable pageable);
    
    // Filter by Student AND Semester (combined filter)
    Page<Score> findByStudent_SecureIdAndSemesterAndDeletedFalse(String studentSecureId, String semester, Pageable pageable);
    
    // Filter by Subject AND Semester (combined filter)
    Page<Score> findBySubject_SecureIdAndSemesterAndDeletedFalse(String subjectSecureId, String semester, Pageable pageable);
}
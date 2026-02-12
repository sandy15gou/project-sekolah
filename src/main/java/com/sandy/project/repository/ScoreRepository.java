package com.sandy.project.repository;

import com.sandy.project.domain.Score;
import com.sandy.project.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
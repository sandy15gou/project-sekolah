package com.sandy.project.repository;

import com.sandy.project.domain.Score;
import com.sandy.project.dto.ScoreCreateDTO;
import com.sandy.project.dto.ScoreDetailDTO;
import com.sandy.project.dto.ScoreUpdateDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    
    // NOTE: findById(Long id) sudah otomatis ada dari JpaRepository
    // Digunakan untuk internal system (join table, cascade operations)
    
    // ========================================
    // BASIC QUERY - Cari berdasarkan secureId
    // ========================================
    // Use case: Cari 1 nilai spesifik dari API
    // Contoh: GET /api/scores/{secureId}
    Optional<Score> findBySecureId(String secureId);
    
    
    // ========================================
    // CONTOH 1: Query Berdasarkan RELASI (Student)
    // ========================================
    // CARA BACA METHOD NAME:
    // findBy              → keyword Spring Data JPA
    // Student             → nama field di Score.java (private Student student)
    // _SecureId           → field di Student.java (private String secureId)
    //
    // QUERY SQL YANG DIHASILKAN:
    // SELECT s.* FROM scores s
    // JOIN students st ON s.student_id = st.id
    // WHERE st.secure_id = ?
    //
    // USE CASE:
    // - Lihat semua nilai satu siswa
    // - Tampilkan rapor siswa (semua semester, semua mata pelajaran)
    //
    // CONTOH PENGGUNAAN DI SERVICE:
    // List<Score> scores = scoreRepository.findByStudent_SecureId("uuid-student-123");
    // → Return: Matematika=80, IPA=90, IPS=75, dst...
    List<Score> findByStudent_SecureId(String studentSecureId);
    void createScore(List<ScoreCreateDTO> dtos);
    void updateScore(String scoreId, ScoreUpdateDTO dto);
    void deleteScore(String scoreId);
    
    // RETURN VALUE - perlu kasih data balik
    ScoreDetailDTO findScoreDetail(String scoreId);
    List<ScoreDetailDTO> findAllScores();
    List<ScoreDetailDTO> findScoresByStudent(String studentId);
}

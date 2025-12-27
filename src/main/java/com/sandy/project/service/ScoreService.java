package com.sandy.project.service;

import com.sandy.project.dto.ScoreCreateDTO;
import com.sandy.project.dto.ScoreResponseDTO;
import com.sandy.project.dto.ScoreUpdateDTO;

import java.util.List;

public interface ScoreService {
    
    // ========================================
    // CREATE - Input banyak nilai sekaligus
    // ========================================
    void createNewScore(List<ScoreCreateDTO> dtos);
    
    // ========================================
    // UPDATE - Update 1 nilai
    // ========================================
    void updateScore(String scoreId, ScoreUpdateDTO dto);
    
    // ========================================
    // DELETE - Hapus 1 nilai (soft delete)
    // ========================================
    void deleteScore(String scoreId);
    
    // ========================================
    // READ - Lihat detail 1 nilai
    // ========================================
    ScoreResponseDTO findScoreDetailById(String scoreId);
    
    // ========================================
    // READ - Lihat SEMUA nilai
    // ========================================
    List<ScoreResponseDTO> findAllScores();
    
    // ========================================
    // READ - Lihat nilai berdasarkan siswa tertentu
    // ========================================
    List<ScoreResponseDTO> findScoresByStudent(String studentId);
    
    // ========================================
    // BONUS - Lihat nilai berdasarkan siswa DAN semester
    // ========================================
    List<ScoreResponseDTO> findScoresByStudentAndSemester(String studentId, String semester);
}
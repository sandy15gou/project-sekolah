package com.sandy.project.service;

import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.ScoreCreateDTO;
import com.sandy.project.dto.ScoreFilterDTO;
import com.sandy.project.dto.ScoreResponseDTO;
import com.sandy.project.dto.ScoreUpdateDTO;

import java.util.List;

public interface ScoreService {
    
    // ========================================
    // CREATE - Input banyak nilai sekaligus
    
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
    
    // ========== PAGINATION METHODS ==========
    
    /**
     * Find all scores dengan pagination dan sorting
     * @param page Page number (0-based)
     * @param size Items per page (max 50)
     * @param sortBy Field untuk sorting (score, semester, createdAt)
     * @param sortDirection Arah sorting (ASC atau DESC)
     * @return PagedResponseDTO berisi list scores dan metadata pagination
     */
    PagedResponseDTO<ScoreResponseDTO> findAllScoresPaged(int page, int size, String sortBy, String sortDirection);
    
    /**
     * Search scores by student secureId dengan pagination
     * @param studentSecureId SecureId dari student
     * @param page Page number
     * @param size Items per page
     * @param sortBy Field untuk sorting
     * @param sortDirection Arah sorting
     * @return PagedResponseDTO berisi hasil pencarian
     */
    PagedResponseDTO<ScoreResponseDTO> searchScoresByStudentPaged(String studentSecureId, int page, int size, String sortBy, String sortDirection);
    
    /**
     * Search scores by subject secureId dengan pagination
     * @param subjectSecureId SecureId dari subject
     * @param page Page number
     * @param size Items per page
     * @param sortBy Field untuk sorting
     * @param sortDirection Arah sorting
     * @return PagedResponseDTO berisi hasil pencarian
     */
    PagedResponseDTO<ScoreResponseDTO> searchScoresBySubjectPaged(String subjectSecureId, int page, int size, String sortBy, String sortDirection);
    
    /**
     * Search scores by semester dengan pagination
     * @param semester Semester yang dicari (e.g., "1", "2")
     * @param page Page number
     * @param size Items per page
     * @param sortBy Field untuk sorting
     * @param sortDirection Arah sorting
     * @return PagedResponseDTO berisi hasil pencarian
     */
    PagedResponseDTO<ScoreResponseDTO> searchScoresBySemesterPaged(String semester, int page, int size, String sortBy, String sortDirection);
    
    /**
     * Search scores by student AND semester (combined filter) dengan pagination
     * @param studentSecureId SecureId dari student
     * @param semester Semester yang dicari
     * @param page Page number
     * @param size Items per page
     * @param sortBy Field untuk sorting
     * @param sortDirection Arah sorting
     * @return PagedResponseDTO berisi hasil pencarian
     */
    PagedResponseDTO<ScoreResponseDTO> searchScoresByStudentAndSemesterPaged(String studentSecureId, String semester, int page, int size, String sortBy, String sortDirection);
    
    /**
     * Search scores by subject AND semester (combined filter) dengan pagination
     * @param subjectSecureId SecureId dari subject
     * @param semester Semester yang dicari
     * @param page Page number
     * @param size Items per page
     * @param sortBy Field untuk sorting
     * @param sortDirection Arah sorting
     * @return PagedResponseDTO berisi hasil pencarian
     */
    PagedResponseDTO<ScoreResponseDTO> searchScoresBySubjectAndSemesterPaged(String subjectSecureId, String semester, int page, int size, String sortBy, String sortDirection);
    
    /**
     * Filter scores dengan multiple criteria (dynamic query)
     * Semua criteria di filter akan di-combine dengan AND logic
     *
     * @param filter ScoreFilterDTO berisi kriteria filter (minScore, maxScore, semester, studentId, subjectId, grade, isPassing)
     * @param page Page number (0-based)
     * @param size Items per page (max 50)
     * @param sortBy Field untuk sorting (score, semester, createdAt)
     * @param sortDirection Arah sorting (ASC atau DESC)
     * @return PagedResponseDTO berisi hasil filter
     *
     * Tujuan: Menyediakan method untuk filter Score dengan kombinasi kriteria
     */
    PagedResponseDTO<ScoreResponseDTO> filterScores(ScoreFilterDTO filter, int page, int size, String sortBy, String sortDirection);
}
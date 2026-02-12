package com.sandy.project.web;

import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.ScoreCreateDTO;
import com.sandy.project.dto.ScoreFilterDTO;
import com.sandy.project.dto.ScoreResponseDTO;
import com.sandy.project.dto.ScoreUpdateDTO;
import com.sandy.project.service.ScoreService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@AllArgsConstructor
@RestController
@Validated
public class ScoreResource {
    
    private final ScoreService scoreService;
    

    @PostMapping("/v1/scores")
    public ResponseEntity<Void> createNewScore(@RequestBody @Valid List<ScoreCreateDTO> dtos) {
        scoreService.createNewScore(dtos);
        return ResponseEntity.created(URI.create("/v1/scores")).build();
    }
    
 
    @GetMapping("/v1/scores")
    public ResponseEntity<List<ScoreResponseDTO>> getAllScores() {
        List<ScoreResponseDTO> scores = scoreService.findAllScores();
        return ResponseEntity.ok(scores);
    }
    
 
    @GetMapping("/v1/scores/{scoreId}")
    public ResponseEntity<ScoreResponseDTO> getScoreDetail(@PathVariable String scoreId) {
        ScoreResponseDTO score = scoreService.findScoreDetailById(scoreId);
        return ResponseEntity.ok(score);
    }
    
    
    @PutMapping("/v1/scores/{scoreId}")
    public ResponseEntity<Void> updateScore(
            @PathVariable String scoreId,
            @RequestBody @Valid ScoreUpdateDTO dto) {
        scoreService.updateScore(scoreId, dto);
        return ResponseEntity.ok().build();
    }
    
 
    @DeleteMapping("/v1/scores/{scoreId}")
    public ResponseEntity<Void> deleteScore(@PathVariable String scoreId) {
        scoreService.deleteScore(scoreId);
        return ResponseEntity.ok().build();
    }
    
   
    @GetMapping("/v1/scores/student/{studentId}")
    public ResponseEntity<List<ScoreResponseDTO>> getScoresByStudent(
            @PathVariable String studentId) {
        List<ScoreResponseDTO> scores = scoreService.findScoresByStudent(studentId);
        return ResponseEntity.ok(scores);
    }
    
 
    @GetMapping("/v1/scores/student/{studentId}/semester/{semester}")
    public ResponseEntity<List<ScoreResponseDTO>> getScoresByStudentAndSemester(
            @PathVariable String studentId,
            @PathVariable String semester) {
        List<ScoreResponseDTO> scores = scoreService.findScoresByStudentAndSemester(studentId, semester);
        return ResponseEntity.ok(scores);
    }
    
    // ========== PAGINATION ENDPOINTS ==========
    
    /**
     * Get all scores dengan pagination dan sorting
     * Contoh: GET /v1/scores/paged?page=0&size=10&sortBy=score&sortDirection=DESC
     */
    @GetMapping("/v1/scores/paged")
    public ResponseEntity<PagedResponseDTO<ScoreResponseDTO>> getAllScoresPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "score") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        PagedResponseDTO<ScoreResponseDTO> response = scoreService.findAllScoresPaged(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search scores by student secureId dengan pagination
     * Contoh: GET /v1/scores/search/by-student?studentId=abc123&page=0&size=10
     */
    @GetMapping("/v1/scores/search/by-student")
    public ResponseEntity<PagedResponseDTO<ScoreResponseDTO>> searchScoresByStudent(
            @RequestParam String studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "score") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        PagedResponseDTO<ScoreResponseDTO> response = scoreService.searchScoresByStudentPaged(studentId, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search scores by subject secureId dengan pagination
     * Contoh: GET /v1/scores/search/by-subject?subjectId=math001&page=0&size=10
     */
    @GetMapping("/v1/scores/search/by-subject")
    public ResponseEntity<PagedResponseDTO<ScoreResponseDTO>> searchScoresBySubject(
            @RequestParam String subjectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "score") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        PagedResponseDTO<ScoreResponseDTO> response = scoreService.searchScoresBySubjectPaged(subjectId, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search scores by semester dengan pagination
     * Contoh: GET /v1/scores/search/by-semester?semester=1&page=0&size=10
     */
    @GetMapping("/v1/scores/search/by-semester")
    public ResponseEntity<PagedResponseDTO<ScoreResponseDTO>> searchScoresBySemester(
            @RequestParam String semester,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "score") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        PagedResponseDTO<ScoreResponseDTO> response = scoreService.searchScoresBySemesterPaged(semester, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search scores by student AND semester (combined filter) dengan pagination
     * Contoh: GET /v1/scores/search/by-student-and-semester?studentId=abc123&semester=1&page=0&size=10
     */
    @GetMapping("/v1/scores/search/by-student-and-semester")
    public ResponseEntity<PagedResponseDTO<ScoreResponseDTO>> searchScoresByStudentAndSemester(
            @RequestParam String studentId,
            @RequestParam String semester,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "score") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        PagedResponseDTO<ScoreResponseDTO> response = scoreService.searchScoresByStudentAndSemesterPaged(studentId, semester, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search scores by subject AND semester (combined filter) dengan pagination
     * Contoh: GET /v1/scores/search/by-subject-and-semester?subjectId=math001&semester=1&page=0&size=10
     */
    @GetMapping("/v1/scores/search/by-subject-and-semester")
    public ResponseEntity<PagedResponseDTO<ScoreResponseDTO>> searchScoresBySubjectAndSemester(
            @RequestParam String subjectId,
            @RequestParam String semester,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "score") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        PagedResponseDTO<ScoreResponseDTO> response = scoreService.searchScoresBySubjectAndSemesterPaged(subjectId, semester, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Filter scores dengan multiple criteria (advanced filtering)
     * Support filtering by: minScore, maxScore, semester, studentId, subjectId, grade, isPassing
     * Semua parameter bersifat optional dan akan di-combine dengan AND logic
     *
     * Contoh penggunaan:
     * 1. Filter by passing grade (>= 75):
     *    GET /v1/scores/filter?isPassing=true&page=0&size=10
     *
     * 2. Filter by score range:
     *    GET /v1/scores/filter?minScore=80&maxScore=100&page=0&size=10
     *
     * 3. Filter by grade (A, B, C, D, E):
     *    GET /v1/scores/filter?grade=A&page=0&size=10
     *
     * 4. Filter by student (semua nilai siswa tertentu):
     *    GET /v1/scores/filter?studentId=uuid-123&page=0&size=10
     *
     * 5. Filter by subject (semua nilai mata pelajaran tertentu):
     *    GET /v1/scores/filter?subjectId=uuid-456&page=0&size=10
     *
     * 6. Filter by semester:
     *    GET /v1/scores/filter?semester=1&page=0&size=10
     *
     * 7. Complex filter - kombinasi multiple criteria:
     *    GET /v1/scores/filter?studentId=uuid-123&semester=1&minScore=75&page=0&size=10&sortBy=score&sortDirection=DESC
     *
     * 8. Filter siswa yang tidak lulus (< 75):
     *    GET /v1/scores/filter?isPassing=false&semester=1&page=0&size=10
     *
     * @param minScore Filter nilai minimal (>=)
     * @param maxScore Filter nilai maksimal (<=)
     * @param semester Filter semester (partial match)
     * @param studentId Filter berdasarkan student secureId (exact match)
     * @param subjectId Filter berdasarkan subject secureId (exact match)
     * @param grade Filter berdasarkan grade (A, B, C, D, E)
     * @param isPassing Filter berdasarkan status lulus (true = >= 75, false = < 75)
     * @param page Halaman data (0-based, default: 0)
     * @param size Jumlah data per halaman (default: 10, max: 50)
     * @param sortBy Field untuk sorting (default: score)
     * @param sortDirection Arah sorting ASC/DESC (default: DESC)
     * @return PagedResponseDTO berisi list score dan metadata pagination
     */
    @GetMapping("/v1/scores/filter")
    public ResponseEntity<PagedResponseDTO<ScoreResponseDTO>> filterScores(
            @RequestParam(required = false) Integer minScore,
            @RequestParam(required = false) Integer maxScore,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String subjectId,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) Boolean isPassing,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "score") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        // STEP 1: Build filter DTO dari request params
        // Tujuan: Kumpulkan semua kriteria filter dalam satu object
        // Semua field optional, yang null akan diabaikan saat build query
        ScoreFilterDTO filter = ScoreFilterDTO.builder()
                .minScore(minScore)         // Dari URL: ?minScore=75
                .maxScore(maxScore)         // Dari URL: ?maxScore=100
                .semester(semester)         // Dari URL: ?semester=1
                .studentId(studentId)       // Dari URL: ?studentId=uuid-123
                .subjectId(subjectId)       // Dari URL: ?subjectId=uuid-456
                .grade(grade)               // Dari URL: ?grade=A
                .isPassing(isPassing)       // Dari URL: ?isPassing=true
                .build();
        
        // STEP 2: Call service layer untuk proses filtering
        // Service akan:
        // - Validasi input (size, sortBy, sortDirection)
        // - Build Specification dari filter DTO
        // - Execute query ke database
        // - Convert entity ke DTO
        // - Return PagedResponseDTO
        PagedResponseDTO<ScoreResponseDTO> response = scoreService.filterScores(filter, page, size, sortBy, sortDirection);
        
        // STEP 3: Return response ke user
        return ResponseEntity.ok(response);
    }
}
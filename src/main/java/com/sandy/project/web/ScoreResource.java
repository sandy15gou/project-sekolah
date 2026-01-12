package com.sandy.project.web;

import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.ScoreCreateDTO;
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
}
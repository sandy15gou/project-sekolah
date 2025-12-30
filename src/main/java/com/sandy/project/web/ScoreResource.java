package com.sandy.project.web;

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
}
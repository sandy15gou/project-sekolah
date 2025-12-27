package com.sandy.project.service.impl;

import com.sandy.project.domain.Score;
import com.sandy.project.domain.Student;
import com.sandy.project.domain.Subject;
import com.sandy.project.dto.ScoreCreateDTO;
import com.sandy.project.dto.ScoreResponseDTO;
import com.sandy.project.dto.ScoreUpdateDTO;
import com.sandy.project.exception.ResourceNotFoundException;
import com.sandy.project.repository.ScoreRepository;
import com.sandy.project.repository.StudentRepository;
import com.sandy.project.repository.SubjectRepository;
import com.sandy.project.service.ScoreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ScoreServiceImpl implements ScoreService {
    
    private final ScoreRepository scoreRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    
    // ========================================
    // CREATE - Input banyak nilai sekaligus
    // ========================================
    @Override
    public void createNewScore(List<ScoreCreateDTO> dtos) {
        List<Score> scores = dtos.stream()
                .map(dtoItem -> {
                    Score score = new Score();
                    
                    // Validasi student exist
                    Student student = studentRepository.findBySecureId(dtoItem.getStudentId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Student not found with ID: " + dtoItem.getStudentId()
                            ));
                    score.setStudent(student);
                    
                    // Validasi subject exist
                    Subject subject = subjectRepository.findBySecureId(dtoItem.getSubjectId());
                    if (subject == null) {
                        throw new ResourceNotFoundException(
                                "Subject not found with ID: " + dtoItem.getSubjectId()
                        );
                    }
                    score.setSubject(subject);
                    
                    // Set nilai (validasi 0-100 ada di setter entity)
                    score.setScore(Integer.parseInt(dtoItem.getScore()));
                    score.setSemester(dtoItem.getSemester());
                    
                    return score;
                })
                .toList();
        
        scoreRepository.saveAll(scores);
    }
    
    // ========================================
    // UPDATE - Update 1 nilai
    // ========================================
    @Override
    public void updateScore(String scoreId, ScoreUpdateDTO dto) {
        Score score = scoreRepository.findBySecureId(scoreId)
                .orElseThrow(() -> new ResourceNotFoundException("Score not found"));
        
        // Update nilai (validasi 0-100 ada di setter entity)
        score.setScore(Integer.valueOf(dto.getScore()));
        score.setSemester(dto.getSemester());
        
        scoreRepository.save(score);
    }
    
    // ========================================
    // DELETE - Hapus 1 nilai (soft delete)
    // ========================================
    @Override
    public void deleteScore(String scoreId) {
        Score score = scoreRepository.findBySecureId(scoreId)
                .orElseThrow(() -> new ResourceNotFoundException("Score not found"));
        
        scoreRepository.delete(score);  // Soft delete karena ada @SQLDelete di entity
    }
    
    // ========================================
    // READ - Lihat detail 1 nilai
    // ========================================
    @Override
    public ScoreResponseDTO findScoreDetailById(String scoreId) {
        Score score = scoreRepository.findBySecureId(scoreId)
                .orElseThrow(() -> new ResourceNotFoundException("Score not found with ID: " + scoreId));
        
        return convertToResponseDTO(score);
    }
    
    // ========================================
    // READ - Lihat SEMUA nilai
    // ========================================
    @Override
    public List<ScoreResponseDTO> findAllScores() {
        List<Score> scores = scoreRepository.findAll();
        
        return scores.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }
    
    // ========================================
    // READ - Lihat nilai berdasarkan siswa tertentu
    // ========================================
    @Override
    public List<ScoreResponseDTO> findScoresByStudent(String studentId) {
        // Validasi student exist
        studentRepository.findBySecureId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        
        // Ambil semua nilai student tersebut
        List<Score> scores = scoreRepository.findByStudent_SecureId(studentId);
        
        return scores.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }
    
    // ========================================
    // BONUS - Lihat nilai berdasarkan siswa DAN semester
    // ========================================
    @Override
    public List<ScoreResponseDTO> findScoresByStudentAndSemester(String studentId, String semester) {
        // Validasi student exist
        studentRepository.findBySecureId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        
        // Ambil nilai student di semester tertentu
        List<Score> scores = scoreRepository.findByStudent_SecureIdAndSemester(studentId, semester);
        
        return scores.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }
    
    // ========================================
    // HELPER METHOD - Convert Entity ke DTO
    // ========================================
    private ScoreResponseDTO convertToResponseDTO(Score score) {
        return ScoreResponseDTO.builder()
                .secureId(score.getSecureId())
                .studentId(score.getStudent().getSecureId())
                .studentName(score.getStudent().getName())  // Sesuaikan dengan getter di Student entity
                .subjectId(score.getSubject().getSecureId())
                .subjectName(score.getSubject().getName())  // Sesuaikan dengan getter di Subject entity
                .score(score.getScore())
                .semester(score.getSemester())
                .grade(score.getGrade())  // Business logic dari entity
                .isPassing(score.isPassing())  // Business logic dari entity
                .build();
    }
}
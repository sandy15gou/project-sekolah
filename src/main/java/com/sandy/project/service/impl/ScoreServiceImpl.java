package com.sandy.project.service.impl;

import com.sandy.project.domain.Score;
import com.sandy.project.domain.Student;
import com.sandy.project.domain.Subject;
import com.sandy.project.dto.ScoreCreateDTO;
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
    
    @Override
    public void createNewScore(List<ScoreCreateDTO> dtos) {
        List<Score> scores = dtos.stream()
                .map(dtoItem -> {
               
                    Score score = new Score();
                    
                    // 2. Cari Student berdasarkan secureId
                    Student student = studentRepository.findBySecureId(dtoItem.getStudentId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Student not found with ID: " + dtoItem.getStudentId()
                            ));
                    score.setStudent(student);
                    
                    // 3. Cari Subject berdasarkan secureId
                    Subject subject = subjectRepository.findBySecureId(dtoItem.getSubjectId());
                    if (subject == null) {
                        throw new ResourceNotFoundException(
                                "Subject not found with ID: " + dtoItem.getSubjectId()
                        );
                    }
                    score.setSubject(subject);
                    
                    // 4. ⭐⭐⭐ SET SCORE - INI PASTI ADA! ⭐⭐⭐
                    score.setScore(Integer.parseInt(dtoItem.getScore()));
                    
                    // 5. ⭐⭐⭐ SET SEMESTER - INI JUGA PASTI ADA! ⭐⭐⭐
                    score.setSemester(dtoItem.getSemester());
                    
                    return score;
                })
                .toList();
        
        // 6. Save ke database
        scoreRepository.saveAll(scores);
    }
    
    @Override
    public void updateScore(String scoreId, ScoreUpdateDTO dto) {
        // TODO: implement
    }
    
    @Override
    public void deleteScore(String scoreId) {
        // TODO: implement
    }
    
    @Override
    public ScoreUpdateDTO findScoreDetailById(String scoreId) {
        return null;
    }
    
    @Override
    public ScoreUpdateDTO findAllScores(String scoreId) {
        return null;
    }
    
    @Override
    public ScoreUpdateDTO findScoresByStudent(String studentId) {
        return null;
    }
}
package com.sandy.project.service.impl;

import com.sandy.project.domain.Score;
import com.sandy.project.dto.ScoreCreateDTO;
import com.sandy.project.dto.ScoreUpdateDTO;
import com.sandy.project.repository.ScoreRepository;
import com.sandy.project.service.ScoreService;

import java.util.List;

public class ScoreServiceImpl implements ScoreService {
    @Override
    public void createNewScore(List<ScoreCreateDTO> dtos) {
    List<Score> scores = dtos.stream().map((dtoItem) -> {
        Score score = new Score();
        score.setStudentId(dtoItem.getStudentId());
        score.setSubjectId(dtoItem.getSubjectId());
        score.setScore(Integer.parseInt(dtoItem.getScore()));
        score.setSemester(Integer.parseInt(dtoItem.getSemester()));
        return score;
    }).toList();
        ScoreRepository.saveAll(scores);
    }
    
    @Override
    public void updateScore(String scoreId, ScoreUpdateDTO dto) {
    
    }
    
    @Override
    public void deleteScore(String scoreId) {
    
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

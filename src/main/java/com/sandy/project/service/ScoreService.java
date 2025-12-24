package com.sandy.project.service;

import com.sandy.project.dto.ScoreCreateDTO;
import com.sandy.project.dto.ScoreUpdateDTO;

import java.util.List;

public interface ScoreService {
   public void createNewScore(List<ScoreCreateDTO> dtos);
   void updateScore(String scoreId, ScoreUpdateDTO dto);
   public void deleteScore(String scoreId);
    ScoreUpdateDTO findScoreDetailById(String scoreId);
    ScoreUpdateDTO findAllScores(String scoreId);
    ScoreUpdateDTO findScoresByStudent(String studentId);
    
}
// ✅ createScore(List<ScoreCreateDTO> dtos) → input banyak nilai sekaligus
//✅ updateScore(String scoreId, ScoreUpdateDTO dto) → update 1 nilai
//✅ deleteScore(String scoreId) → hapus 1 nilai
//✅ findScoreDetail(String scoreId) → lihat detail lengkap 1 nilai
//✅ findScoreById(String scoreId) → lihat ringkas 1 nilai (kalau perlu)
//✅ findAllScores() → lihat semua nilai
//✅ findScoresByStudent(String studentId) → lihat nilai berdasarkan siswa
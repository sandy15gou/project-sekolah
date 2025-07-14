package com.sandy.project.service;

import com.sandy.project.dto.TeacherCreateDTO;
import com.sandy.project.dto.TeacherDetailDTO;
import com.sandy.project.dto.TeacherResponseDTO;

import java.util.List;

public interface TeacherService {
    
    public void createNewTeacher(List<TeacherCreateDTO> dtos);
    
    void updateTeacher(String teacherId, TeacherCreateDTO dto);
    
    void deleteTeacher(String teacherId);
    
    TeacherDetailDTO findTeacherDetail(String id);
    
    public TeacherResponseDTO findTeacherById(String id);
}

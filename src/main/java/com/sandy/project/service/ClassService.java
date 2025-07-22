
package com.sandy.project.service;

import com.sandy.project.dto.ClassDetailDTO;
import com.sandy.project.dto.ClassRequestDTO;

import java.util.List;

public interface ClassService {
    void createNewClass(List<ClassRequestDTO> dtos);
    void updateClass(String classId, ClassRequestDTO dto);
    void deleteClass(String classId);
    ClassDetailDTO findClassDetail(String classId);
    List<ClassDetailDTO> findAllClasses();
    void addStudentToClass(String classId, String studentId);
    void removeStudentFromClass(String classId, String studentId);
}
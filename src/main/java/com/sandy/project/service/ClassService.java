
package com.sandy.project.service;

import com.sandy.project.dto.ClassDetailDTO;
import com.sandy.project.dto.ClassRequestDTO;
import com.sandy.project.dto.ClassResponseDTO;
import com.sandy.project.dto.PagedResponseDTO;

import java.util.List;

public interface ClassService {
    void createNewClass(List<ClassRequestDTO> dtos);
    void updateClass(String classId, ClassRequestDTO dto);
    void deleteClass(String classId);
    ClassDetailDTO findClassDetail(String classId);
    List<ClassDetailDTO> findAllClasses();
    void addStudentToClass(String classId, String studentId);
    void removeStudentFromClass(String classId, String studentId);
    PagedResponseDTO<ClassResponseDTO> findAllClassesPaged(int page, int size, String sortBy, String sortDirection);
    PagedResponseDTO<ClassResponseDTO> searchClassesByClassNamePaged(String className, int page, int size, String sortBy, String sortDirection);
    PagedResponseDTO<ClassResponseDTO> searchClassesByAcademicYearPaged(String academicYear, int page, int size, String sortBy, String sortDirection);
    PagedResponseDTO<ClassResponseDTO> searchClassesByGradeLevelPaged(String gradeLevel, int page, int size, String sortBy, String sortDirection);
}
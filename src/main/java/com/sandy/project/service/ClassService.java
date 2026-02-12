
package com.sandy.project.service;

import com.sandy.project.dto.ClassDetailDTO;
import com.sandy.project.dto.ClassFilterDTO;
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
    
    /**
     * Filter classes dengan multiple criteria (dynamic query)
     * Semua criteria di filter akan di-combine dengan AND logic
     *
     * @param filter ClassFilterDTO berisi kriteria filter (className, gradeLevel, academicYear, capacity)
     * @param page Page number (0-based)
     * @param size Items per page (max 50)
     * @param sortBy Field untuk sorting (className, gradeLevel, academicYear, maxCapacity)
     * @param sortDirection Arah sorting (ASC atau DESC)
     * @return PagedResponseDTO berisi hasil filter
     *
     * Tujuan: Menyediakan method untuk filter Class dengan kombinasi kriteria
     */
    PagedResponseDTO<ClassResponseDTO> filterClasses(ClassFilterDTO filter, int page, int size, String sortBy, String sortDirection);
}
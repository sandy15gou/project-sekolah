package com.sandy.project.service;

import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.TeacherCreateDTO;
import com.sandy.project.dto.TeacherDetailDTO;
import com.sandy.project.dto.TeacherFilterDTO;
import com.sandy.project.dto.TeacherResponseDTO;

import java.util.List;

public interface TeacherService {
    
    public void createNewTeacher(List<TeacherCreateDTO> dtos);
    
    void updateTeacher(String teacherId, TeacherCreateDTO dto);
    
    void deleteTeacher(String teacherId);
    
    TeacherDetailDTO findTeacherDetail(String id);
    
    public TeacherResponseDTO findTeacherById(String id);
    
    /**
     * Find all teachers menggunakan JPA Projection - SOLUSI N+1 Problem
     * @return List of TeacherDetailDTO
     */
    List<TeacherDetailDTO> findAllTeachers();
    
    // ========== PAGINATION METHODS ==========
    
    /**
     * Find all teachers dengan pagination dan sorting
     * @param page Page number (0-based)
     * @param size Items per page (max 50)
     * @param sortBy Field untuk sorting (hanya: name, createdAt)
     * @param sortDirection Arah sorting (ASC atau DESC)
     * @return PagedResponseDTO berisi list teachers dan metadata pagination
     */
    PagedResponseDTO<TeacherResponseDTO> findAllTeachersPaged(int page, int size, String sortBy, String sortDirection);
    
    /**
     * Search teachers by name dengan pagination
     * @param name Nama yang dicari (partial match)
     * @param page Page number (0-based)
     * @param size Items per page (max 50)
     * @param sortBy Field untuk sorting (hanya: name, createdAt)
     * @param sortDirection Arah sorting (ASC atau DESC)
     * @return PagedResponseDTO berisi hasil pencarian
     */
    PagedResponseDTO<TeacherResponseDTO> searchTeachersByNamePaged(String name, int page, int size, String sortBy, String sortDirection);
    
    /**
     * Filter teachers dengan multiple criteria (dynamic filtering)
     * @param filter TeacherFilterDTO berisi kriteria filter (semua optional)
     * @param page Page number (0-based)
     * @param size Items per page (max 50)
     * @param sortBy Field untuk sorting (whitelist: name, createdAt, birthDate, gender)
     * @param sortDirection Arah sorting (ASC atau DESC)
     * @return PagedResponseDTO berisi hasil filter dengan pagination metadata
     */
    PagedResponseDTO<TeacherResponseDTO> filterTeachers(TeacherFilterDTO filter, int page, int size, String sortBy, String sortDirection);
}

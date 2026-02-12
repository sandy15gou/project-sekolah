package com.sandy.project.service;

import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.SubjectDetailDTO;
import com.sandy.project.dto.SubjectFilterDTO;
import com.sandy.project.dto.SubjectResponseDTO;
import java.util.List;

public interface SubjectService {
    SubjectDetailDTO findSubjectDetail(String subjectId);
    List<SubjectDetailDTO> findAllSubjects();
    void createSubject(SubjectDetailDTO dto);
    void updateSubject(String subjectId, SubjectDetailDTO dto);
    void deleteSubject(String subjectId);
    void addEligibleTeacher(String subjectId, String teacherId);
    void removeEligibleTeacher(String subjectId, String teacherId);
    
    /**
     * Filter subjects dengan multiple criteria (dynamic query)
     * Semua criteria di filter akan di-combine dengan AND logic
     *
     * @param filter SubjectFilterDTO berisi kriteria filter (name, description)
     * @param page Page number (0-based)
     * @param size Items per page (max 50)
     * @param sortBy Field untuk sorting (name, createdAt)
     * @param sortDirection Arah sorting (ASC atau DESC)
     * @return PagedResponseDTO berisi hasil filter
     *
     * Tujuan: Menyediakan method untuk filter Subject dengan kombinasi kriteria
     */
    PagedResponseDTO<SubjectResponseDTO> filterSubjects(SubjectFilterDTO filter, int page, int size, String sortBy, String sortDirection);
}

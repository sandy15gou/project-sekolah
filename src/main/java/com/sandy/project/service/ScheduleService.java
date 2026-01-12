package com.sandy.project.service;

import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.ScheduleDetailDTO;
import com.sandy.project.dto.ScheduleResponseDTO;

import java.util.List;

public interface ScheduleService {
    ScheduleDetailDTO findScheduleDetail(String scheduleId);
    List<ScheduleDetailDTO> findAllSchedules();
    void createSchedule(ScheduleDetailDTO dto);
    void updateSchedule(String scheduleId, ScheduleDetailDTO dto);
    void deleteSchedule(String scheduleId);
    
    // ========== PAGINATION METHODS ==========
    
    /**
     * Find all schedules dengan pagination dan sorting
     * @param page Page number (0-based)
     * @param size Items per page (max 50)
     * @param sortBy Field untuk sorting (day, startTime, semester, createdAt)
     * @param sortDirection Arah sorting (ASC atau DESC)
     * @return PagedResponseDTO berisi list schedules dan metadata pagination
     */
    PagedResponseDTO<ScheduleResponseDTO> findAllSchedulesPaged(int page, int size, String sortBy, String sortDirection);
    
    /**
     * Search schedules by day dengan pagination
     * @param day Hari yang dicari (e.g., "Monday", "Tuesday")
     * @param page Page number
     * @param size Items per page
     * @param sortBy Field untuk sorting
     * @param sortDirection Arah sorting
     * @return PagedResponseDTO berisi hasil pencarian
     */
    PagedResponseDTO<ScheduleResponseDTO> searchSchedulesByDayPaged(String day, int page, int size, String sortBy, String sortDirection);
    
    /**
     * Search schedules by semester dengan pagination
     * @param semester Semester yang dicari (e.g., "1", "2")
     * @param page Page number
     * @param size Items per page
     * @param sortBy Field untuk sorting
     * @param sortDirection Arah sorting
     * @return PagedResponseDTO berisi hasil pencarian
     */
    PagedResponseDTO<ScheduleResponseDTO> searchSchedulesBySemesterPaged(String semester, int page, int size, String sortBy, String sortDirection);
    
    /**
     * Search schedules by class secureId dengan pagination
     * @param classSecureId SecureId dari class
     * @param page Page number
     * @param size Items per page
     * @param sortBy Field untuk sorting
     * @param sortDirection Arah sorting
     * @return PagedResponseDTO berisi hasil pencarian
     */
    PagedResponseDTO<ScheduleResponseDTO> searchSchedulesByClassPaged(String classSecureId, int page, int size, String sortBy, String sortDirection);
    
    /**
     * Search schedules by subject secureId dengan pagination
     * @param subjectSecureId SecureId dari subject
     * @param page Page number
     * @param size Items per page
     * @param sortBy Field untuk sorting
     * @param sortDirection Arah sorting
     * @return PagedResponseDTO berisi hasil pencarian
     */
    PagedResponseDTO<ScheduleResponseDTO> searchSchedulesBySubjectPaged(String subjectSecureId, int page, int size, String sortBy, String sortDirection);
    
    /**
     * Search schedules by teacher secureId dengan pagination
     * @param teacherSecureId SecureId dari teacher
     * @param page Page number
     * @param size Items per page
     * @param sortBy Field untuk sorting
     * @param sortDirection Arah sorting
     * @return PagedResponseDTO berisi hasil pencarian
     */
    PagedResponseDTO<ScheduleResponseDTO> searchSchedulesByTeacherPaged(String teacherSecureId, int page, int size, String sortBy, String sortDirection);
    
    /**
     * Search schedules by day AND semester (combined filter) dengan pagination
     * @param day Hari yang dicari
     * @param semester Semester yang dicari
     * @param page Page number
     * @param size Items per page
     * @param sortBy Field untuk sorting
     * @param sortDirection Arah sorting
     * @return PagedResponseDTO berisi hasil pencarian
     */
    PagedResponseDTO<ScheduleResponseDTO> searchSchedulesByDayAndSemesterPaged(String day, String semester, int page, int size, String sortBy, String sortDirection);
}


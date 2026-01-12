package com.sandy.project.web;

import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.ScheduleDetailDTO;
import com.sandy.project.dto.ScheduleResponseDTO;
import com.sandy.project.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;



@AllArgsConstructor
@RestController
@Validated
public class ScheduleResource {
    
    private final ScheduleService scheduleService;
    
    
    @PostMapping("/v1/schedules")
    public ResponseEntity<Void> createSchedule(@RequestBody @Valid ScheduleDetailDTO dto) {
        scheduleService.createSchedule(dto);
        return ResponseEntity.created(URI.create("/v1/schedules")).build();
    }
    
  
    @GetMapping("/v1/schedules")
    public ResponseEntity<List<ScheduleDetailDTO>> getAllSchedules() {
        List<ScheduleDetailDTO> schedules = scheduleService.findAllSchedules();
        return ResponseEntity.ok(schedules);
    }
    
 
    @GetMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<ScheduleDetailDTO> getScheduleDetail(@PathVariable String scheduleId) {
        ScheduleDetailDTO schedule = scheduleService.findScheduleDetail(scheduleId);
        return ResponseEntity.ok(schedule);
    }
    

    @PutMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<Void> updateSchedule(
            @PathVariable String scheduleId,
            @RequestBody @Valid ScheduleDetailDTO dto) {
        scheduleService.updateSchedule(scheduleId, dto);
        return ResponseEntity.ok().build();
    }
    

    @DeleteMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.ok().build();
    }
    
    // ========== PAGINATION ENDPOINTS ==========
    
    /**
     * Get all schedules dengan pagination dan sorting
     * Contoh: GET /v1/schedules/paged?page=0&size=10&sortBy=day&sortDirection=ASC
     */
    @GetMapping("/v1/schedules/paged")
    public ResponseEntity<PagedResponseDTO<ScheduleResponseDTO>> getAllSchedulesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "day") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        PagedResponseDTO<ScheduleResponseDTO> response = scheduleService.findAllSchedulesPaged(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search schedules by day dengan pagination
     * Contoh: GET /v1/schedules/search/by-day?day=Monday&page=0&size=10
     */
    @GetMapping("/v1/schedules/search/by-day")
    public ResponseEntity<PagedResponseDTO<ScheduleResponseDTO>> searchSchedulesByDay(
            @RequestParam String day,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "day") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        PagedResponseDTO<ScheduleResponseDTO> response = scheduleService.searchSchedulesByDayPaged(day, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search schedules by semester dengan pagination
     * Contoh: GET /v1/schedules/search/by-semester?semester=1&page=0&size=10
     */
    @GetMapping("/v1/schedules/search/by-semester")
    public ResponseEntity<PagedResponseDTO<ScheduleResponseDTO>> searchSchedulesBySemester(
            @RequestParam String semester,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "semester") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        PagedResponseDTO<ScheduleResponseDTO> response = scheduleService.searchSchedulesBySemesterPaged(semester, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search schedules by class secureId dengan pagination
     * Contoh: GET /v1/schedules/search/by-class?classId=abc123&page=0&size=10
     */
    @GetMapping("/v1/schedules/search/by-class")
    public ResponseEntity<PagedResponseDTO<ScheduleResponseDTO>> searchSchedulesByClass(
            @RequestParam String classId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "day") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        PagedResponseDTO<ScheduleResponseDTO> response = scheduleService.searchSchedulesByClassPaged(classId, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search schedules by subject secureId dengan pagination
     * Contoh: GET /v1/schedules/search/by-subject?subjectId=xyz789&page=0&size=10
     */
    @GetMapping("/v1/schedules/search/by-subject")
    public ResponseEntity<PagedResponseDTO<ScheduleResponseDTO>> searchSchedulesBySubject(
            @RequestParam String subjectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "day") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        PagedResponseDTO<ScheduleResponseDTO> response = scheduleService.searchSchedulesBySubjectPaged(subjectId, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search schedules by teacher secureId dengan pagination
     * Contoh: GET /v1/schedules/search/by-teacher?teacherId=def456&page=0&size=10
     */
    @GetMapping("/v1/schedules/search/by-teacher")
    public ResponseEntity<PagedResponseDTO<ScheduleResponseDTO>> searchSchedulesByTeacher(
            @RequestParam String teacherId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "day") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        PagedResponseDTO<ScheduleResponseDTO> response = scheduleService.searchSchedulesByTeacherPaged(teacherId, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search schedules by day AND semester (combined filter) dengan pagination
     * Contoh: GET /v1/schedules/search/by-day-and-semester?day=Monday&semester=1&page=0&size=10
     */
    @GetMapping("/v1/schedules/search/by-day-and-semester")
    public ResponseEntity<PagedResponseDTO<ScheduleResponseDTO>> searchSchedulesByDayAndSemester(
            @RequestParam String day,
            @RequestParam String semester,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "day") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        PagedResponseDTO<ScheduleResponseDTO> response = scheduleService.searchSchedulesByDayAndSemesterPaged(day, semester, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
}
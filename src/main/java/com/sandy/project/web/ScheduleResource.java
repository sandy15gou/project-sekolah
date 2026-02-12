package com.sandy.project.web;

import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.ScheduleDetailDTO;
import com.sandy.project.dto.ScheduleFilterDTO;
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
    
    /**
     * Filter schedules dengan multiple criteria (advanced filtering)
     * Support filtering by: day, startTime, endTime, semester, classId, subjectId, teacherId
     * Semua parameter bersifat optional dan akan di-combine dengan AND logic
     *
     * Contoh penggunaan:
     * 1. Filter by day only:
     *    GET /v1/schedules/filter?day=Monday&page=0&size=10
     *
     * 2. Filter by semester:
     *    GET /v1/schedules/filter?semester=1&page=0&size=10
     *
     * 3. Filter by classId (jadwal untuk kelas tertentu):
     *    GET /v1/schedules/filter?classId=uuid-123&page=0&size=10
     *
     * 4. Filter by teacherId (jadwal mengajar guru tertentu):
     *    GET /v1/schedules/filter?teacherId=uuid-456&page=0&size=10
     *
     * 5. Filter by time range:
     *    GET /v1/schedules/filter?startTime=08:00&endTime=10:00&page=0&size=10
     *
     * 6. Complex filter - kombinasi multiple criteria:
     *    GET /v1/schedules/filter?day=Monday&semester=1&classId=uuid-123&page=0&size=10&sortBy=startTime&sortDirection=ASC
     *
     * @param day Filter hari (partial match, case insensitive)
     * @param startTime Filter waktu mulai (partial match)
     * @param endTime Filter waktu selesai (partial match)
     * @param semester Filter semester (partial match)
     * @param classId Filter berdasarkan class secureId (exact match)
     * @param subjectId Filter berdasarkan subject secureId (exact match)
     * @param teacherId Filter berdasarkan teacher secureId (exact match)
     * @param page Halaman data (0-based, default: 0)
     * @param size Jumlah data per halaman (default: 10, max: 50)
     * @param sortBy Field untuk sorting (default: day)
     * @param sortDirection Arah sorting ASC/DESC (default: ASC)
     * @return PagedResponseDTO berisi list schedule dan metadata pagination
     */
    @GetMapping("/v1/schedules/filter")
    public ResponseEntity<PagedResponseDTO<ScheduleResponseDTO>> filterSchedules(
            @RequestParam(required = false) String day,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String classId,
            @RequestParam(required = false) String subjectId,
            @RequestParam(required = false) String teacherId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "day") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        // STEP 1: Build filter DTO dari request params
        // Tujuan: Kumpulkan semua kriteria filter dalam satu object
        // Semua field optional, yang null akan diabaikan saat build query
        ScheduleFilterDTO filter = ScheduleFilterDTO.builder()
                .day(day)                   // Dari URL: ?day=Monday
                .startTime(startTime)       // Dari URL: ?startTime=08:00
                .endTime(endTime)           // Dari URL: ?endTime=10:00
                .semester(semester)         // Dari URL: ?semester=1
                .classId(classId)           // Dari URL: ?classId=uuid-123
                .subjectId(subjectId)       // Dari URL: ?subjectId=uuid-456
                .teacherId(teacherId)       // Dari URL: ?teacherId=uuid-789
                .build();
        
        // STEP 2: Call service layer untuk proses filtering
        // Service akan:
        // - Validasi input (size, sortBy, sortDirection)
        // - Build Specification dari filter DTO
        // - Execute query ke database
        // - Convert entity ke DTO
        // - Return PagedResponseDTO
        PagedResponseDTO<ScheduleResponseDTO> response = scheduleService.filterSchedules(filter, page, size, sortBy, sortDirection);
        
        // STEP 3: Return response ke user
        return ResponseEntity.ok(response);
    }
}
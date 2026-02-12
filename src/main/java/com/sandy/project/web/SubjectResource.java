package com.sandy.project.web;

import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.SubjectDetailDTO;
import com.sandy.project.dto.SubjectFilterDTO;
import com.sandy.project.dto.SubjectResponseDTO;
import com.sandy.project.service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class SubjectResource {
    
    private final SubjectService subjectService;
    
    @PostMapping("/v1/subjects")
    public ResponseEntity<String> createSubject(@RequestBody SubjectDetailDTO dto) {
        subjectService.createSubject(dto);
        return ResponseEntity.ok("Subject created successfully");
    }
    
    @GetMapping("/v1/subjects")
    public ResponseEntity<List<SubjectDetailDTO>> getAllSubjects() {
        List<SubjectDetailDTO> subjects = subjectService.findAllSubjects();
        return ResponseEntity.ok(subjects);
    }
    
    @GetMapping("/v1/subjects/{subjectId}")
    public ResponseEntity<SubjectDetailDTO> getSubjectDetail(@PathVariable String subjectId) {
        SubjectDetailDTO subjectDetail = subjectService.findSubjectDetail(subjectId);
        return ResponseEntity.ok(subjectDetail);
    }
    
    @PutMapping("/v1/subjects/{subjectId}")
    public ResponseEntity<String> updateSubject(@PathVariable String subjectId, @RequestBody SubjectDetailDTO dto) {
        subjectService.updateSubject(subjectId, dto);
        return ResponseEntity.ok("Subject updated successfully");
    }
    
    @DeleteMapping("/v1/subjects/{subjectId}")
    public ResponseEntity<String> deleteSubject(@PathVariable String subjectId) {
        subjectService.deleteSubject(subjectId);
        return ResponseEntity.ok("Subject deleted successfully");
    }
    
    @PostMapping("/v1/subjects/{subjectId}/teachers/{teacherId}")
    public ResponseEntity<String> addEligibleTeacher(@PathVariable String subjectId, @PathVariable String teacherId) {
        subjectService.addEligibleTeacher(subjectId, teacherId);
        return ResponseEntity.ok("Teacher added to subject successfully");
    }
    
    @DeleteMapping("/v1/subjects/{subjectId}/teachers/{teacherId}")
    public ResponseEntity<String> removeEligibleTeacher(@PathVariable String subjectId, @PathVariable String teacherId) {
        subjectService.removeEligibleTeacher(subjectId, teacherId);
        return ResponseEntity.ok("Teacher removed from subject successfully");
    }
    
    /**
     * Filter subjects dengan multiple criteria (simple filtering)
     * Support filtering by: name, description
     * Semua parameter bersifat optional dan akan di-combine dengan AND logic
     *
     * Contoh penggunaan:
     * 1. Filter by name:
     *    GET /v1/subjects/filter?name=Math&page=0&size=10
     *
     * 2. Filter by description keyword:
     *    GET /v1/subjects/filter?description=science&page=0&size=10
     *
     * 3. Filter kombinasi:
     *    GET /v1/subjects/filter?name=English&description=language&page=0&size=10&sortBy=name&sortDirection=ASC
     *
     * @param name Filter nama mata pelajaran (partial match, case insensitive)
     * @param description Filter deskripsi (partial match, case insensitive)
     * @param page Halaman data (0-based, default: 0)
     * @param size Jumlah data per halaman (default: 10, max: 50)
     * @param sortBy Field untuk sorting (default: name)
     * @param sortDirection Arah sorting ASC/DESC (default: ASC)
     * @return PagedResponseDTO berisi list subject dan metadata pagination
     */
    @GetMapping("/v1/subjects/filter")
    public ResponseEntity<PagedResponseDTO<SubjectResponseDTO>> filterSubjects(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        // STEP 1: Build filter DTO dari request params
        // Tujuan: Kumpulkan semua kriteria filter dalam satu object
        // Semua field optional, yang null akan diabaikan saat build query
        SubjectFilterDTO filter = SubjectFilterDTO.builder()
                .name(name)                 // Dari URL: ?name=Math
                .description(description)   // Dari URL: ?description=science
                .build();
        
        // STEP 2: Call service layer untuk proses filtering
        // Service akan:
        // - Validasi input (size, sortBy, sortDirection)
        // - Build Specification dari filter DTO
        // - Execute query ke database
        // - Convert entity ke DTO
        // - Return PagedResponseDTO
        PagedResponseDTO<SubjectResponseDTO> response = subjectService.filterSubjects(filter, page, size, sortBy, sortDirection);
        
        // STEP 3: Return response ke user
        return ResponseEntity.ok(response);
    }
}
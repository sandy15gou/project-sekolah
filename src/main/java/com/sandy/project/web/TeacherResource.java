package com.sandy.project.web;

import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.TeacherCreateDTO;
import com.sandy.project.dto.TeacherDetailDTO;
import com.sandy.project.dto.TeacherFilterDTO;
import com.sandy.project.dto.TeacherResponseDTO;
import com.sandy.project.service.TeacherService;
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
public class TeacherResource {
    private final TeacherService teacherService;
    
    @PostMapping("/v1/teacher")
    public ResponseEntity<Void> createNewTeacher(@RequestBody @Valid List<TeacherCreateDTO> dto){
        teacherService.createNewTeacher(dto);
        return ResponseEntity.created(URI.create("/teacher")).build();
    }
    
    @PutMapping("/v1/teacher/{id}")
    public ResponseEntity<Void> updateTeacher(@PathVariable String id, @RequestBody @Valid TeacherCreateDTO dto) {
        teacherService.updateTeacher(id, dto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/v1/teacher/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/v1/teacher/{id}")
    public ResponseEntity<TeacherDetailDTO> findTeacherDetail(@PathVariable String id) {
        TeacherDetailDTO dto = teacherService.findTeacherDetail(id);
        return ResponseEntity.ok(dto);
    }
    
    // ========== PAGINATION ENDPOINTS ==========
    
    /**
     * Get all teachers dengan pagination dan sorting
     * Contoh: GET /v1/teachers/paged?page=0&size=10&sortBy=name&sortDirection=ASC
     */
    @GetMapping("/v1/teachers/paged")
    public ResponseEntity<PagedResponseDTO<TeacherResponseDTO>> getAllTeachersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        PagedResponseDTO<TeacherResponseDTO> response = teacherService.findAllTeachersPaged(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search teachers by name dengan pagination
     * Contoh: GET /v1/teachers/search?name=John&page=0&size=10
     */
    @GetMapping("/v1/teachers/search")
    public ResponseEntity<PagedResponseDTO<TeacherResponseDTO>> searchTeachersByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        PagedResponseDTO<TeacherResponseDTO> response = teacherService.searchTeachersByNamePaged(name, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Filter teachers dengan multiple criteria (dynamic filtering)
     * Semua filter bersifat optional dan akan di-combine dengan AND logic
     *
     * Contoh penggunaan:
     * 1. Filter by name only:
     *    GET /v1/teachers/filter?name=John&page=0&size=10
     *
     * 2. Filter by gender:
     *    GET /v1/teachers/filter?gender=F&page=0&size=10
     *
     * 3. Filter by age range:
     *    GET /v1/teachers/filter?minAge=30&maxAge=50&page=0&size=10
     *
     * 4. Filter by birth date range:
     *    GET /v1/teachers/filter?birthDateFrom=1980-01-01&birthDateTo=1990-12-31&page=0&size=10
     *
     * 5. Complex filter - kombinasi multiple criteria:
     *    GET /v1/teachers/filter?name=John&gender=M&address=Jakarta&minAge=30&page=0&size=10&sortBy=name&sortDirection=ASC
     */
    @GetMapping("/v1/teachers/filter")
    public ResponseEntity<PagedResponseDTO<TeacherResponseDTO>> filterTeachers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String birthDateFrom,
            @RequestParam(required = false) String birthDateTo,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        // Build filter DTO dari request params
        TeacherFilterDTO filter = TeacherFilterDTO.builder()
                .name(name)
                .gender(gender)
                .address(address)
                .birthDateFrom(birthDateFrom != null ? java.time.LocalDate.parse(birthDateFrom) : null)
                .birthDateTo(birthDateTo != null ? java.time.LocalDate.parse(birthDateTo) : null)
                .minAge(minAge)
                .maxAge(maxAge)
                .build();
        
        PagedResponseDTO<TeacherResponseDTO> response = teacherService.filterTeachers(filter, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
}
    
// Note: The above code assumes that the TeacherService and related DTOs are implemented correctly.

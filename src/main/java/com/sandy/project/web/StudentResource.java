package com.sandy.project.web;

import java.net.URI;
import java.util.List;

import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.StudentDetailDTO;
import com.sandy.project.dto.StudentFilterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sandy.project.dto.StudentCreateDTO;
import com.sandy.project.dto.StudentResponseDTO;
import com.sandy.project.dto.StudentUpdateDTO;
import com.sandy.project.service.StudentService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@Validated
public class StudentResource {
    
    private final StudentService studentService;
    
    @PostMapping("/v1/student")
    public ResponseEntity<Void> createNewStudent(@RequestBody @Valid List<StudentCreateDTO> dto){
        studentService.createNewStudent(dto);
        return ResponseEntity.created(URI.create("/student")).build();
    }
    
    @GetMapping("/v1/student/{id}")
    public ResponseEntity<StudentDetailDTO> findStudentDetail(@PathVariable String id) {
        StudentDetailDTO dto = studentService.findStudentDetail(id);
        return ResponseEntity.ok(dto);
    }
    
    @PutMapping("/v1/student/{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable String id,
                                              @RequestBody @Valid StudentUpdateDTO dto) {
        studentService.updateStudent(id, dto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/v1/student/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
    
    // ========== PAGINATION ENDPOINTS ==========
    
    /**
     * Get all students dengan pagination dan sorting
     * Contoh: GET /v1/students/paged?page=0&size=10&sortBy=name&sortDirection=ASC
     */
    @GetMapping("/v1/students/paged")
    public ResponseEntity<PagedResponseDTO<StudentResponseDTO>> getAllStudentsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        PagedResponseDTO<StudentResponseDTO> response = studentService.findAllStudentsPaged(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search students by name dengan pagination
     * Contoh: GET /v1/students/search?name=John&page=0&size=10
     */
    @GetMapping("/v1/students/search")
    public ResponseEntity<PagedResponseDTO<StudentResponseDTO>> searchStudentsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        PagedResponseDTO<StudentResponseDTO> response = studentService.searchStudentsByNamePaged(name, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Filter students dengan multiple criteria (advanced filtering)
     * Support filtering by: name, gender, address, birthDateFrom, birthDateTo, minAge, maxAge
     * Semua parameter bersifat optional dan akan di-combine dengan AND logic
     *
     * Contoh penggunaan:
     * 1. Filter by gender saja:
     *    GET /v1/students/filter?gender=M&page=0&size=10
     *
     * 2. Filter by name dan gender:
     *    GET /v1/students/filter?name=John&gender=M&page=0&size=10
     *
     * 3. Filter by age range:
     *    GET /v1/students/filter?minAge=15&maxAge=18&page=0&size=10
     *
     * 4. Filter by birth date range:
     *    GET /v1/students/filter?birthDateFrom=2005-01-01&birthDateTo=2008-12-31&page=0&size=10
     *
     * 5. Complex filter - kombinasi multiple criteria:
     *    GET /v1/students/filter?name=John&gender=M&address=Jakarta&minAge=16&page=0&size=10&sortBy=name&sortDirection=ASC
     */
    @GetMapping("/v1/students/filter")
    public ResponseEntity<PagedResponseDTO<StudentResponseDTO>> filterStudents(
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
        StudentFilterDTO filter = StudentFilterDTO.builder()
                .name(name)
                .gender(gender)
                .address(address)
                .birthDateFrom(birthDateFrom != null ? java.time.LocalDate.parse(birthDateFrom) : null)
                .birthDateTo(birthDateTo != null ? java.time.LocalDate.parse(birthDateTo) : null)
                .minAge(minAge)
                .maxAge(maxAge)
                .build();
        
        PagedResponseDTO<StudentResponseDTO> response = studentService.filterStudents(filter, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
}
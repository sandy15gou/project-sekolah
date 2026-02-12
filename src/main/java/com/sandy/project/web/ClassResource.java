package com.sandy.project.web;
import com.sandy.project.dto.ClassDetailDTO;
import com.sandy.project.dto.ClassFilterDTO;
import com.sandy.project.dto.ClassRequestDTO;
import com.sandy.project.dto.ClassResponseDTO;
import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.service.ClassService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@AllArgsConstructor
@RestController
@Validated
public class ClassResource {
    private static final Logger logger = LoggerFactory.getLogger(ClassResource.class);
    
    private final ClassService classService;
    
    @PostMapping("/v1/classes")
    public ResponseEntity<Void> createClass(@RequestBody @Valid List<ClassRequestDTO> dtos) {
        classService.createNewClass(dtos);
        return ResponseEntity.created(URI.create("/v1/classes")).build();
    }
    
    @GetMapping("/v1/classes")
    public ResponseEntity<List<ClassDetailDTO>> getAllClasses() {
        List<ClassDetailDTO> classes = classService.findAllClasses();
        return ResponseEntity.ok(classes);
    }
    
    @GetMapping("/v1/classes/{classId}")
    public ResponseEntity<ClassDetailDTO> getClassDetail(@PathVariable String classId) {
        ClassDetailDTO classDetail = classService.findClassDetail(classId);
        return ResponseEntity.ok(classDetail);
    }
    
    @PutMapping("/v1/classes/{classId}")
    public ResponseEntity<Void> updateClass(@PathVariable String classId,
                                            @RequestBody @Valid ClassRequestDTO dto) {
        classService.updateClass(classId, dto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/v1/classes/{classId}")
    public ResponseEntity<Void> deleteClass(@PathVariable String classId) {
        classService.deleteClass(classId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/v1/classes/{classId}/students/{studentId}")
    public ResponseEntity<Void> addStudentToClass(@PathVariable String classId,
                                                  @PathVariable String studentId) {
        logger.info("Received request to add student {} to class {}", studentId, classId);
        classService.addStudentToClass(classId, studentId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/v1/classes/{classId}/students/{studentId}")
    public ResponseEntity<Void> removeStudentFromClass(@PathVariable String classId,
                                                       @PathVariable String studentId) {
        classService.removeStudentFromClass(classId, studentId);
        return ResponseEntity.ok().build();
    }
    
    // ==================== PAGINATION ENDPOINTS ====================
    
    @GetMapping("/v1/classes/paged")
    public ResponseEntity<PagedResponseDTO<ClassResponseDTO>> getAllClassesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "className") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        PagedResponseDTO<ClassResponseDTO> pagedClasses = classService.findAllClassesPaged(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(pagedClasses);
    }
    
    @GetMapping("/v1/classes/search/by-name")
    public ResponseEntity<PagedResponseDTO<ClassResponseDTO>> searchClassesByName(
            @RequestParam String className,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "className") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        PagedResponseDTO<ClassResponseDTO> pagedClasses = classService.searchClassesByClassNamePaged(className, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(pagedClasses);
    }
    
    @GetMapping("/v1/classes/search/by-academic-year")
    public ResponseEntity<PagedResponseDTO<ClassResponseDTO>> searchClassesByAcademicYear(
            @RequestParam String academicYear,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "academicYear") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        PagedResponseDTO<ClassResponseDTO> pagedClasses = classService.searchClassesByAcademicYearPaged(academicYear, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(pagedClasses);
    }
    
    @GetMapping("/v1/classes/search/by-grade-level")
    public ResponseEntity<PagedResponseDTO<ClassResponseDTO>> searchClassesByGradeLevel(
            @RequestParam String gradeLevel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "gradeLevel") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        PagedResponseDTO<ClassResponseDTO> pagedClasses = classService.searchClassesByGradeLevelPaged(gradeLevel, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(pagedClasses);
    }
    
    /**
     * Filter classes dengan multiple criteria (advanced filtering)
     * Support filtering by: className, gradeLevel, academicYear, minCapacity, maxCapacity
     * Semua parameter bersifat optional dan akan di-combine dengan AND logic
     *
     * Contoh penggunaan:
     * 1. Filter by className only:
     *    GET /v1/classes/filter?className=X&page=0&size=10
     *
     * 2. Filter by gradeLevel:
     *    GET /v1/classes/filter?gradeLevel=10&page=0&size=10
     *
     * 3. Filter by academicYear:
     *    GET /v1/classes/filter?academicYear=2024&page=0&size=10
     *
     * 4. Filter by capacity range:
     *    GET /v1/classes/filter?minCapacity=20&maxCapacity=40&page=0&size=10
     *
     * 5. Complex filter - kombinasi multiple criteria:
     *    GET /v1/classes/filter?className=X&gradeLevel=10&academicYear=2024&minCapacity=25&page=0&size=10&sortBy=className&sortDirection=ASC
     *
     * @param className Filter nama kelas (partial match, case insensitive)
     * @param gradeLevel Filter tingkat kelas (partial match, case insensitive)
     * @param academicYear Filter tahun ajaran (partial match, case insensitive)
     * @param minCapacity Filter kapasitas minimal (>=)
     * @param maxCapacity Filter kapasitas maksimal (<=)
     * @param page Halaman data (0-based, default: 0)
     * @param size Jumlah data per halaman (default: 10, max: 50)
     * @param sortBy Field untuk sorting (default: className)
     * @param sortDirection Arah sorting ASC/DESC (default: ASC)
     * @return PagedResponseDTO berisi list class dan metadata pagination
     */
    @GetMapping("/v1/classes/filter")
    public ResponseEntity<PagedResponseDTO<ClassResponseDTO>> filterClasses(
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String gradeLevel,
            @RequestParam(required = false) String academicYear,
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) Integer maxCapacity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "className") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        // STEP 1: Build filter DTO dari request params
        // Tujuan: Kumpulkan semua kriteria filter dalam satu object
        // Semua field optional, yang null akan diabaikan saat build query
        ClassFilterDTO filter = ClassFilterDTO.builder()
                .className(className)           // Dari URL: ?className=X-1
                .gradeLevel(gradeLevel)         // Dari URL: ?gradeLevel=10
                .academicYear(academicYear)     // Dari URL: ?academicYear=2024
                .minCapacity(minCapacity)       // Dari URL: ?minCapacity=20
                .maxCapacity(maxCapacity)       // Dari URL: ?maxCapacity=40
                .build();
        
        // STEP 2: Call service layer untuk proses filtering
        // Service akan:
        // - Validasi input (size, sortBy, sortDirection)
        // - Build Specification dari filter DTO
        // - Execute query ke database
        // - Convert entity ke DTO
        // - Return PagedResponseDTO
        PagedResponseDTO<ClassResponseDTO> response = classService. filterClasses(filter, page, size, sortBy, sortDirection);
        
        // STEP 3: Return response ke user
        return ResponseEntity.ok(response);
    }
}
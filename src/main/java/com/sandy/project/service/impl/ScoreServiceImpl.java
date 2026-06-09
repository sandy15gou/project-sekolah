package com.sandy.project.service.impl;

import com.sandy.project.domain.Score;
import com.sandy.project.domain.Student;
import com.sandy.project.domain.Subject;
import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.ScoreCreateDTO;
import com.sandy.project.dto.ScoreFilterDTO;
import com.sandy.project.dto.ScoreResponseDTO;
import com.sandy.project.dto.ScoreUpdateDTO;
import com.sandy.project.dto.query.ScoreQueryDTO;
import com.sandy.project.exception.ResourceNotFoundException;
import com.sandy.project.repository.ScoreRepository;
import com.sandy.project.repository.StudentRepository;
import com.sandy.project.repository.SubjectRepository;
import com.sandy.project.service.ScoreService;
import com.sandy.project.specification.ScoreSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ScoreServiceImpl implements ScoreService {
    
    private final ScoreRepository scoreRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    
    // Whitelist field yang boleh di-sort (security measure)
    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList("score", "semester", "createdAt");
    private static final int MAX_PAGE_SIZE = 50;
    
    // ========================================
    // CREATE - Input banyak nilai sekaligus
    // ========================================
    @Override
    public void createNewScore(List<ScoreCreateDTO> dtos) {
        List<Score> scores = dtos.stream()
                .map(dtoItem -> {
                    Score score = new Score();
                    
                    // Validasi student exist
                    Student student = studentRepository.findBySecureId(dtoItem.getStudentId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Student not found with ID: " + dtoItem.getStudentId()
                            ));
                    score.setStudent(student);
                    
                    // Validasi subject exist
                    Subject subject = subjectRepository.findBySecureId(dtoItem.getSubjectId());
                    if (subject == null) {
                        throw new ResourceNotFoundException(
                                "Subject not found with ID: " + dtoItem.getSubjectId()
                        );
                    }
                    score.setSubject(subject);
                    
                    // Set nilai (validasi 0-100 ada di setter entity)
                    score.setScore(Integer.parseInt(dtoItem.getScore()));
                    score.setSemester(dtoItem.getSemester());
                    
                    return score;
                })
                .toList();
        
        scoreRepository.saveAll(scores);
    }
    
    // ========================================
    // UPDATE - Update 1 nilai
    // ========================================
    @Override
    public void updateScore(String scoreId, ScoreUpdateDTO dto) {
        Score score = scoreRepository.findBySecureId(scoreId)
                .orElseThrow(() -> new ResourceNotFoundException("Score not found"));
        
        // Update nilai (validasi 0-100 ada di setter entity)
        score.setScore(Integer.valueOf(dto.getScore()));
        score.setSemester(dto.getSemester());
        
        scoreRepository.save(score);
    }
    
    // ========================================
    // DELETE - Hapus 1 nilai (soft delete)
    // ========================================
    @Override
    public void deleteScore(String scoreId) {
        Score score = scoreRepository.findBySecureId(scoreId)
                .orElseThrow(() -> new ResourceNotFoundException("Score not found"));
        
        scoreRepository.delete(score);  // Soft delete karena ada @SQLDelete di entity
    }
    
    // ========================================
    // READ - Lihat detail 1 nilai
    // ========================================
    @Override
    public ScoreResponseDTO findScoreDetailById(String scoreId) {
        // OPTIMASI: Gunakan JPA Projection untuk menghindari N+1 problem
        ScoreQueryDTO queryDTO = scoreRepository.findScoreQueryDTOBySecureId(scoreId)
                .orElseThrow(() -> new ResourceNotFoundException("Score not found with ID: " + scoreId));
        
        return convertQueryDTOToResponseDTO(queryDTO);
    }
    
    // ========================================
    // READ - Lihat SEMUA nilai
    // ========================================
    @Override
    public List<ScoreResponseDTO> findAllScores() {
        // OPTIMASI: Gunakan JPA Projection untuk menghindari N+1 problem
        // Mengambil Score + Student + Subject dalam 1 query JOIN
        List<ScoreQueryDTO> queryDTOs = scoreRepository.findAllScoreQueryDTO();
        
        return queryDTOs.stream()
                .map(this::convertQueryDTOToResponseDTO)
                .toList();
    }
    
    // ========================================
    // READ - Lihat nilai berdasarkan siswa tertentu
    // ========================================
    @Override
    public List<ScoreResponseDTO> findScoresByStudent(String studentId) {
        // Validasi student exist
        studentRepository.findBySecureId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        
        // OPTIMASI: Gunakan JPA Projection untuk menghindari N+1 problem
        List<ScoreQueryDTO> queryDTOs = scoreRepository.findScoreQueryDTOByStudentSecureId(studentId);
        
        return queryDTOs.stream()
                .map(this::convertQueryDTOToResponseDTO)
                .toList();
    }
    
    // ========================================
    // BONUS - Lihat nilai berdasarkan siswa DAN semester
    // ========================================
    @Override
    public List<ScoreResponseDTO> findScoresByStudentAndSemester(String studentId, String semester) {
        // Validasi student exist
        studentRepository.findBySecureId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        
        // Ambil nilai student di semester tertentu — dengan eager loading (N+1 fix)
        List<Score> scores = scoreRepository.findByStudentAndSemesterWithRelationsList(studentId, semester);
        
        return scores.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }
    
    // ========================================
    // HELPER METHOD - Convert Entity ke DTO
    // ========================================
    private ScoreResponseDTO convertToResponseDTO(Score score) {
        return ScoreResponseDTO.builder()
                .secureId(score.getSecureId())
                .studentId(score.getStudent().getSecureId())
                .studentName(score.getStudent().getName())  // Sesuaikan dengan getter di Student entity
                .subjectId(score.getSubject().getSecureId())
                .subjectName(score.getSubject().getName())  // Sesuaikan dengan getter di Subject entity
                .score(score.getScore())
                .semester(score.getSemester())
                .grade(score.getGrade())  // Business logic dari entity
                .isPassing(score.isPassing())  // Business logic dari entity
                .build();
    }
    
    /**
     * Helper method untuk convert ScoreQueryDTO (JPA Projection) ke ScoreResponseDTO
     * Digunakan untuk menghindari N+1 problem
     */
    private ScoreResponseDTO convertQueryDTOToResponseDTO(ScoreQueryDTO queryDTO) {
        // Calculate grade dan isPassing berdasarkan score value
        int scoreValue = queryDTO.score() != null ? queryDTO.score() : 0;
        String grade = calculateGrade(scoreValue);
        boolean isPassing = scoreValue >= 60;
        
        return ScoreResponseDTO.builder()
                .secureId(queryDTO.secureId())
                .studentId(queryDTO.studentSecureId())
                .studentName(queryDTO.studentName())
                .subjectId(queryDTO.subjectSecureId())
                .subjectName(queryDTO.subjectName())
                .score(queryDTO.score())
                .semester(queryDTO.semester())
                .grade(grade)
                .isPassing(isPassing)
                .build();
    }
    
    /**
     * Helper method untuk kalkulasi grade berdasarkan score
     */
    private String calculateGrade(int score) {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "E";
    }
    
    // ========== PAGINATION METHODS ==========
    
    @Override
    public PagedResponseDTO<ScoreResponseDTO> findAllScoresPaged(int page, int size, String sortBy, String sortDirection) {
        // Validasi dan buat Pageable
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        
        // Query ke database
        Page<Score> scorePage = scoreRepository.findAllWithRelations(pageable);
        
        // Convert ke DTO
        Page<ScoreResponseDTO> dtoPage = scorePage.map(this::convertToResponseDTO);
        
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<ScoreResponseDTO> searchScoresByStudentPaged(String studentSecureId, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Score> scorePage = scoreRepository.findByStudentSecureIdWithRelations(studentSecureId, pageable);
        Page<ScoreResponseDTO> dtoPage = scorePage.map(this::convertToResponseDTO);
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<ScoreResponseDTO> searchScoresBySubjectPaged(String subjectSecureId, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Score> scorePage = scoreRepository.findBySubjectSecureIdWithRelations(subjectSecureId, pageable);
        Page<ScoreResponseDTO> dtoPage = scorePage.map(this::convertToResponseDTO);
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<ScoreResponseDTO> searchScoresBySemesterPaged(String semester, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Score> scorePage = scoreRepository.findBySemesterWithRelations(semester, pageable);
        Page<ScoreResponseDTO> dtoPage = scorePage.map(this::convertToResponseDTO);
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<ScoreResponseDTO> searchScoresByStudentAndSemesterPaged(String studentSecureId, String semester, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Score> scorePage = scoreRepository.findByStudentAndSemesterWithRelations(studentSecureId, semester, pageable);
        Page<ScoreResponseDTO> dtoPage = scorePage.map(this::convertToResponseDTO);
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<ScoreResponseDTO> searchScoresBySubjectAndSemesterPaged(String subjectSecureId, String semester, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Score> scorePage = scoreRepository.findBySubjectAndSemesterWithRelations(subjectSecureId, semester, pageable);
        Page<ScoreResponseDTO> dtoPage = scorePage.map(this::convertToResponseDTO);
        return new PagedResponseDTO<>(dtoPage);
    }
    
    // ========== HELPER METHODS ==========
    
    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        // Validasi size
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        
        // Validasi sortBy
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "score"; // Default ke score
        }
        
        // Validasi direction
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    /**
     * Filter scores dengan multiple criteria menggunakan Specification
     *
     * Flow:
     * 1. Validasi input (size, sortBy, sortDirection)
     * 2. Build Pageable object
     * 3. Build Specification dari ScoreFilterDTO
     * 4. Execute query dengan findAll(spec, pageable)
     * 5. Convert hasil ke DTO
     * 6. Return PagedResponseDTO
     */
    @Override
    public PagedResponseDTO<ScoreResponseDTO> filterScores(ScoreFilterDTO filter, int page, int size, String sortBy, String sortDirection) {
        // STEP 1: Validasi size tidak melebihi max
        // Tujuan: Mencegah user request data terlalu banyak sekaligus
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        
        // STEP 2: Validasi sortBy field (whitelist untuk keamanan)
        // Tujuan: Mencegah SQL injection & error jika field tidak valid
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "score"; // Default ke score kalau field tidak valid
        }
        
        // STEP 3: Validasi sort direction
        // Tujuan: Pastikan hanya ASC atau DESC
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
            ? Sort.Direction.DESC   // Descending (100 → 0)
            : Sort.Direction.ASC;   // Ascending (0 → 100)
        
        // STEP 4: Buat Pageable object
        // Breakdown:
        // - page: halaman ke berapa (0-based, 0 = halaman pertama)
        // - size: berapa data per halaman
        // - Sort.by(direction, sortBy): urutkan berdasarkan field & arah
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        // STEP 5: Build Specification dari filter DTO
        // Tujuan: Buat instruksi query dinamis berdasarkan field yang diisi
        // ScoreSpecification.filterBy() akan:
        // - Cek field mana yang tidak null
        // - Build kondisi WHERE untuk setiap field
        // - Gabungkan dengan AND logic
        // - Support filter berdasarkan grade (A, B, C, D, E)
        // - Support filter berdasarkan isPassing (lulus/tidak)
        Specification<Score> spec = ScoreSpecification.filterBy(filter);
        
        // STEP 6: Query ke database dengan specification & pagination
        // Method findAll(spec, pageable) OTOMATIS ada dari JpaSpecificationExecutor
        // Return: Page<Score> berisi data + metadata (totalElements, totalPages, dll)
        Page<Score> scorePage = scoreRepository.findAll(spec, pageable);
        
        // STEP 7: Convert Score entity ke ScoreResponseDTO
        // Tujuan: Hanya kirim data yang perlu (security), hide field internal
        // convertToResponseDTO() adalah helper method yang sudah ada
        Page<ScoreResponseDTO> dtoPage = scorePage.map(this::convertToResponseDTO);
        
        // STEP 8: Wrap ke PagedResponseDTO dan return
        // PagedResponseDTO berisi:
        // - content: List<ScoreResponseDTO>
        // - page, size, totalElements, totalPages, last, first, dll
        return new PagedResponseDTO<>(dtoPage);
    }
}
package com.sandy.project.service.impl;

import com.sandy.project.domain.Subject;
import com.sandy.project.domain.Teacher;
import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.SubjectDetailDTO;
import com.sandy.project.dto.SubjectFilterDTO;
import com.sandy.project.dto.SubjectResponseDTO;
import com.sandy.project.dto.TeacherDetailDTO;
import com.sandy.project.exception.ResourceNotFoundException;
import com.sandy.project.repository.SubjectRepository;
import com.sandy.project.repository.TeacherRepository;
import com.sandy.project.service.SubjectService;
import com.sandy.project.specification.SubjectSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class SubjectServiceImpl implements SubjectService {
    
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    
    // Whitelist field yang boleh di-sort (security measure)
    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList("name", "createdAt");
    private static final int MAX_PAGE_SIZE = 50;
    
    @Override
    public SubjectDetailDTO findSubjectDetail(String subjectId) {
        // OPTIMASI: Gunakan JOIN FETCH untuk eager load eligible teachers
        Subject subject = subjectRepository.findBySecureIdWithTeachers(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        return mapToDetailDTO(subject);
    }
    
    
    
    @Override
    public List<SubjectDetailDTO> findAllSubjects() {
        // OPTIMASI: JOIN FETCH - 1 query ambil semua subjects + teachers sekaligus
        List<Subject> subjects = subjectRepository.findAllWithTeachers();
        
        return subjects.stream()
                .map(this::mapToDetailDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void createSubject(SubjectDetailDTO dto) {
        Subject subject = new Subject();
        subject.setName(dto.getName());
        subject.setDescription(dto.getDescription());
        subjectRepository.save(subject);
    }
    
    @Override
    public void updateSubject(String subjectId, SubjectDetailDTO dto) {
        Subject subject = subjectRepository.findBySecureId(subjectId);
        if (subject == null) {
            throw new ResourceNotFoundException("Subject not found");
        }
        subject.setName(dto.getName());
        subject.setDescription(dto.getDescription());
        subjectRepository.save(subject);
    }
    
    @Override
    public void deleteSubject(String subjectId) {
        Subject subject = subjectRepository.findBySecureId(subjectId);
        if (subject == null) {
            throw new ResourceNotFoundException("Subject not found");
        }
        subjectRepository.delete(subject);
    }
    
    @Override
    public void addEligibleTeacher(String subjectId, String teacherId) {
        Subject subject = subjectRepository.findBySecureId(subjectId);
        if (subject == null) {
            throw new ResourceNotFoundException("Subject not found");
        }
        
        Teacher teacher = teacherRepository.findBySecureId(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        
        if (subject.getEligibleTeachers() == null) {
            subject.setEligibleTeachers(new ArrayList<>());
        }
        
        boolean teacherExists = subject.getEligibleTeachers().stream()
                .anyMatch(t -> t.getSecureId().equals(teacherId));
        
        if (!teacherExists) {
            subject.getEligibleTeachers().add(teacher);
            subjectRepository.save(subject);
        }
    }
    
    @Override
    public void removeEligibleTeacher(String subjectId, String teacherId) {
        Subject subject = subjectRepository.findBySecureId(subjectId);
        if (subject == null) {
            throw new ResourceNotFoundException("Subject not found");
        }
        
        if (subject.getEligibleTeachers() != null) {
            boolean removed = subject.getEligibleTeachers().removeIf(t -> t.getSecureId().equals(teacherId));
            if (removed) {
                subjectRepository.save(subject);
            }
        }
    }
    
    private SubjectDetailDTO mapToDetailDTO(Subject subject) {
        SubjectDetailDTO dto = new SubjectDetailDTO();
        dto.setSecureId(subject.getSecureId());
        dto.setName(subject.getName());
        dto.setDescription(subject.getDescription());
        
        if (subject.getEligibleTeachers() != null) {
            List<TeacherDetailDTO> eligibleTeacherDTOs = subject.getEligibleTeachers().stream()
                    .map(teacher -> {
                        TeacherDetailDTO teacherDto = new TeacherDetailDTO();
                        teacherDto.setSecureId(teacher.getSecureId());
                        teacherDto.setTeacherId(teacher.getId().toString());
                        teacherDto.setTeacherName(teacher.getName());
                        teacherDto.setTeacherBirthDate(teacher.getBirthDate() != null ? teacher.getBirthDate().toEpochDay() : null);
                        teacherDto.setTeacherGender(teacher.getGender());
                        teacherDto.setTeacherAddress(teacher.getAddress());
                        return teacherDto;
                    }).collect(Collectors.toList());
            dto.setEligibleTeachers(eligibleTeacherDTOs);
        }
        
        return dto;
    }
    
    /**
     * Filter subjects dengan multiple criteria menggunakan Specification
     *
     * Flow:
     * 1. Validasi input (size, sortBy, sortDirection)
     * 2. Build Pageable object
     * 3. Build Specification dari SubjectFilterDTO
     * 4. Execute query dengan findAll(spec, pageable)
     * 5. Convert hasil ke DTO
     * 6. Return PagedResponseDTO
     */
    @Override
    public PagedResponseDTO<SubjectResponseDTO> filterSubjects(SubjectFilterDTO filter, int page, int size, String sortBy, String sortDirection) {
        // STEP 1: Validasi size tidak melebihi max
        // Tujuan: Mencegah user request data terlalu banyak sekaligus
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        
        // STEP 2: Validasi sortBy field (whitelist untuk keamanan)
        // Tujuan: Mencegah SQL injection & error jika field tidak valid
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "name"; // Default ke name kalau field tidak valid
        }
        
        // STEP 3: Validasi sort direction
        // Tujuan: Pastikan hanya ASC atau DESC
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
            ? Sort.Direction.DESC   // Descending (Z → A)
            : Sort.Direction.ASC;   // Ascending (A → Z)
        
        // STEP 4: Buat Pageable object
        // Breakdown:
        // - page: halaman ke berapa (0-based, 0 = halaman pertama)
        // - size: berapa data per halaman
        // - Sort.by(direction, sortBy): urutkan berdasarkan field & arah
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        // STEP 5: Build Specification dari filter DTO
        // Tujuan: Buat instruksi query dinamis berdasarkan field yang diisi
        // SubjectSpecification.filterBy() akan:
        // - Cek field mana yang tidak null
        // - Build kondisi WHERE untuk setiap field
        // - Gabungkan dengan AND logic
        Specification<Subject> spec = SubjectSpecification.filterBy(filter);
        
        // STEP 6: Query ke database dengan specification & pagination
        // Method findAll(spec, pageable) OTOMATIS ada dari JpaSpecificationExecutor
        // Return: Page<Subject> berisi data + metadata (totalElements, totalPages, dll)
        Page<Subject> subjectPage = subjectRepository.findAll(spec, pageable);
        
        // STEP 7: Convert Subject entity ke SubjectResponseDTO
        // Tujuan: Hanya kirim data yang perlu (security), hide field internal
        Page<SubjectResponseDTO> dtoPage = subjectPage.map(subject -> {
            SubjectResponseDTO dto = new SubjectResponseDTO();
            dto.setSecureId(subject.getSecureId());
            dto.setName(subject.getName());
            return dto;
        });
        
        // STEP 8: Wrap ke PagedResponseDTO dan return
        // PagedResponseDTO berisi:
        // - content: List<SubjectResponseDTO>
        // - page, size, totalElements, totalPages, last, first, dll
        return new PagedResponseDTO<>(dtoPage);
    }
}

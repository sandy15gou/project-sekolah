package com.sandy.project.service.impl;

import com.sandy.project.domain.Teacher;
import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.TeacherCreateDTO;
import com.sandy.project.dto.TeacherDetailDTO;
import com.sandy.project.dto.TeacherFilterDTO;
import com.sandy.project.dto.TeacherResponseDTO;
import com.sandy.project.dto.query.TeacherQueryDTO;
import com.sandy.project.exception.ResourceNotFoundException;
import com.sandy.project.repository.TeacherRepository;
import com.sandy.project.service.TeacherService;
import com.sandy.project.specification.TeacherSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    
    private final TeacherRepository teacherRepository;
    
    // Whitelist field yang boleh di-sort (security measure)
    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList("name", "createdAt", "birthDate", "gender");
    private static final int MAX_PAGE_SIZE = 50;
    
    @Override
    public void createNewTeacher(List<TeacherCreateDTO> dtos) {
        List<Teacher> teachers = dtos.stream().map((dtoItem) -> {
            Teacher teacher = new Teacher();
            teacher.setName(dtoItem.getTeacherName());
            teacher.setBirthDate(LocalDate.ofEpochDay(dtoItem.getTeacherBirthDate()));
            teacher.setGender(dtoItem.getTeacherGender());
            teacher.setAddress(dtoItem.getTeacherAddress());
            return teacher;
        }).toList();
        
        teacherRepository.saveAll(teachers);
    }
    
    @Override
    public void updateTeacher(String teacherId, TeacherCreateDTO dto) {
        Teacher teacher = teacherRepository.findBySecureId(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        
        teacher.setName(dto.getTeacherName());
        teacher.setBirthDate(LocalDate.ofEpochDay(dto.getTeacherBirthDate()));
        teacher.setGender(dto.getTeacherGender());
        teacher.setAddress(dto.getTeacherAddress());
        
        teacherRepository.save(teacher);
    }
    
    @Override
    public void deleteTeacher(String teacherId) {
        Teacher teacher = teacherRepository.findBySecureId(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        
        teacherRepository.delete(teacher);
    }
    
    @Override
    public TeacherDetailDTO findTeacherDetail(String id) {
        // OPTIMASI: Gunakan JPA Projection untuk konsistensi
        TeacherQueryDTO queryDTO = teacherRepository.findTeacherQueryDTOBySecureId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        
        return convertQueryDTOToDetailDTO(queryDTO);
    }
    
    @Override
    public List<TeacherDetailDTO> findAllTeachers() {
        // OPTIMASI: Gunakan JPA Projection untuk menghindari N+1 problem
        List<TeacherQueryDTO> queryDTOs = teacherRepository.findAllTeacherQueryDTO();
        
        return queryDTOs.stream()
                .map(this::convertQueryDTOToDetailDTO)
                .toList();
    }
    
    /**
     * Helper method untuk convert TeacherQueryDTO ke TeacherDetailDTO
     */
    private TeacherDetailDTO convertQueryDTOToDetailDTO(TeacherQueryDTO queryDTO) {
        TeacherDetailDTO dto = new TeacherDetailDTO();
        dto.setSecureId(queryDTO.secureId());
        dto.setTeacherName(queryDTO.name());
        dto.setTeacherGender(queryDTO.gender());
        // Convert LocalDate dari QueryDTO ke Long (epoch) untuk DetailDTO
        dto.setTeacherBirthDate(queryDTO.birthDate() != null ? queryDTO.birthDate().toEpochDay() : null);
        dto.setTeacherAddress(queryDTO.address());
        return dto;
    }
    
    @Override
    public TeacherResponseDTO findTeacherById(String id) {
        Teacher teacher = teacherRepository.findBySecureId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        
        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto.setTeacherName(teacher.getName());
        dto.setBirthDate(teacher.getBirthDate() != null ? teacher.getBirthDate().toEpochDay() : null);
        return dto;
        
    }
    
    // ========== PAGINATION METHODS ==========
    
    @Override
    public PagedResponseDTO<TeacherResponseDTO> findAllTeachersPaged(int page, int size, String sortBy, String sortDirection) {
        // Validasi size tidak melebihi max
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        
        // Validasi sortBy field (whitelist untuk keamanan)
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "name"; // Default ke name kalau field tidak valid
        }
        
        // Validasi sort direction
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        // Buat Pageable object
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        // Query ke database
        Page<Teacher> teacherPage = teacherRepository.findByDeletedFalse(pageable);
        
        // Convert Teacher entity ke TeacherResponseDTO
        Page<TeacherResponseDTO> dtoPage = teacherPage.map(teacher -> {
            TeacherResponseDTO dto = new TeacherResponseDTO();
            dto.setTeacherName(teacher.getName());
            dto.setBirthDate(teacher.getBirthDate() != null ? teacher.getBirthDate().toEpochDay() : null);
            return dto;
        });
        
        // Wrap ke PagedResponseDTO
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<TeacherResponseDTO> searchTeachersByNamePaged(String name, int page, int size, String sortBy, String sortDirection) {
        // Validasi size
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        
        // Validasi sortBy
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "name";
        }
        
        // Validasi direction
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        // Buat Pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        // Query search by name
        Page<Teacher> teacherPage = teacherRepository.findByNameContainingAndDeletedFalse(name, pageable);
        
        // Convert ke DTO
        Page<TeacherResponseDTO> dtoPage = teacherPage.map(teacher -> {
            TeacherResponseDTO dto = new TeacherResponseDTO();
            dto.setTeacherName(teacher.getName());
            dto.setBirthDate(teacher.getBirthDate() != null ? teacher.getBirthDate().toEpochDay() : null);
            return dto;
        });
        
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<TeacherResponseDTO> filterTeachers(TeacherFilterDTO filter, int page, int size, String sortBy, String sortDirection) {
        // Validasi size
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        
        // Validasi sortBy
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "name";
        }
        
        // Validasi direction
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        // Buat Pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        // Build Specification dari filter DTO
        Specification<Teacher> spec = TeacherSpecification.filterBy(filter);
        
        // Query dengan dynamic filter menggunakan Specification
        Page<Teacher> teacherPage = teacherRepository.findAll(spec, pageable);
        
        // Convert ke DTO
        Page<TeacherResponseDTO> dtoPage = teacherPage.map(teacher -> {
            TeacherResponseDTO dto = new TeacherResponseDTO();
            dto.setTeacherName(teacher.getName());
            dto.setBirthDate(teacher.getBirthDate() != null ? teacher.getBirthDate().toEpochDay() : null);
            return dto;
        });
        
        return new PagedResponseDTO<>(dtoPage);
    }
}

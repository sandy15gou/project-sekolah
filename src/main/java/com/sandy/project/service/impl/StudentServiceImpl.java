package com.sandy.project.service.impl;


import com.sandy.project.domain.Student;
import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.StudentCreateDTO;
import com.sandy.project.dto.StudentDetailDTO;
import com.sandy.project.dto.StudentFilterDTO;
import com.sandy.project.dto.StudentResponseDTO;
import com.sandy.project.dto.StudentUpdateDTO;
import com.sandy.project.dto.query.StudentQueryDTO;
import com.sandy.project.exception.ResourceNotFoundException;
import com.sandy.project.repository.StudentRepository;
import com.sandy.project.specification.StudentSpecification;
import com.sandy.project.service.StudentService;
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
public class StudentServiceImpl implements StudentService {
    
    private final StudentRepository studentRepository;
    
    // Whitelist field yang boleh di-sort (security measure)
    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList("name", "createdAt", "birthDate", "gender");
    private static final int MAX_PAGE_SIZE = 50;
    
    @Override
    public PagedResponseDTO<StudentResponseDTO> findAllStudentsPaged(int page, int size, String sortBy, String sortDirection) {
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
        Page<Student> studentPage = studentRepository.findByDeletedFalse(pageable);
        
        // Convert Student entity ke StudentResponseDTO
        Page<StudentResponseDTO> dtoPage = studentPage.map(student -> {
            StudentResponseDTO dto = new StudentResponseDTO();
            dto.setStudentName(student.getName());
            dto.setBirthDate(student.getBirthDate().toEpochDay());
            return dto;
        });
        
        // Wrap ke PagedResponseDTO
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<StudentResponseDTO> searchStudentsByNamePaged(String name, int page, int size, String sortBy, String sortDirection) {
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
        Page<Student> studentPage = studentRepository.findByNameContainingAndDeletedFalse(name, pageable);
        
        // Convert ke DTO
        Page<StudentResponseDTO> dtoPage = studentPage.map(student -> {
            StudentResponseDTO dto = new StudentResponseDTO();
            dto.setStudentName(student.getName());
            dto.setBirthDate(student.getBirthDate().toEpochDay());
            return dto;
        });
        
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<StudentResponseDTO> filterStudents(StudentFilterDTO filter, int page, int size, String sortBy, String sortDirection) {
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
        Specification<Student> spec = StudentSpecification.filterBy(filter);
        
        // Query dengan dynamic filter menggunakan Specification
        Page<Student> studentPage = studentRepository.findAll(spec, pageable);
        
        // Convert ke DTO
        Page<StudentResponseDTO> dtoPage = studentPage.map(student -> {
            StudentResponseDTO dto = new StudentResponseDTO();
            dto.setStudentName(student.getName());
            dto.setBirthDate(student.getBirthDate().toEpochDay());
            return dto;
        });
        
        return new PagedResponseDTO<>(dtoPage);
    }
    
    public StudentResponseDTO findStudentById(String id) {
        Student student = studentRepository.findBySecureId(id)
                .orElseThrow(() -> new ResourceNotFoundException("invalid.authorId"));
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setStudentName(student.getName());
        dto.setBirthDate(student.getBirthDate().toEpochDay());
        return dto;
    }
    
    @Override
    public void createNewStudent(List<StudentCreateDTO> dtos) {
        List<Student> students = dtos.stream().map((dtoItem) -> {
            Student student = new Student();
            student.setName(dtoItem.getStudentName());
            student.setGender(dtoItem.getStudentGender());
            student.setAddress(dtoItem.getStudentAddress());
            student.setBirthDate(LocalDate.ofEpochDay(dtoItem.getStudentBirthDate()));
            return student;
        }).toList();
        studentRepository.saveAll(students);
    }
    @Override
    public void updateStudent(String studentId, StudentUpdateDTO dto) {
        Student student = studentRepository.findBySecureId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student Not Found"));
        student.setName(dto.getStudentName());
        student.setBirthDate(LocalDate.ofEpochDay(dto.getStudentBirthDate()));
        student.setGender(dto.getStudentGender());
        student.setAddress(dto.getStudentAddress());
        studentRepository.save(student);
        
        
    
    }
    
    @Override
    public void deleteStudent(String studentId) {
        Student student = studentRepository.findBySecureId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        studentRepository.delete(student);
    
    }
    @Override
    public StudentDetailDTO findStudentDetail(String id) {
        // OPTIMASI: Gunakan JPA Projection untuk menghindari N+1 problem
        // Mengambil Student + Class info dalam 1 query JOIN
        StudentQueryDTO queryDTO = studentRepository.findStudentQueryDTOBySecureId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        
        return convertQueryDTOToDetailDTO(queryDTO);
    }
    
    @Override
    public List<StudentDetailDTO> findAllStudents() {
        // OPTIMASI: Gunakan JPA Projection untuk menghindari N+1 problem
        // Mengambil Student + Class info dalam 1 query JOIN
        List<StudentQueryDTO> queryDTOs = studentRepository.findAllStudentQueryDTO();
        
        return queryDTOs.stream()
                .map(this::convertQueryDTOToDetailDTO)
                .toList();
    }
    
    /**
     * Helper method untuk convert StudentQueryDTO ke StudentDetailDTO
     */
    private StudentDetailDTO convertQueryDTOToDetailDTO(StudentQueryDTO queryDTO) {
        StudentDetailDTO dto = new StudentDetailDTO();
        dto.setSecureId(queryDTO.secureId());
        dto.setStudentName(queryDTO.name());
        dto.setStudentBirthDate(queryDTO.birthDate() != null ? queryDTO.birthDate().toEpochDay() : null);
        dto.setStudentGender(queryDTO.gender());
        dto.setStudentAddress(queryDTO.address());
        
        // Class info dari JPA Projection (tidak trigger lazy loading)
        if (queryDTO.classSecureId() != null) {
            dto.setClassSecureId(queryDTO.classSecureId());
            dto.setClassName(queryDTO.className());
        }
        
        return dto;
    }
    
    
}

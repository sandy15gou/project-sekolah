package com.sandy.project.service.impl;


import com.sandy.project.domain.Student;
import com.sandy.project.dto.StudentCreateDTO;
import com.sandy.project.dto.StudentDetailDTO;
import com.sandy.project.dto.StudentResponseDTO;
import com.sandy.project.dto.StudentUpdateDTO;
import com.sandy.project.exception.ResourceNotFoundException;
import com.sandy.project.repository.StudentRepository;
import com.sandy.project.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    
    private final StudentRepository studentRepository;
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
            student.setBirthDate(LocalDate.ofEpochDay(dtoItem.getStudentBirthDate()));
            return student;
        }).toList();
        studentRepository.saveAll(students);
    }
    @Override
    public void updateStudent(String studentId, StudentUpdateDTO dto) {
    
    }
    
    @Override
    public void deleteStudent(String studentId) {
    
    }
    @Override
    public StudentDetailDTO findStudentDetail(String id) {
        Student student = studentRepository.findBySecureId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        
        StudentDetailDTO dto = new StudentDetailDTO();
        dto.setSecureId(student.getSecureId());
        dto.setStudentName(student.getName());
        dto.setStudentId(student.getId().toString());
        dto.setStudentBirthDate(student.getBirthDate().toEpochDay());
        dto.setStudentGender(student.getGender());
        dto.setStudentAddress(student.getAddress());
        
        return dto;
    }
    
    
}

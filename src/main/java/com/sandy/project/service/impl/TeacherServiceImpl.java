package com.sandy.project.service.impl;

import com.sandy.project.domain.Teacher;
import com.sandy.project.dto.TeacherCreateDTO;
import com.sandy.project.dto.TeacherDetailDTO;
import com.sandy.project.dto.TeacherResponseDTO;
import com.sandy.project.exception.ResourceNotFoundException;
import com.sandy.project.repository.TeacherRepository;
import com.sandy.project.service.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    
    private final TeacherRepository teacherRepository;
    
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
        Teacher teacher = teacherRepository.findBySecureId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        
        TeacherDetailDTO dto = new TeacherDetailDTO();
        dto.setSecureId(teacher.getSecureId());
        dto.setTeacherId(teacher.getId().toString());
        dto.setTeacherName(teacher.getName());
        dto.setTeacherBirthDate(teacher.getBirthDate().toEpochDay());
        dto.setTeacherGender(teacher.getGender());
        dto.setTeacherAddress(teacher.getAddress());
        
        return dto;
    }
    
    @Override
    public TeacherResponseDTO findTeacherById(String id) {
        return null;
    }
}

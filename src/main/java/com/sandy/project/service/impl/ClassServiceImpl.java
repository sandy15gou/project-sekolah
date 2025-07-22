package com.sandy.project.service.impl;

import com.sandy.project.domain.Class;
import com.sandy.project.domain.SchoolClass;
import com.sandy.project.domain.Student;
import com.sandy.project.domain.Teacher;
import com.sandy.project.dto.ClassDetailDTO;
import com.sandy.project.dto.ClassRequestDTO;
import com.sandy.project.dto.ClassRequestDTO;
import com.sandy.project.dto.StudentDetailDTO;
import com.sandy.project.dto.TeacherDetailDTO;
import com.sandy.project.exception.ResourceNotFoundException;
import com.sandy.project.repository.ClassRepository;
import com.sandy.project.repository.StudentRepository;
import com.sandy.project.repository.TeacherRepository;
import com.sandy.project.service.ClassService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClassServiceImpl implements ClassService {
    
    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    
   
    @Override
    public void createNewClass(List<ClassRequestDTO> dtos) {
        List<Class> classes = dtos.stream().map((dtoItem)->{
            Class kelas = new Class();
            kelas.setClassName(dtoItem.getClassName());
            kelas.setAcademicYear(dtoItem.getAcademicYear());
            kelas.setGradeLevel(dtoItem.getGradeLevel());
            kelas.setHomeroomTeacher(teacherRepository.findBySecureId(dtoItem.getHomeroomTeacher())
                    .orElseThrow(() -> new ResourceNotFoundException("Homeroom teacher not found")));
            
            return kelas;
        }).toList();
        classRepository.saveAll(classes);
    
    }
    
    @Override
    public void updateClass(String classId, ClassRequestDTO dto) {
        Class kelas = classRepository.findBySecureId(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not Found"));
        kelas.setClassName(dto.getClassName());
        kelas.setGradeLevel((dto.getGradeLevel()));
        kelas.setAcademicYear(dto.getAcademicYear());
        kelas.setHomeroomTeacher(teacherRepository.findBySecureId(dto.getHomeroomTeacher())
                .orElseThrow(() -> new ResourceNotFoundException("Homeroom teacher not found")));
        classRepository.save(kelas);
        
    
    }
    
    @Override
    public void deleteClass(String classId) {
        Class kelas = classRepository.findBySecureId(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        

        classRepository.delete(kelas);
    
    }
    
    @Override
    public ClassDetailDTO findClassDetail(String classId) {
        Class kelas = classRepository.findBySecureId(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        
        ClassDetailDTO dto = new ClassDetailDTO();
        dto.setSecureId(kelas.getSecureId());
        dto.setClassName(kelas.getClassName());
        dto.setGradeLevel(kelas.getGradeLevel());
        dto.setAcademicYear(kelas.getAcademicYear());
        
        TeacherDetailDTO teacherDto = new TeacherDetailDTO();
        Teacher homeroomTeacher = kelas.getHomeroomTeacher();
        if (homeroomTeacher != null) {
            teacherDto.setSecureId(homeroomTeacher.getSecureId());
            teacherDto.setTeacherId(homeroomTeacher.getId().toString());
            teacherDto.setTeacherName(homeroomTeacher.getName());
            teacherDto.setTeacherBirthDate(homeroomTeacher.getBirthDate().toEpochDay());
            teacherDto.setTeacherGender(homeroomTeacher.getGender());
            teacherDto.setTeacherAddress(homeroomTeacher.getAddress());
            
            // ✅ SET teacher ke class DTO (yang missing!)
            dto.setHomeroomTeacher(teacherDto);
        }
        
        return dto;
    }
    @Override
    public List<ClassDetailDTO> findAllClasses() {
        List<Class> classes = classRepository.findAll();
        return classes.stream().map(kelas -> {
            ClassDetailDTO dto = new ClassDetailDTO();
            dto.setSecureId(kelas.getSecureId());
            dto.setClassName(kelas.getClassName());
            dto.setGradeLevel(kelas.getGradeLevel());
            dto.setAcademicYear(kelas.getAcademicYear());
            
            Teacher homeroomTeacher = kelas.getHomeroomTeacher();
            if (homeroomTeacher != null) {
                TeacherDetailDTO teacherDto = new TeacherDetailDTO();
                teacherDto.setSecureId(homeroomTeacher.getSecureId());
                teacherDto.setTeacherId(homeroomTeacher.getId().toString());
                teacherDto.setTeacherName(homeroomTeacher.getName());
                teacherDto.setTeacherBirthDate(homeroomTeacher.getBirthDate().toEpochDay());
                teacherDto.setTeacherGender(homeroomTeacher.getGender());
                teacherDto.setTeacherAddress(homeroomTeacher.getAddress());
                dto.setHomeroomTeacher(teacherDto);
            }
            
            List<StudentDetailDTO> studentDTOs = kelas.getStudents() != null
                    ? kelas.getStudents().stream().map(student -> {
                StudentDetailDTO studentDto = new StudentDetailDTO();
                studentDto.setSecureId(student.getSecureId());
                studentDto.setStudentId(student.getId().toString());
                studentDto.setStudentName(student.getName());
                studentDto.setStudentBirthDate(student.getBirthDate().toEpochDay());
                studentDto.setStudentGender(student.getGender());
                studentDto.setStudentAddress(student.getAddress());
                return studentDto;
            }).toList()
                    : new ArrayList<>();
            dto.setStudents(studentDTOs);
            
            return dto;
        }).toList();
    }
    @Override
    public void addStudentToClass(String classId, String studentId) {
    
    }
    
    @Override
    public void removeStudentFromClass(String classId, String studentId) {
    
    }
    
    // Implementasi method lainnya
}
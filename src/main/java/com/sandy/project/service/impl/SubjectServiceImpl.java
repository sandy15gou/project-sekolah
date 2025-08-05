package com.sandy.project.service.impl;

import com.sandy.project.domain.Subject;
import com.sandy.project.domain.Teacher;
import com.sandy.project.dto.SubjectDetailDTO;
import com.sandy.project.dto.TeacherDetailDTO;
import com.sandy.project.exception.ResourceNotFoundException;
import com.sandy.project.repository.SubjectRepository;
import com.sandy.project.repository.TeacherRepository;
import com.sandy.project.service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class SubjectServiceImpl implements SubjectService {
    
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    
    @Override
    public SubjectDetailDTO findSubjectDetail(String subjectId) {
        Subject subject = subjectRepository.findBySecureId(subjectId);
        if (subject == null) {
            throw new ResourceNotFoundException("Subject not found");
        }
        return mapToDetailDTO(subject);
    }
    
    @Override
    public List<SubjectDetailDTO> findAllSubjects() {
        return subjectRepository.findAll().stream()
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
}

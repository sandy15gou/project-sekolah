package com.sandy.project.service.impl;

import com.sandy.project.domain.Subject;
import com.sandy.project.domain.Teacher;
import com.sandy.project.dto.SubjectDetailDTO;
import com.sandy.project.repository.SubjectRepository;
import com.sandy.project.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;

    @Override
    public SubjectDetailDTO findSubjectDetail(String subjectId) {
        Subject subject = subjectRepository.findBySecureId(subjectId);
        if (subject == null) {
            throw new RuntimeException("Subject not found");
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
        // eligibleTeachers mapping by secureId (implementasi bisa disesuaikan)
        subjectRepository.save(subject);
    }

    @Override
    public void updateSubject(String subjectId, SubjectDetailDTO dto) {
        Subject subject = subjectRepository.findBySecureId(subjectId);
        if (subject == null) {
            throw new RuntimeException("Subject not found");
        }
        subject.setName(dto.getName());
        subject.setDescription(dto.getDescription());
        // eligibleTeachers mapping by secureId (implementasi bisa disesuaikan)
        subjectRepository.save(subject);
    }

    @Override
    public void deleteSubject(String subjectId) {
        Subject subject = subjectRepository.findBySecureId(subjectId);
        if (subject == null) {
            throw new RuntimeException("Subject not found");
        }
        subjectRepository.delete(subject);
    }

    private SubjectDetailDTO mapToDetailDTO(Subject subject) {
        SubjectDetailDTO dto = new SubjectDetailDTO();
        dto.setName(subject.getName());
        dto.setDescription(subject.getDescription());
        if (subject.getEligibleTeachers() != null) {
            dto.setEligibleTeacherIds(subject.getEligibleTeachers().stream()
                    .map(Teacher::getSecureId)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}


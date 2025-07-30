package com.sandy.project.service;

import com.sandy.project.dto.SubjectDetailDTO;
import java.util.List;

public interface SubjectService {
    SubjectDetailDTO findSubjectDetail(String subjectId);
    List<SubjectDetailDTO> findAllSubjects();
    void createSubject(SubjectDetailDTO dto);
    void updateSubject(String subjectId, SubjectDetailDTO dto);
    void deleteSubject(String subjectId);
}


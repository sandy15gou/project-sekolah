package com.sandy.project.web;

import com.sandy.project.dto.SubjectDetailDTO;
import com.sandy.project.service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@AllArgsConstructor
public class SubjectResource {
    
    private final SubjectService subjectService;
    
    @PostMapping
    public ResponseEntity<String> createSubject(@RequestBody SubjectDetailDTO dto) {
        subjectService.createSubject(dto);
        return ResponseEntity.ok("Subject created successfully");
    }
    
    @GetMapping
    public ResponseEntity<List<SubjectDetailDTO>> getAllSubjects() {
        List<SubjectDetailDTO> subjects = subjectService.findAllSubjects();
        return ResponseEntity.ok(subjects);
    }
    
    @GetMapping("/{subjectId}")
    public ResponseEntity<SubjectDetailDTO> getSubjectDetail(@PathVariable String subjectId) {
        SubjectDetailDTO subjectDetail = subjectService.findSubjectDetail(subjectId);
        return ResponseEntity.ok(subjectDetail);
    }
    
    @PutMapping("/{subjectId}")
    public ResponseEntity<String> updateSubject(@PathVariable String subjectId, @RequestBody SubjectDetailDTO dto) {
        subjectService.updateSubject(subjectId, dto);
        return ResponseEntity.ok("Subject updated successfully");
    }
    
    @DeleteMapping("/{subjectId}")
    public ResponseEntity<String> deleteSubject(@PathVariable String subjectId) {
        subjectService.deleteSubject(subjectId);
        return ResponseEntity.ok("Subject deleted successfully");
    }
    
    @PostMapping("/{subjectId}/teachers/{teacherId}")
    public ResponseEntity<String> addEligibleTeacher(@PathVariable String subjectId, @PathVariable String teacherId) {
        subjectService.addEligibleTeacher(subjectId, teacherId);
        return ResponseEntity.ok("Teacher added to subject successfully");
    }
    
    @DeleteMapping("/{subjectId}/teachers/{teacherId}")
    public ResponseEntity<String> removeEligibleTeacher(@PathVariable String subjectId, @PathVariable String teacherId) {
        subjectService.removeEligibleTeacher(subjectId, teacherId);
        return ResponseEntity.ok("Teacher removed from subject successfully");
    }
}

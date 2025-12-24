package com.sandy.project.web;

import com.sandy.project.dto.ClassDetailDTO;
import com.sandy.project.dto.ClassRequestDTO;
import com.sandy.project.service.ClassService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
@RestController
@Validated
public class ClassResource {
    private static final Logger logger = LoggerFactory.getLogger(ClassResource.class);
    
    private final ClassService classService;
    
    @PostMapping("/v1/classes")
    public ResponseEntity<Void> createClass(@RequestBody @Valid List<ClassRequestDTO> dtos) {
        classService.createNewClass(dtos);
        return ResponseEntity.created(URI.create("/v1/classes")).build();
    }
    
    @GetMapping("/v1/classes")
    public ResponseEntity<List<ClassDetailDTO>> getAllClasses() {
        List<ClassDetailDTO> classes = classService.findAllClasses();
        return ResponseEntity.ok(classes);
    }
    
    @GetMapping("/v1/classes/{classId}")
    public ResponseEntity<ClassDetailDTO> getClassDetail(@PathVariable String classId) {
        ClassDetailDTO classDetail = classService.findClassDetail(classId);
        return ResponseEntity.ok(classDetail);
    }
    
    @PutMapping("/v1/classes/{classId}")
    public ResponseEntity<Void> updateClass(@PathVariable String classId,
                                            @RequestBody @Valid ClassRequestDTO dto) {
        classService.updateClass(classId, dto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/v1/classes/{classId}")
    public ResponseEntity<Void> deleteClass(@PathVariable String classId) {
        classService.deleteClass(classId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/v1/classes/{classId}/students/{studentId}")
    public ResponseEntity<Void> addStudentToClass(@PathVariable String classId,
                                                  @PathVariable String studentId) {
        logger.info("Received request to add student {} to class {}", studentId, classId);
        classService.addStudentToClass(classId, studentId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/v1/classes/{classId}/students/{studentId}")
    public ResponseEntity<Void> removeStudentFromClass(@PathVariable String classId,
                                                       @PathVariable String studentId) {
        classService.removeStudentFromClass(classId, studentId);
        return ResponseEntity.ok().build();
    }
}
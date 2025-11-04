package com.sandy.project.web;

import com.sandy.project.dto.ClassDetailDTO;
import com.sandy.project.dto.ClassRequestDTO;
import com.sandy.project.service.ClassService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@AllArgsConstructor
public class ClassResource {
    
    private final ClassService classService;
    
    @PostMapping
    public ResponseEntity<String> createClass(@RequestBody List<ClassRequestDTO> dtos) {
        classService.createNewClass(dtos);
        return ResponseEntity.ok("Classes created successfully");
    }
    
    @GetMapping
    public ResponseEntity<List<ClassDetailDTO>> getAllClasses() {
        List<ClassDetailDTO> classes = classService.findAllClasses();
        return ResponseEntity.ok(classes);
    }
    
    @GetMapping("/{classId}")
    public ResponseEntity<ClassDetailDTO> getClassDetail(@PathVariable String classId) {
        ClassDetailDTO classDetail = classService.findClassDetail(classId);
        return ResponseEntity.ok(classDetail);
    }
    
    @PutMapping("/{classId}")
    public ResponseEntity<String> updateClass(@PathVariable String classId, @RequestBody ClassRequestDTO dto) {
        classService.updateClass(classId, dto);
        return ResponseEntity.ok("Class updated successfully");
    }
    
    @DeleteMapping("/{classId}")
    public ResponseEntity<String> deleteClass(@PathVariable String classId) {
        classService.deleteClass(classId);
        return ResponseEntity.ok("Class deleted successfully");
    }
    
    @PostMapping("/{classId}/students/{studentId}")
    public ResponseEntity<String> addStudentToClass(@PathVariable String classId, @PathVariable String studentId) {
        classService.addStudentToClass(classId, studentId);
        return ResponseEntity.ok("Student added to class successfully");
    }
    
    @DeleteMapping("/{classId}/students/{studentId}")
    public ResponseEntity<String> removeStudentFromClass(@PathVariable String classId, @PathVariable String studentId) {
        classService.removeStudentFromClass(classId, studentId);
        return ResponseEntity.ok("Student removed from class successfully");
    }
}

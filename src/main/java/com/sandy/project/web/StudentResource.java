package com.sandy.project.web;

import java.net.URI;
import java.util.List;

import com.sandy.project.dto.StudentDetailDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sandy.project.dto.StudentCreateDTO;
import com.sandy.project.dto.StudentResponseDTO;
import com.sandy.project.dto.StudentUpdateDTO;
import com.sandy.project.service.StudentService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@Validated
public class StudentResource {
    
    private final StudentService studentService;
    
    @PostMapping("/v1/student")
    public ResponseEntity<Void> createNewStudent(@RequestBody @Valid List<StudentCreateDTO> dto){
        studentService.createNewStudent(dto);
        return ResponseEntity.created(URI.create("/student")).build();
    }
    
    @GetMapping("/v1/student/{id}")
    public ResponseEntity<StudentDetailDTO> findStudentDetail(@PathVariable String id) {
        StudentDetailDTO dto = studentService.findStudentDetail(id);
        return ResponseEntity.ok(dto);
    }
    
    @PutMapping("/v1/student/{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable String id,
                                              @RequestBody @Valid StudentUpdateDTO dto) {
        studentService.updateStudent(id, dto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/v1/student/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
}
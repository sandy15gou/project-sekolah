package com.sandy.project.web;

import com.sandy.project.dto.TeacherCreateDTO;
import com.sandy.project.dto.TeacherDetailDTO;
import com.sandy.project.service.TeacherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@AllArgsConstructor
@RestController
@Validated
public class TeacherResource {
    private final TeacherService teacherService;
    
    @PostMapping("/v1/teacher")
    public ResponseEntity<Void> createNewTeacher(@RequestBody @Valid List<TeacherCreateDTO> dto){
        teacherService.createNewTeacher(dto);
        return ResponseEntity.created(URI.create("/teacher")).build();
    }
    
    @PutMapping("/v1/teacher/{id}")
    public ResponseEntity<Void> updateTeacher(@PathVariable String id, @RequestBody @Valid TeacherCreateDTO dto) {
        teacherService.updateTeacher(id, dto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/v1/teacher/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable String id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/v1/teacher/{id}")
    public ResponseEntity<TeacherDetailDTO> findTeacherDetail(@PathVariable String id) {
        TeacherDetailDTO dto = teacherService.findTeacherDetail(id);
        return ResponseEntity.ok(dto);
    }
}
    
// Note: The above code assumes that the TeacherService and related DTOs are implemented correctly.

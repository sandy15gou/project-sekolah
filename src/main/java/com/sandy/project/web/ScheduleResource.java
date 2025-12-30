package com.sandy.project.web;

import com.sandy.project.dto.ScheduleDetailDTO;
import com.sandy.project.service.ScheduleService;
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
public class ScheduleResource {
    
    private final ScheduleService scheduleService;
    
    
    @PostMapping("/v1/schedules")
    public ResponseEntity<Void> createSchedule(@RequestBody @Valid ScheduleDetailDTO dto) {
        scheduleService.createSchedule(dto);
        return ResponseEntity.created(URI.create("/v1/schedules")).build();
    }
    
  
    @GetMapping("/v1/schedules")
    public ResponseEntity<List<ScheduleDetailDTO>> getAllSchedules() {
        List<ScheduleDetailDTO> schedules = scheduleService.findAllSchedules();
        return ResponseEntity.ok(schedules);
    }
    
 
    @GetMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<ScheduleDetailDTO> getScheduleDetail(@PathVariable String scheduleId) {
        ScheduleDetailDTO schedule = scheduleService.findScheduleDetail(scheduleId);
        return ResponseEntity.ok(schedule);
    }
    

    @PutMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<Void> updateSchedule(
            @PathVariable String scheduleId,
            @RequestBody @Valid ScheduleDetailDTO dto) {
        scheduleService.updateSchedule(scheduleId, dto);
        return ResponseEntity.ok().build();
    }
    

    @DeleteMapping("/v1/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.ok().build();
    }
}
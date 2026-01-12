package com.sandy.project.service.impl;

import com.sandy.project.domain.Schedule;
import com.sandy.project.domain.Class;
import com.sandy.project.domain.Subject;
import com.sandy.project.domain.Teacher;
import com.sandy.project.dto.*;
import com.sandy.project.exception.ResourceNotFoundException;
import com.sandy.project.repository.ScheduleRepository;
import com.sandy.project.repository.ClassRepository;
import com.sandy.project.repository.SubjectRepository;
import com.sandy.project.repository.TeacherRepository;
import com.sandy.project.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    
    // Whitelist field yang boleh di-sort (security measure)
    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList("day", "startTime", "semester", "createdAt");
    private static final int MAX_PAGE_SIZE = 50;

    @Override
    public ScheduleDetailDTO findScheduleDetail(String scheduleId) {
        Schedule schedule = scheduleRepository.findBySecureId(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        return mapToDetailDTO(schedule);
    }

    @Override
    public List<ScheduleDetailDTO> findAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::mapToDetailDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void createSchedule(ScheduleDetailDTO dto) {
        Schedule schedule = new Schedule();
        schedule.setDay(dto.getDay());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setSemester(dto.getSemester());
        if (dto.getSchoolClass() != null) {
            Class clazz = classRepository.findBySecureId(dto.getSchoolClass().getSecureId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
            schedule.setClazz(clazz);
        }
        if (dto.getSubject() != null) {
            Subject subject = subjectRepository.findBySecureId(dto.getSubject().getSecureId());
            if (subject == null) throw new ResourceNotFoundException("Subject not found");
            schedule.setSubject(subject);
        }
        if (dto.getTeacher() != null) {
            Teacher teacher = teacherRepository.findBySecureId(dto.getTeacher().getSecureId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
            schedule.setTeacher(teacher);
        }
        scheduleRepository.save(schedule);
    }

    @Override
    public void updateSchedule(String scheduleId, ScheduleDetailDTO dto) {
        Schedule schedule = scheduleRepository.findBySecureId(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        schedule.setDay(dto.getDay());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setSemester(dto.getSemester());
        if (dto.getSchoolClass() != null) {
            Class clazz = classRepository.findBySecureId(dto.getSchoolClass().getSecureId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
            schedule.setClazz(clazz);
        }
        if (dto.getSubject() != null) {
            Subject subject = subjectRepository.findBySecureId(dto.getSubject().getSecureId());
            if (subject == null) throw new ResourceNotFoundException("Subject not found");
            schedule.setSubject(subject);
        }
        if (dto.getTeacher() != null) {
            Teacher teacher = teacherRepository.findBySecureId(dto.getTeacher().getSecureId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
            schedule.setTeacher(teacher);
        }
        scheduleRepository.save(schedule);
    }

    @Override
    public void deleteSchedule(String scheduleId) {
        Schedule schedule = scheduleRepository.findBySecureId(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        scheduleRepository.delete(schedule);
    }
    
    // ========== PAGINATION METHODS ==========
    
    @Override
    public PagedResponseDTO<ScheduleResponseDTO> findAllSchedulesPaged(int page, int size, String sortBy, String sortDirection) {
        // Validasi dan buat Pageable
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        
        // Query ke database
        Page<Schedule> schedulePage = scheduleRepository.findByDeletedFalse(pageable);
        
        // Convert ke DTO
        Page<ScheduleResponseDTO> dtoPage = schedulePage.map(this::mapToResponseDTO);
        
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<ScheduleResponseDTO> searchSchedulesByDayPaged(String day, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Schedule> schedulePage = scheduleRepository.findByDayContainingIgnoreCaseAndDeletedFalse(day, pageable);
        Page<ScheduleResponseDTO> dtoPage = schedulePage.map(this::mapToResponseDTO);
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<ScheduleResponseDTO> searchSchedulesBySemesterPaged(String semester, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Schedule> schedulePage = scheduleRepository.findBySemesterContainingIgnoreCaseAndDeletedFalse(semester, pageable);
        Page<ScheduleResponseDTO> dtoPage = schedulePage.map(this::mapToResponseDTO);
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<ScheduleResponseDTO> searchSchedulesByClassPaged(String classSecureId, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Schedule> schedulePage = scheduleRepository.findByClazz_SecureIdAndDeletedFalse(classSecureId, pageable);
        Page<ScheduleResponseDTO> dtoPage = schedulePage.map(this::mapToResponseDTO);
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<ScheduleResponseDTO> searchSchedulesBySubjectPaged(String subjectSecureId, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Schedule> schedulePage = scheduleRepository.findBySubject_SecureIdAndDeletedFalse(subjectSecureId, pageable);
        Page<ScheduleResponseDTO> dtoPage = schedulePage.map(this::mapToResponseDTO);
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<ScheduleResponseDTO> searchSchedulesByTeacherPaged(String teacherSecureId, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Schedule> schedulePage = scheduleRepository.findByTeacher_SecureIdAndDeletedFalse(teacherSecureId, pageable);
        Page<ScheduleResponseDTO> dtoPage = schedulePage.map(this::mapToResponseDTO);
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<ScheduleResponseDTO> searchSchedulesByDayAndSemesterPaged(String day, String semester, int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Schedule> schedulePage = scheduleRepository.findByDayContainingIgnoreCaseAndSemesterContainingIgnoreCaseAndDeletedFalse(
                day, semester, pageable);
        Page<ScheduleResponseDTO> dtoPage = schedulePage.map(this::mapToResponseDTO);
        return new PagedResponseDTO<>(dtoPage);
    }
    
    // ========== HELPER METHODS ==========
    
    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        // Validasi size
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        
        // Validasi sortBy
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "day"; // Default ke day
        }
        
        // Validasi direction
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
    
    private ScheduleResponseDTO mapToResponseDTO(Schedule schedule) {
        ScheduleResponseDTO dto = new ScheduleResponseDTO();
        dto.setSecureId(schedule.getSecureId());
        dto.setDay(schedule.getDay());
        dto.setTime(schedule.getStartTime() + " - " + schedule.getEndTime());
        
        if (schedule.getClazz() != null) {
            dto.setClassName(schedule.getClazz().getClassName());
        }
        if (schedule.getSubject() != null) {
            dto.setSubjectName(schedule.getSubject().getName());
        }
        if (schedule.getTeacher() != null) {
            dto.setTeacherName(schedule.getTeacher().getName());
        }
        
        return dto;
    }

    private ScheduleDetailDTO mapToDetailDTO(Schedule schedule) {
        ScheduleDetailDTO dto = new ScheduleDetailDTO();
        dto.setSecureId(schedule.getSecureId());
        dto.setDay(schedule.getDay());
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());
        dto.setSemester(schedule.getSemester());
        if (schedule.getClazz() != null) {
            ClassDetailDTO classDto = new ClassDetailDTO();
            classDto.setSecureId(schedule.getClazz().getSecureId());
            classDto.setClassName(schedule.getClazz().getClassName());
            classDto.setGradeLevel(schedule.getClazz().getGradeLevel());
            classDto.setAcademicYear(schedule.getClazz().getAcademicYear());
            // Tidak perlu set schedules, students, subjects untuk mencegah recursive/loop
            dto.setSchoolClass(classDto);
        }
        if (schedule.getSubject() != null) {
            SubjectResponseDTO subjectDto = new SubjectResponseDTO();
            subjectDto.setSecureId(schedule.getSubject().getSecureId());
            subjectDto.setName(schedule.getSubject().getName());
            dto.setSubject(subjectDto);
        }
        if (schedule.getTeacher() != null) {
            TeacherDetailDTO teacherDto = new TeacherDetailDTO();
            teacherDto.setSecureId(schedule.getTeacher().getSecureId());
            teacherDto.setTeacherName(schedule.getTeacher().getName());
            teacherDto.setTeacherBirthDate(schedule.getTeacher().getBirthDate() != null ? schedule.getTeacher().getBirthDate().toEpochDay() : null);
            teacherDto.setTeacherGender(schedule.getTeacher().getGender());
            teacherDto.setTeacherAddress(schedule.getTeacher().getAddress());
            dto.setTeacher(teacherDto);
        }
        return dto;
    }
}

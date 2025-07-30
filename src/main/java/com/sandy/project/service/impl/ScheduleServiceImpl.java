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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

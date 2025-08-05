package com.sandy.project.service.impl;

import com.sandy.project.domain.Class;
import com.sandy.project.domain.Student;
import com.sandy.project.domain.Teacher;
import com.sandy.project.domain.Schedule;
import com.sandy.project.domain.Subject;
import com.sandy.project.dto.ClassDetailDTO;
import com.sandy.project.dto.ClassRequestDTO;
import com.sandy.project.dto.StudentDetailDTO;
import com.sandy.project.dto.TeacherDetailDTO;
import com.sandy.project.dto.ScheduleDetailDTO;
import com.sandy.project.dto.SubjectDetailDTO;
import com.sandy.project.dto.SubjectResponseDTO;
import com.sandy.project.exception.ResourceNotFoundException;
import com.sandy.project.repository.ClassRepository;
import com.sandy.project.repository.StudentRepository;
import com.sandy.project.repository.TeacherRepository;
import com.sandy.project.repository.ScheduleRepository;
import com.sandy.project.repository.SubjectRepository;
import com.sandy.project.service.ClassService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ClassServiceImpl implements ClassService {
    
    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;
    private final SubjectRepository subjectRepository;
    
    
    @Override
    public void createNewClass(List<ClassRequestDTO> dtos) {
        List<Class> classes = dtos.stream().map((dtoItem) -> {
            Class kelas = new Class();
            kelas.setClassName(dtoItem.getClassName());
            kelas.setAcademicYear(dtoItem.getAcademicYear());
            kelas.setGradeLevel(dtoItem.getGradeLevel());
            kelas.setHomeroomTeacher(teacherRepository.findBySecureId(dtoItem.getHomeroomTeacher())
                    .orElseThrow(() -> new ResourceNotFoundException("Homeroom teacher not found")));
            
            return kelas;
        }).toList();
        classRepository.saveAll(classes);
        
    }
    
    @Override
    public void updateClass(String classId, ClassRequestDTO dto) {
        Class kelas = classRepository.findBySecureId(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not Found"));
        kelas.setClassName(dto.getClassName());
        kelas.setGradeLevel((dto.getGradeLevel()));
        kelas.setAcademicYear(dto.getAcademicYear());
        kelas.setHomeroomTeacher(teacherRepository.findBySecureId(dto.getHomeroomTeacher())
                .orElseThrow(() -> new ResourceNotFoundException("Homeroom teacher not found")));
        classRepository.save(kelas);
        
        
    }
    
    @Override
    public void deleteClass(String classId) {
        Class kelas = classRepository.findBySecureId(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        
        
        classRepository.delete(kelas);
        
    }
    
    @Override
    public ClassDetailDTO findClassDetail(String classId) {
        Class kelas = classRepository.findBySecureId(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        
        ClassDetailDTO dto = new ClassDetailDTO();
        dto.setSecureId(kelas.getSecureId());
        dto.setClassName(kelas.getClassName());
        dto.setGradeLevel(kelas.getGradeLevel());
        dto.setAcademicYear(kelas.getAcademicYear());
        dto.setMaxCapacity(kelas.getMaxCapacity());
        dto.setDescription(kelas.getDescription());
        
        // Homeroom Teacher
        TeacherDetailDTO teacherDto = new TeacherDetailDTO();
        Teacher homeroomTeacher = kelas.getHomeroomTeacher();
        if (homeroomTeacher != null) {
            teacherDto.setSecureId(homeroomTeacher.getSecureId());
            teacherDto.setTeacherId(homeroomTeacher.getId().toString());
            teacherDto.setTeacherName(homeroomTeacher.getName());
            teacherDto.setTeacherBirthDate(homeroomTeacher.getBirthDate() != null ? homeroomTeacher.getBirthDate().toEpochDay() : null);
            teacherDto.setTeacherGender(homeroomTeacher.getGender());
            teacherDto.setTeacherAddress(homeroomTeacher.getAddress());
            dto.setHomeroomTeacher(teacherDto);
        }
        
        // Students
        List<StudentDetailDTO> studentDTOs = kelas.getStudents() != null
                ? kelas.getStudents().stream().map(student -> {
            StudentDetailDTO studentDto = new StudentDetailDTO();
            studentDto.setSecureId(student.getSecureId());
            studentDto.setStudentId(student.getId().toString());
            studentDto.setStudentName(student.getName());
            studentDto.setStudentBirthDate(student.getBirthDate() != null ? student.getBirthDate().toEpochDay() : null);
            studentDto.setStudentGender(student.getGender());
            studentDto.setStudentAddress(student.getAddress());
            return studentDto;
        }).toList() : new ArrayList<>();
        dto.setStudents(studentDTOs);
        
        // Schedules
        List<Schedule> schedules = scheduleRepository.findByClazz_SecureId(classId);
        List<ScheduleDetailDTO> scheduleDTOs = schedules.stream().map(schedule -> {
            ScheduleDetailDTO scheduleDto = new ScheduleDetailDTO();
            scheduleDto.setSecureId(schedule.getSecureId());
            scheduleDto.setDay(schedule.getDay());
            scheduleDto.setStartTime(schedule.getStartTime());
            scheduleDto.setEndTime(schedule.getEndTime());
            scheduleDto.setSemester(schedule.getSemester());
            
            // Subject
            if (schedule.getSubject() != null) {
                SubjectResponseDTO subjectDto = new SubjectResponseDTO();
                subjectDto.setSecureId(schedule.getSubject().getSecureId());
                subjectDto.setName(schedule.getSubject().getName());
                scheduleDto.setSubject(subjectDto);
            }
            
            // Teacher
            if (schedule.getTeacher() != null) {
                TeacherDetailDTO tDto = new TeacherDetailDTO();
                tDto.setSecureId(schedule.getTeacher().getSecureId());
                tDto.setTeacherId(schedule.getTeacher().getId().toString());
                tDto.setTeacherName(schedule.getTeacher().getName());
                tDto.setTeacherBirthDate(schedule.getTeacher().getBirthDate() != null ? schedule.getTeacher().getBirthDate().toEpochDay() : null);
                tDto.setTeacherGender(schedule.getTeacher().getGender());
                tDto.setTeacherAddress(schedule.getTeacher().getAddress());
                scheduleDto.setTeacher(tDto);
            }
            
            // Class (gunakan ClassDetailDTO sesuai field schoolClass di ScheduleDetailDTO)
            if (schedule.getClazz() != null) {
                ClassDetailDTO classDetailDto = new ClassDetailDTO();
                classDetailDto.setSecureId(schedule.getClazz().getSecureId());
                classDetailDto.setClassName(schedule.getClazz().getClassName());
                classDetailDto.setGradeLevel(schedule.getClazz().getGradeLevel());
                classDetailDto.setAcademicYear(schedule.getClazz().getAcademicYear());
                // Tidak perlu set schedules, students, subjects untuk mencegah recursive/loop
                scheduleDto.setSchoolClass(classDetailDto);
            }
            return scheduleDto;
        }).toList();
        dto.setSchedules(scheduleDTOs);
        
        // Subjects (unique from schedules)
        List<SubjectDetailDTO> subjectDTOs = schedules.stream()
                .map(Schedule::getSubject)
                .filter(subject -> subject != null)
                .distinct()
                .map(subject -> {
                    SubjectDetailDTO subjectDto = new SubjectDetailDTO();
                    subjectDto.setSecureId(subject.getSecureId());
                    subjectDto.setName(subject.getName());
                    subjectDto.setDescription(subject.getDescription());
                    
                    // Eligible teachers for this subject
                    if (subject.getEligibleTeachers() != null) {
                        List<TeacherDetailDTO> eligibleTeacherDTOs = subject.getEligibleTeachers().stream()
                                .map(teacher -> {
                                    TeacherDetailDTO tDto = new TeacherDetailDTO();
                                    tDto.setSecureId(teacher.getSecureId());
                                    tDto.setTeacherId(teacher.getId().toString());
                                    tDto.setTeacherName(teacher.getName());
                                    tDto.setTeacherBirthDate(teacher.getBirthDate() != null ? teacher.getBirthDate().toEpochDay() : null);
                                    tDto.setTeacherGender(teacher.getGender());
                                    tDto.setTeacherAddress(teacher.getAddress());
                                    return tDto;
                                }).toList();
                        subjectDto.setEligibleTeachers(eligibleTeacherDTOs);
                    }
                    return subjectDto;
                }).toList();
        dto.setSubjects(subjectDTOs);
        
        // All Teachers teaching in this class (unique from schedules)
        List<TeacherDetailDTO> teacherDTOs = schedules.stream()
                .map(Schedule::getTeacher)
                .filter(teacher -> teacher != null)
                .distinct()
                .map(teacher -> {
                    TeacherDetailDTO tDto = new TeacherDetailDTO();
                    tDto.setSecureId(teacher.getSecureId());
                    tDto.setTeacherId(teacher.getId().toString());
                    tDto.setTeacherName(teacher.getName());
                    tDto.setTeacherBirthDate(teacher.getBirthDate() != null ? teacher.getBirthDate().toEpochDay() : null);
                    tDto.setTeacherGender(teacher.getGender());
                    tDto.setTeacherAddress(teacher.getAddress());
                    return tDto;
                }).collect(Collectors.toList());
        
        // Add homeroom teacher to the list if not already present
        if (homeroomTeacher != null) {
            boolean homeroomAlreadyInList = teacherDTOs.stream()
                    .anyMatch(t -> t.getSecureId().equals(homeroomTeacher.getSecureId()));
            if (!homeroomAlreadyInList) {
                teacherDTOs.add(teacherDto);
            }
        }
        dto.setTeachers(teacherDTOs);
        
        // Statistics
        dto.setCurrentStudentCount(studentDTOs.size());
        dto.setTotalSubjects(subjectDTOs.size());
        dto.setTotalTeachers(teacherDTOs.size());
        
        return dto;
    }
    
    @Override
    public List<ClassDetailDTO> findAllClasses() {
        List<Class> classes = classRepository.findAll();
        return classes.stream().map(kelas -> {
            ClassDetailDTO dto = new ClassDetailDTO();
            dto.setSecureId(kelas.getSecureId());
            dto.setClassName(kelas.getClassName());
            dto.setGradeLevel(kelas.getGradeLevel());
            dto.setAcademicYear(kelas.getAcademicYear());
            
            Teacher homeroomTeacher = kelas.getHomeroomTeacher();
            if (homeroomTeacher != null) {
                TeacherDetailDTO teacherDto = new TeacherDetailDTO();
                teacherDto.setSecureId(homeroomTeacher.getSecureId());
                teacherDto.setTeacherId(homeroomTeacher.getId().toString());
                teacherDto.setTeacherName(homeroomTeacher.getName());
                teacherDto.setTeacherBirthDate(homeroomTeacher.getBirthDate().toEpochDay());
                teacherDto.setTeacherGender(homeroomTeacher.getGender());
                teacherDto.setTeacherAddress(homeroomTeacher.getAddress());
                dto.setHomeroomTeacher(teacherDto);
            }
            
            List<StudentDetailDTO> studentDTOs = kelas.getStudents() != null
                    ? kelas.getStudents().stream().map(student -> {
                StudentDetailDTO studentDto = new StudentDetailDTO();
                studentDto.setSecureId(student.getSecureId());
                studentDto.setStudentId(student.getId().toString());
                studentDto.setStudentName(student.getName());
                studentDto.setStudentBirthDate(student.getBirthDate().toEpochDay());
                studentDto.setStudentGender(student.getGender());
                studentDto.setStudentAddress(student.getAddress());
                return studentDto;
            }).toList()
                    : new ArrayList<>();
            dto.setStudents(studentDTOs);
            
            return dto;
        }).toList();
    }
    
    @Override
    public void addStudentToClass(String classId, String studentId) {
        // Cari class berdasarkan secureId
        Class kelas = classRepository.findBySecureId(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        
        // Cari student berdasarkan secureId
        Student student = studentRepository.findBySecureId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        
        // Inisialisasi list students jika null
        if (kelas.getStudents() == null) {
            kelas.setStudents(new ArrayList<>());
        }
        
        // Cek apakah student sudah ada di class (avoid duplicate)
        boolean studentExists = kelas.getStudents().stream()
                .anyMatch(s -> s.getSecureId().equals(studentId));
        
        if (!studentExists) {
            // Tambahkan student ke class
            kelas.getStudents().add(student);
            // Save class - Hibernate akan handle junction table secara otomatis
            classRepository.save(kelas);
        }
    }
    
    @Override
    public void removeStudentFromClass(String classId, String studentId) {
        // Cari class berdasarkan secureId
        Class kelas = classRepository.findBySecureId(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        
        // Tidak perlu mencari student secara eksplisit, cukup remove dari collection
        if (kelas.getStudents() != null) {
            // Remove student dari class berdasarkan secureId
            boolean removed = kelas.getStudents().removeIf(s -> s.getSecureId().equals(studentId));
            
            // Save class - Hibernate akan handle junction table secara otomatis
            if (removed) {
                classRepository.save(kelas);
            }
        }
    }
}
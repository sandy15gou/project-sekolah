package com.sandy.project.service.impl;

import com.sandy.project.domain.Class;
import com.sandy.project.domain.Student;
import com.sandy.project.domain.Teacher;
import com.sandy.project.domain.Schedule;
import com.sandy.project.domain.Subject;
import com.sandy.project.dto.*;
import com.sandy.project.dto.query.ClassQueryDTO;
import com.sandy.project.exception.ResourceNotFoundException;
import com.sandy.project.repository.ClassRepository;
import com.sandy.project.repository.StudentRepository;
import com.sandy.project.repository.TeacherRepository;
import com.sandy.project.repository.ScheduleRepository;
import com.sandy.project.repository.SubjectRepository;
import com.sandy.project.service.ClassService;
import com.sandy.project.specification.ClassSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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
    
    private static final List<String> ALLOWED_SORT_FIELDS =
            Arrays.asList("className", "gradeLevel", "academicYear", "createdAt");
    
    private static final int MAX_PAGE_SIZE = 50;
    
    
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
        // OPTIMASI: Gunakan JPA Projection untuk mendapatkan Class + Teacher dalam 1 query
        // Menghindari N+1 problem dari lazy loading homeroomTeacher
        ClassQueryDTO queryDTO = classRepository.findClassQueryDTOBySecureId(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        
        // Masih perlu query entity lengkap untuk mendapat students (ManyToMany)
        Class kelas = classRepository.findBySecureId(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        
        ClassDetailDTO dto = new ClassDetailDTO();
        // Ambil data dari QueryDTO (sudah ter-JOIN, tidak trigger lazy loading)
        dto.setSecureId(queryDTO.secureId());
        dto.setClassName(queryDTO.className());
        dto.setGradeLevel(queryDTO.gradeLevel());
        dto.setAcademicYear(queryDTO.academicYear());
        dto.setMaxCapacity(queryDTO.maxCapacity());
        dto.setDescription(queryDTO.description());
        
        // Homeroom Teacher dari QueryDTO (tidak perlu akses lazy field)
        if (queryDTO.homeroomTeacherSecureId() != null) {
            TeacherDetailDTO teacherDto = new TeacherDetailDTO();
            teacherDto.setSecureId(queryDTO.homeroomTeacherSecureId());
            teacherDto.setTeacherName(queryDTO.homeroomTeacherName());
            // Jika butuh data lengkap teacher, harus query terpisah atau buat TeacherQueryDTO
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
        if (queryDTO.homeroomTeacherSecureId() != null) {
            boolean homeroomAlreadyInList = teacherDTOs.stream()
                    .anyMatch(t -> t.getSecureId().equals(queryDTO.homeroomTeacherSecureId()));
            if (!homeroomAlreadyInList && dto.getHomeroomTeacher() != null) {
                teacherDTOs.add(dto.getHomeroomTeacher());
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
        // OPTIMASI: Gunakan JPA Projection untuk menghindari N+1 problem
        // Mengambil Class + Teacher dalam 1 query JOIN
        List<ClassQueryDTO> queryDTOs = classRepository.findAllClassQueryDTO();
        
        return queryDTOs.stream().map(queryDTO -> {
            ClassDetailDTO dto = new ClassDetailDTO();
            dto.setSecureId(queryDTO.secureId());
            dto.setClassName(queryDTO.className());
            dto.setGradeLevel(queryDTO.gradeLevel());
            dto.setAcademicYear(queryDTO.academicYear());
            dto.setMaxCapacity(queryDTO.maxCapacity());
            dto.setDescription(queryDTO.description());
            
            // Homeroom Teacher dari QueryDTO (tidak trigger lazy loading)
            if (queryDTO.homeroomTeacherSecureId() != null) {
                TeacherDetailDTO teacherDto = new TeacherDetailDTO();
                teacherDto.setSecureId(queryDTO.homeroomTeacherSecureId());
                teacherDto.setTeacherName(queryDTO.homeroomTeacherName());
                dto.setHomeroomTeacher(teacherDto);
            }
            
            // NOTE: Jika butuh students, harus query terpisah atau gunakan method lain
            // Untuk list view, biasanya tidak perlu students detail
            dto.setStudents(new ArrayList<>());
            
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
    
    @Override
    public PagedResponseDTO<ClassResponseDTO> findAllClassesPaged(int page, int size, String sortBy, String sortDirection) {
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "createdAt"; // Default ke createdAt kalau field tidak valid
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        // Buat Pageable object
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Class> classPage = classRepository.findByDeletedFalse(pageable);
        Page<ClassResponseDTO> dtoPage = classPage.map(kelas ->{
            ClassResponseDTO dto = new ClassResponseDTO();
            dto.setSecureId(kelas.getSecureId());
            dto.setClassName(kelas.getClassName());
            dto.setGradeLevel(kelas.getGradeLevel());
            dto.setAcademicYear(kelas.getAcademicYear());
            return dto;
        });
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<ClassResponseDTO> searchClassesByClassNamePaged(String className, int page, int size, String sortBy, String sortDirection) {
        // Validasi size
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        
        // Validasi sortBy
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "className";
        }
        
        // Validasi direction
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        // Buat Pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        // Query search by className
        Page<Class> classPage = classRepository.findByClassNameContainingAndDeletedFalse(className, pageable);
        
        // Convert ke DTO
        Page<ClassResponseDTO> dtoPage = classPage.map(kelas -> {
            ClassResponseDTO dto = new ClassResponseDTO();
            dto.setSecureId(kelas.getSecureId());
            dto.setClassName(kelas.getClassName());
            dto.setGradeLevel(kelas.getGradeLevel());
            dto.setAcademicYear(kelas.getAcademicYear());
            return dto;
        });
        
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<ClassResponseDTO> searchClassesByAcademicYearPaged(String academicYear, int page, int size, String sortBy, String sortDirection) {
        // Validasi size
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        
        // Validasi sortBy
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "academicYear";
        }
        
        // Validasi direction
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        // Buat Pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        // Query search by academicYear
        Page<Class> classPage = classRepository.findByAcademicYearContainingAndDeletedFalse(academicYear, pageable);
        
        // Convert ke DTO
        Page<ClassResponseDTO> dtoPage = classPage.map(kelas -> {
            ClassResponseDTO dto = new ClassResponseDTO();
            dto.setSecureId(kelas.getSecureId());
            dto.setClassName(kelas.getClassName());
            dto.setGradeLevel(kelas.getGradeLevel());
            dto.setAcademicYear(kelas.getAcademicYear());
            return dto;
        });
        
        return new PagedResponseDTO<>(dtoPage);
    }
    
    @Override
    public PagedResponseDTO<ClassResponseDTO> searchClassesByGradeLevelPaged(String gradeLevel, int page, int size, String sortBy, String sortDirection) {
        // Validasi size
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        
        // Validasi sortBy
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "gradeLevel";
        }
        
        // Validasi direction
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        // Buat Pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        // Query search by gradeLevel
        Page<Class> classPage = classRepository.findByGradeLevelContainingAndDeletedFalse(gradeLevel, pageable);
        
        // Convert ke DTO
        Page<ClassResponseDTO> dtoPage = classPage.map(kelas -> {
            ClassResponseDTO dto = new ClassResponseDTO();
            dto.setSecureId(kelas.getSecureId());
            dto.setClassName(kelas.getClassName());
            dto.setGradeLevel(kelas.getGradeLevel());
            dto.setAcademicYear(kelas.getAcademicYear());
            return dto;
        });
        
        return new PagedResponseDTO<>(dtoPage);
    }
    
    /**
     * Filter classes dengan multiple criteria menggunakan Specification
     *
     * Flow:
     * 1. Validasi input (size, sortBy, sortDirection)
     * 2. Build Pageable object
     * 3. Build Specification dari ClassFilterDTO
     * 4. Execute query dengan findAll(spec, pageable)
     * 5. Convert hasil ke DTO
     * 6. Return PagedResponseDTO
     */
    @Override
    public PagedResponseDTO<ClassResponseDTO> filterClasses(ClassFilterDTO filter, int page, int size, String sortBy, String sortDirection) {
        // STEP 1: Validasi size tidak melebihi max
        // Tujuan: Mencegah user request data terlalu banyak sekaligus
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        
        // STEP 2: Validasi sortBy field (whitelist untuk keamanan)
        // Tujuan: Mencegah SQL injection & error jika field tidak valid
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "className"; // Default ke className kalau field tidak valid
        }
        
        // STEP 3: Validasi sort direction
        // Tujuan: Pastikan hanya ASC atau DESC
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC")
            ? Sort.Direction.DESC   // Z → A, 100 → 1
            : Sort.Direction.ASC;   // A → Z, 1 → 100
        
        // STEP 4: Buat Pageable object
        // Breakdown:
        // - page: halaman ke berapa (0-based, 0 = halaman pertama)
        // - size: berapa data per halaman
        // - Sort.by(direction, sortBy): urutkan berdasarkan field & arah
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        // STEP 5: Build Specification dari filter DTO
        // Tujuan: Buat instruksi query dinamis berdasarkan field yang diisi
        // ClassSpecification.filterBy() akan:
        // - Cek field mana yang tidak null
        // - Build kondisi WHERE untuk setiap field
        // - Gabungkan dengan AND logic
        Specification<Class> spec = ClassSpecification.filterBy(filter);
        
        // STEP 6: Query ke database dengan specification & pagination
        // Method findAll(spec, pageable) OTOMATIS ada dari JpaSpecificationExecutor
        // Return: Page<Class> berisi data + metadata (totalElements, totalPages, dll)
        Page<Class> classPage = classRepository.findAll(spec, pageable);
        
        // STEP 7: Convert Class entity ke ClassResponseDTO
        // Tujuan: Hanya kirim data yang perlu (security), hide field internal
        Page<ClassResponseDTO> dtoPage = classPage.map(kelas -> {
            ClassResponseDTO dto = new ClassResponseDTO();
            dto.setSecureId(kelas.getSecureId());           // ID aman (UUID)
            dto.setClassName(kelas.getClassName());         // Nama kelas
            dto.setGradeLevel(kelas.getGradeLevel());       // Tingkat kelas
            dto.setAcademicYear(kelas.getAcademicYear());   // Tahun ajaran
            return dto;
        });
        
        // STEP 8: Wrap ke PagedResponseDTO dan return
        // PagedResponseDTO berisi:
        // - content: List<ClassResponseDTO>
        // - page, size, totalElements, totalPages, last, first, dll
        return new PagedResponseDTO<>(dtoPage);
    }
}
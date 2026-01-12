package com.sandy.project.repository;

import com.sandy.project.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findBySecureId(String secureId);
    List<Schedule> findByClazz_SecureId(String classSecureId);
    List<Schedule> findByTeacher_SecureId(String teacherSecureId);
    List<Schedule> findBySubject_SecureId(String subjectSecureId);
    
    // ========== PAGINATION METHODS ==========
    
    // General: Get all schedules dengan soft delete check
    Page<Schedule> findByDeletedFalse(Pageable pageable);
    
    // Filter by Day (e.g., "Monday", "Tuesday", etc.)
    Page<Schedule> findByDayContainingIgnoreCaseAndDeletedFalse(String day, Pageable pageable);
    
    // Filter by Semester
    Page<Schedule> findBySemesterContainingIgnoreCaseAndDeletedFalse(String semester, Pageable pageable);
    
    // Filter by Class (using class secureId)
    Page<Schedule> findByClazz_SecureIdAndDeletedFalse(String classSecureId, Pageable pageable);
    
    // Filter by Subject (using subject secureId)
    Page<Schedule> findBySubject_SecureIdAndDeletedFalse(String subjectSecureId, Pageable pageable);
    
    // Filter by Teacher (using teacher secureId)
    Page<Schedule> findByTeacher_SecureIdAndDeletedFalse(String teacherSecureId, Pageable pageable);
    
    // Filter by Day AND Semester (combined filter)
    Page<Schedule> findByDayContainingIgnoreCaseAndSemesterContainingIgnoreCaseAndDeletedFalse(
            String day, String semester, Pageable pageable);
}

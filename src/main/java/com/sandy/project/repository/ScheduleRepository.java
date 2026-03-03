package com.sandy.project.repository;

import com.sandy.project.domain.Schedule;
import com.sandy.project.dto.query.ScheduleQueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * Repository untuk Schedule entity
 * Extends JpaSpecificationExecutor untuk support dynamic query dengan Specification
 *
 * Tujuan: Menyediakan method untuk akses database Schedule
 * Method dari JpaSpecificationExecutor yang otomatis tersedia:
 * - Page<Schedule> findAll(Specification<Schedule> spec, Pageable pageable)
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
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

    // ========== JPA PROJECTION METHODS (N+1 SOLUTION) ==========
    
    /**
     * JPA Projection untuk menghindari N+1 problem
     * Mengambil Schedule beserta Class, Subject, dan Teacher dalam 1 query JOIN
     */
    @Query("""
        SELECT new com.sandy.project.dto.query.ScheduleQueryDTO(
            sch.secureId, sch.day, sch.startTime, sch.endTime, sch.semester,
            c.secureId, c.className,
            sub.secureId, sub.name,
            t.secureId, t.name
        )
        FROM Schedule sch
        LEFT JOIN sch.clazz c
        LEFT JOIN sch.subject sub
        LEFT JOIN sch.teacher t
        WHERE sch.deleted = false
    """)
    List<ScheduleQueryDTO> findAllScheduleQueryDTO();
}

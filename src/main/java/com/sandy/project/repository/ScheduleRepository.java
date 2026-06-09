package com.sandy.project.repository;

import com.sandy.project.domain.Schedule;
import com.sandy.project.dto.query.ScheduleQueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // ========== PAGINATION WITH EAGER LOADING (N+1 FIX) ==========
    
    @EntityGraph(attributePaths = {"clazz", "subject", "teacher"})
    @Query("SELECT s FROM Schedule s WHERE s.deleted = false")
    Page<Schedule> findAllWithRelations(Pageable pageable);
    
    @EntityGraph(attributePaths = {"clazz", "subject", "teacher"})
    @Query("SELECT s FROM Schedule s WHERE LOWER(s.day) LIKE LOWER(CONCAT('%', :day, '%')) AND s.deleted = false")
    Page<Schedule> findByDayWithRelations(@Param("day") String day, Pageable pageable);
    
    @EntityGraph(attributePaths = {"clazz", "subject", "teacher"})
    @Query("SELECT s FROM Schedule s WHERE LOWER(s.semester) LIKE LOWER(CONCAT('%', :semester, '%')) AND s.deleted = false")
    Page<Schedule> findBySemesterWithRelations(@Param("semester") String semester, Pageable pageable);
    
    @EntityGraph(attributePaths = {"clazz", "subject", "teacher"})
    @Query("SELECT s FROM Schedule s WHERE s.clazz.secureId = :classSecureId AND s.deleted = false")
    Page<Schedule> findByClassSecureIdWithRelations(@Param("classSecureId") String classSecureId, Pageable pageable);
    
    @EntityGraph(attributePaths = {"clazz", "subject", "teacher"})
    @Query("SELECT s FROM Schedule s WHERE s.subject.secureId = :subjectSecureId AND s.deleted = false")
    Page<Schedule> findBySubjectSecureIdWithRelations(@Param("subjectSecureId") String subjectSecureId, Pageable pageable);
    
    @EntityGraph(attributePaths = {"clazz", "subject", "teacher"})
    @Query("SELECT s FROM Schedule s WHERE s.teacher.secureId = :teacherSecureId AND s.deleted = false")
    Page<Schedule> findByTeacherSecureIdWithRelations(@Param("teacherSecureId") String teacherSecureId, Pageable pageable);
    
    @EntityGraph(attributePaths = {"clazz", "subject", "teacher"})
    @Query("SELECT s FROM Schedule s WHERE LOWER(s.day) LIKE LOWER(CONCAT('%', :day, '%')) AND LOWER(s.semester) LIKE LOWER(CONCAT('%', :semester, '%')) AND s.deleted = false")
    Page<Schedule> findByDayAndSemesterWithRelations(@Param("day") String day, @Param("semester") String semester, Pageable pageable);

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
    
    /**
     * Fetch Schedules dengan pagination dan JPA Projection
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
    Page<ScheduleQueryDTO> findAllScheduleQueryDTOPaged(Pageable pageable);
    
    /**
     * Fetch single Schedule by secureId dengan JPA Projection
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
        WHERE sch.secureId = :secureId AND sch.deleted = false
    """)
    Optional<ScheduleQueryDTO> findScheduleQueryDTOBySecureId(@Param("secureId") String secureId);
}

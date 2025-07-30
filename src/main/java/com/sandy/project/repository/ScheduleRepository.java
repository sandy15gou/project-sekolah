package com.sandy.project.repository;

import com.sandy.project.domain.Schedule;
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
}

package com.sandy.project.repository;

import com.sandy.project.domain.Class;

import com.sandy.project.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {
    
 
    Optional<Class> findBySecureId(String secureId);
    
    List<Class> findByHomeroomTeacherSecureId(String teacherSecureId);
   
    List<Class> findByAcademicYear(String academicYear);
    Page<Class> findByDeletedFalse(Pageable pageable);
    Page<Class> findByClassNameContainingAndDeletedFalse(String name, Pageable pageable);
    
    Page<Class> findByGradeLevelContainingAndDeletedFalse(String gradeLevel, Pageable pageable);
    
    Page<Class> findByAcademicYearContainingAndDeletedFalse(String academicYear, Pageable pageable);
    
}
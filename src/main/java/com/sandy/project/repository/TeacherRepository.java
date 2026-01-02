package com.sandy.project.repository;

import com.sandy.project.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
 
    Optional<Teacher> findBySecureId(String secureId);
}

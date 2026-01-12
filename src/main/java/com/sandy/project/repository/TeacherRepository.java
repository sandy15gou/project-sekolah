package com.sandy.project.repository;

import com.sandy.project.domain.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long>,
                                          JpaSpecificationExecutor<Teacher> {
    
 
    Optional<Teacher> findBySecureId(String secureId);
    
    // ========== PAGINATION METHODS ==========
    
    // Pagination: Get all teachers dengan soft delete check
    Page<Teacher> findByDeletedFalse(Pageable pageable);
    
    // Pagination: Search by name dengan LIKE dan soft delete check
    Page<Teacher> findByNameContainingAndDeletedFalse(String name, Pageable pageable);
}

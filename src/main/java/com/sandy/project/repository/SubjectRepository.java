package com.sandy.project.repository;

import com.sandy.project.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository untuk Subject entity
 * Extends JpaSpecificationExecutor untuk support dynamic query dengan Specification
 *
 * Tujuan: Menyediakan method untuk akses database Subject
 * Method dari JpaSpecificationExecutor yang otomatis tersedia:
 * - Page<Subject> findAll(Specification<Subject> spec, Pageable pageable)
 */
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>, JpaSpecificationExecutor<Subject> {
    Subject findBySecureId(String secureId);
}

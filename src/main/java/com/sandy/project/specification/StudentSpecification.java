package com.sandy.project.specification;

import com.sandy.project.domain.Student;
import com.sandy.project.dto.StudentFilterDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Specification class untuk dynamic query filtering Student
 * Menggunakan JPA Criteria API untuk build query secara dinamis
 */
public class StudentSpecification {
    
    /**
     * Build specification berdasarkan StudentFilterDTO
     * Semua filter akan di-combine menggunakan AND logic
     *
     * @param filter DTO yang berisi kriteria filter
     * @return Specification untuk digunakan di repository
     */
    public static Specification<Student> filterBy(StudentFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Always filter by deleted = false (soft delete)
            predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
            
            // Filter by name (case insensitive, partial match)
            if (filter.getName() != null && !filter.getName().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"
                    )
                );
            }
            
            // Filter by gender (exact match)
            if (filter.getGender() != null && !filter.getGender().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.equal(
                        criteriaBuilder.upper(root.get("gender")),
                        filter.getGender().toUpperCase()
                    )
                );
            }
            
            // Filter by address (case insensitive, partial match)
            if (filter.getAddress() != null && !filter.getAddress().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("address")),
                        "%" + filter.getAddress().toLowerCase() + "%"
                    )
                );
            }
            
            // Filter by birth date range (from)
            if (filter.getBirthDateFrom() != null) {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("birthDate"),
                        filter.getBirthDateFrom()
                    )
                );
            }
            
            // Filter by birth date range (to)
            if (filter.getBirthDateTo() != null) {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("birthDate"),
                        filter.getBirthDateTo()
                    )
                );
            }
            
            // Filter by minimum age
            if (filter.getMinAge() != null) {
                LocalDate maxBirthDate = LocalDate.now().minusYears(filter.getMinAge());
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("birthDate"),
                        maxBirthDate
                    )
                );
            }
            
            // Filter by maximum age
            if (filter.getMaxAge() != null) {
                LocalDate minBirthDate = LocalDate.now().minusYears(filter.getMaxAge() + 1).plusDays(1);
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("birthDate"),
                        minBirthDate
                    )
                );
            }
            
            // Combine all predicates with AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}


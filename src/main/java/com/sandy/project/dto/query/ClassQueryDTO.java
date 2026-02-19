package com.sandy.project.dto.query;

import java.io.Serializable;

/**
 * Query DTO untuk Class entity - menghindari N+1 problem
 *
 * Digunakan sebagai JPQL constructor expression agar data Class
 * beserta relasi LAZY (homeroomTeacher) bisa diambil dalam 1 query JOIN,
 * tanpa trigger lazy loading terpisah.
 *
 * Contoh JPQL:
 * SELECT new com.sandy.project.dto.query.ClassQueryDTO(
 *     c.secureId, c.className, c.gradeLevel, c.academicYear,
 *     c.maxCapacity, c.description,
 *     t.secureId, t.name
 * )
 * FROM Class c
 * LEFT JOIN c.homeroomTeacher t
 * WHERE c.deleted = false
 */
public record ClassQueryDTO(
        String secureId,
        String className,
        String gradeLevel,
        String academicYear,
        Integer maxCapacity,
        String description,
        // Homeroom Teacher (dari relasi LAZY)
        String homeroomTeacherSecureId,
        String homeroomTeacherName
) implements Serializable {
}

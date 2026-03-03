package com.sandy.project.dto.query;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Query DTO untuk Student entity - menghindari N+1 problem
 *
 * Digunakan sebagai JPQL constructor expression agar data Student
 * beserta relasi LAZY (studentClass) bisa diambil dalam 1 query JOIN,
 * tanpa trigger lazy loading terpisah.
 *
 * Contoh JPQL:
 * SELECT new com.sandy.project.dto.query.StudentQueryDTO(
 *     s.secureId, s.name, s.birthDate, s.gender, s.address,
 *     c.secureId, c.className
 * )
 * FROM Student s
 * LEFT JOIN s.studentClass c
 * WHERE s.deleted = false
 */
public record StudentQueryDTO(
        String secureId,
        String name,
        LocalDate birthDate,
        String gender,
        String address,
        // Class (dari relasi LAZY)
        String classSecureId,
        String className
) implements Serializable {
}

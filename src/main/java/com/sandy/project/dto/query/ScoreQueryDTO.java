package com.sandy.project.dto.query;

import java.io.Serializable;

/**
 * Query DTO untuk Score entity - menghindari N+1 problem
 *
 * Digunakan sebagai JPQL constructor expression agar data Score
 * beserta relasi LAZY (student, subject) bisa diambil dalam 1 query JOIN,
 * tanpa trigger lazy loading terpisah.
 *
 * Contoh JPQL:
 * SELECT new com.sandy.project.dto.query.ScoreQueryDTO(
 *     sc.secureId, sc.score, sc.semester,
 *     st.secureId, st.name,
 *     sub.secureId, sub.name
 * )
 * FROM Score sc
 * LEFT JOIN sc.student st
 * LEFT JOIN sc.subject sub
 * WHERE sc.deleted = false
 */
public record ScoreQueryDTO(
        String secureId,
        Integer score,
        String semester,
        // Student (dari relasi LAZY)
        String studentSecureId,
        String studentName,
        // Subject (dari relasi LAZY)
        String subjectSecureId,
        String subjectName
) implements Serializable {
}

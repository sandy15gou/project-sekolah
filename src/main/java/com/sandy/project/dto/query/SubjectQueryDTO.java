package com.sandy.project.dto.query;

/**
 * JPA Projection untuk Subject
 * Digunakan untuk mengoptimalkan query dan menghindari N+1 problem
 *
 * CATATAN: eligibleTeachers tidak bisa di-fetch dalam constructor projection
 * karena JPA tidak support subquery yang return collection.
 * Teachers harus di-fetch terpisah menggunakan method findEligibleTeachersBySubjectSecureId()
 */
public record SubjectQueryDTO(
        String secureId,
        String name,
        String description
) {
}


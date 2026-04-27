package com.sandy.project.dto.query;

import java.time.LocalDate;

/**
 * JPA Projection untuk Teacher
 * Digunakan untuk optimasi query Teacher, menghindari N+1 problem
 * Hanya fetch field yang dibutuhkan untuk list/detail view
 */
public record TeacherQueryDTO(
        String secureId,
        String name,
        String address,
        LocalDate birthDate,
        String gender
) {
}


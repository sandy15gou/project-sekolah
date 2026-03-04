package com.sandy.project.dto.query;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Query DTO untuk Teacher entity - untuk konsistensi dengan entity lain
 *
 * Teacher tidak memiliki relasi LAZY yang perlu di-join,
 * tetapi DTO ini berguna untuk:
 * 1. Konsistensi API response
 * 2. Menghindari expose internal ID
 * 3. Optimasi transfer data (hanya field yang diperlukan)
 *
 * Contoh JPQL:
 * SELECT new com.sandy.project.dto.query.TeacherQueryDTO(
 *     t.secureId, t.name, t.birthDate, t.gender, t.address
 * )
 * FROM Teacher t
 * WHERE t.deleted = false
 */
public record TeacherQueryDTO(
        String secureId,
        String name,
        String gender,
        LocalDate birthDate,
        String address
) implements Serializable {
}

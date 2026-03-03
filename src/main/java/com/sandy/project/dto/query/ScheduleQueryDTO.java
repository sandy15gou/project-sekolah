package com.sandy.project.dto.query;

import java.io.Serializable;

/**
 * Query DTO untuk Schedule entity - menghindari N+1 problem
 *
 * Digunakan sebagai JPQL constructor expression agar data Schedule
 * beserta relasi LAZY (clazz, subject, teacher) bisa diambil dalam 1 query JOIN,
 * tanpa trigger lazy loading terpisah.
 *
 * Contoh JPQL:
 * SELECT new com.sandy.project.dto.query.ScheduleQueryDTO(
 *     sch.secureId, sch.day, sch.startTime, sch.endTime, sch.semester,
 *     c.secureId, c.className,
 *     sub.secureId, sub.name,
 *     t.secureId, t.name
 * )
 * FROM Schedule sch
 * LEFT JOIN sch.clazz c
 * LEFT JOIN sch.subject sub
 * LEFT JOIN sch.teacher t
 * WHERE sch.deleted = false
 */
public record ScheduleQueryDTO(
        String secureId,
        String day,
        String startTime,
        String endTime,
        String semester,
        // Class (dari relasi LAZY)
        String classSecureId,
        String className,
        // Subject (dari relasi LAZY)
        String subjectSecureId,
        String subjectName,
        // Teacher (dari relasi LAZY)
        String teacherSecureId,
        String teacherName
) implements Serializable {
}

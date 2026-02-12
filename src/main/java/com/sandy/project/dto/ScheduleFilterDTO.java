package com.sandy.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO untuk filter Schedule dengan berbagai kriteria
 * Semua field bersifat optional, kombinasi field akan menggunakan AND logic
 *
 * Tujuan: Wadah untuk menyimpan kriteria filter dari user
 * Analogi: Formulir pencarian jadwal yang diisi user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleFilterDTO {
    
    /**
     * Filter berdasarkan hari (partial match, case insensitive)
     * Contoh: "Monday" akan match "Monday", "monday", dll
     *
     * Breakdown:
     * - Input user: "Monday"
     * - Diconvert: "monday" (lowercase)
     * - Pattern: "%monday%" (wildcard)
     * - Match: apapun yang mengandung "monday"
     */
    private String day;
    
    /**
     * Filter berdasarkan waktu mulai (partial match, case insensitive)
     * Contoh: "08:00" akan match "08:00", "08:00:00", dll
     *
     * Breakdown:
     * - Input user: "08:00"
     * - Diconvert: "08:00" (lowercase)
     * - Pattern: "%08:00%"
     * - Match: apapun yang mengandung "08:00"
     */
    private String startTime;
    
    /**
     * Filter berdasarkan waktu selesai (partial match, case insensitive)
     * Contoh: "10:00" akan match "10:00", "10:00:00", dll
     *
     * Breakdown:
     * - Input user: "10:00"
     * - Diconvert: "10:00" (lowercase)
     * - Pattern: "%10:00%"
     * - Match: apapun yang mengandung "10:00"
     */
    private String endTime;
    
    /**
     * Filter berdasarkan semester (partial match, case insensitive)
     * Contoh: "1" akan match "Semester 1", "1/2024", dll
     *
     * Breakdown:
     * - Input user: "1"
     * - Diconvert: "1" (lowercase)
     * - Pattern: "%1%"
     * - Match: apapun yang mengandung "1"
     */
    private String semester;
    
    /**
     * Filter berdasarkan class ID (exact match)
     * Contoh: classId = "uuid-123" → cari schedule untuk kelas tertentu
     *
     * Breakdown:
     * - Input user: "uuid-123"
     * - SQL: class.secure_id = 'uuid-123'
     * - Tujuan: Cari jadwal untuk kelas tertentu (exact match)
     */
    private String classId;
    
    /**
     * Filter berdasarkan subject ID (exact match)
     * Contoh: subjectId = "uuid-456" → cari schedule untuk mata pelajaran tertentu
     *
     * Breakdown:
     * - Input user: "uuid-456"
     * - SQL: subject.secure_id = 'uuid-456'
     * - Tujuan: Cari jadwal untuk mata pelajaran tertentu
     */
    private String subjectId;
    
    /**
     * Filter berdasarkan teacher ID (exact match)
     * Contoh: teacherId = "uuid-789" → cari schedule untuk guru tertentu
     *
     * Breakdown:
     * - Input user: "uuid-789"
     * - SQL: teacher.secure_id = 'uuid-789'
     * - Tujuan: Cari jadwal mengajar untuk guru tertentu
     */
    private String teacherId;
}


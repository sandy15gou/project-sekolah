package com.sandy.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO untuk filter Score dengan berbagai kriteria
 * Semua field bersifat optional, kombinasi field akan menggunakan AND logic
 *
 * Tujuan: Wadah untuk menyimpan kriteria filter dari user
 * Analogi: Formulir pencarian nilai yang diisi user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreFilterDTO {
    
    /**
     * Filter berdasarkan nilai minimal (>=)
     * Contoh: minScore = 75 → cari nilai >= 75 (passing grade)
     *
     * Breakdown:
     * - Input user: 75
     * - SQL: score >= 75
     * - Tujuan: Cari nilai yang lebih besar atau sama dengan 75
     */
    private Integer minScore;
    
    /**
     * Filter berdasarkan nilai maksimal (<=)
     * Contoh: maxScore = 90 → cari nilai <= 90
     *
     * Breakdown:
     * - Input user: 90
     * - SQL: score <= 90
     * - Tujuan: Cari nilai yang lebih kecil atau sama dengan 90
     */
    private Integer maxScore;
    
    /**
     * Filter berdasarkan semester (partial match, case insensitive)
     * Contoh: "1" akan match "1", "Semester 1", dll
     *
     * Breakdown:
     * - Input user: "1"
     * - Diconvert: "1" (lowercase)
     * - Pattern: "%1%"
     * - Match: apapun yang mengandung "1"
     */
    private String semester;
    
    /**
     * Filter berdasarkan student ID (exact match)
     * Contoh: studentId = "uuid-123" → cari nilai untuk siswa tertentu
     *
     * Breakdown:
     * - Input user: "uuid-123"
     * - SQL: student.secure_id = 'uuid-123'
     * - Tujuan: Cari semua nilai untuk siswa tertentu (EXACT match)
     */
    private String studentId;
    
    /**
     * Filter berdasarkan subject ID (exact match)
     * Contoh: subjectId = "uuid-456" → cari nilai untuk mata pelajaran tertentu
     *
     * Breakdown:
     * - Input user: "uuid-456"
     * - SQL: subject.secure_id = 'uuid-456'
     * - Tujuan: Cari semua nilai untuk mata pelajaran tertentu
     */
    private String subjectId;
    
    /**
     * Filter berdasarkan grade/huruf mutu
     * Contoh: grade = "A" → cari nilai grade A (90-100)
     *
     * Breakdown:
     * - Input user: "A"
     * - Logic: A = score >= 90
     * - B = score >= 75 AND score < 90
     * - C = score >= 60 AND score < 75
     * - D = score >= 50 AND score < 60
     * - E = score < 50
     */
    private String grade;
    
    /**
     * Filter berdasarkan passing status (lulus/tidak)
     * Contoh: isPassing = true → cari nilai yang lulus (>= 75)
     *
     * Breakdown:
     * - Input user: true
     * - SQL: score >= 75
     * - Tujuan: Cari nilai yang sudah mencapai passing grade
     */
    private Boolean isPassing;
}


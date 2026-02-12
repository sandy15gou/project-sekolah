package com.sandy.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO untuk filter Class dengan berbagai kriteria
 * Semua field bersifat optional, kombinasi field akan menggunakan AND logic
 *
 * Tujuan: Wadah untuk menyimpan kriteria filter dari user
 * Analogi: Formulir pencarian yang diisi user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassFilterDTO {
    
    /**
     * Filter berdasarkan nama kelas (partial match, case insensitive)
     * Contoh: "X" akan match "X-1", "X-2", "XI-A", dll
     *
     * Breakdown:
     * - Input user: "X"
     * - Diconvert: "x" (lowercase)
     * - Pattern: "%x%" (wildcard)
     * - Match: apapun yang mengandung "x"
     */
    private String className;
    
    /**
     * Filter berdasarkan tingkat kelas (partial match, case insensitive)
     * Contoh: "10" akan match "10", "Grade 10", dll
     *
     * Breakdown:
     * - Input user: "10"
     * - Diconvert: "10" (lowercase)
     * - Pattern: "%10%"
     * - Match: apapun yang mengandung "10"
     */
    private String gradeLevel;
    
    /**
     * Filter berdasarkan tahun ajaran (partial match, case insensitive)
     * Contoh: "2024" akan match "2024/2025", "2024-2025", dll
     *
     * Breakdown:
     * - Input user: "2024"
     * - Diconvert: "2024" (lowercase)
     * - Pattern: "%2024%"
     * - Match: apapun yang mengandung "2024"
     */
    private String academicYear;
    
    /**
     * Filter berdasarkan kapasitas minimal (>=)
     * Contoh: minCapacity = 20 → cari kelas dengan kapasitas >= 20
     *
     * Breakdown:
     * - Input user: 20
     * - SQL: max_capacity >= 20
     * - Tujuan: Cari kelas yang bisa menampung minimal 20 siswa
     */
    private Integer minCapacity;
    
    /**
     * Filter berdasarkan kapasitas maksimal (<=)
     * Contoh: maxCapacity = 40 → cari kelas dengan kapasitas <= 40
     *
     * Breakdown:
     * - Input user: 40
     * - SQL: max_capacity <= 40
     * - Tujuan: Cari kelas yang kapasitasnya tidak lebih dari 40 siswa
     */
    private Integer maxCapacity;
}


package com.sandy.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO untuk filter Subject dengan berbagai kriteria
 * Semua field bersifat optional, kombinasi field akan menggunakan AND logic
 *
 * Tujuan: Wadah untuk menyimpan kriteria filter dari user
 * Analogi: Formulir pencarian mata pelajaran yang diisi user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectFilterDTO {
    
    /**
     * Filter berdasarkan nama mata pelajaran (partial match, case insensitive)
     * Contoh: "Math" akan match "Mathematics", "Math Basic", dll
     *
     * Breakdown:
     * - Input user: "Math"
     * - Diconvert: "math" (lowercase)
     * - Pattern: "%math%" (wildcard)
     * - Match: apapun yang mengandung "math"
     *
     * Use case:
     * - Cari mata pelajaran yang namanya mengandung "Math"
     * - Cari "English" → match "English", "English Literature", dll
     */
    private String name;
    
    /**
     * Filter berdasarkan deskripsi (partial match, case insensitive)
     * Contoh: "science" akan match deskripsi yang mengandung kata "science"
     *
     * Breakdown:
     * - Input user: "science"
     * - Diconvert: "science" (lowercase)
     * - Pattern: "%science%"
     * - Match: apapun deskripsi yang mengandung "science"
     *
     * Use case:
     * - Cari mata pelajaran dengan deskripsi tertentu
     * - Filter by kategori (science, social, art, dll)
     */
    private String description;
}


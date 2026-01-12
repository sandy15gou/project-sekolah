package com.sandy.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO untuk filter Student dengan berbagai kriteria
 * Semua field bersifat optional, kombinasi field akan menggunakan AND logic
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentFilterDTO {
    
    /**
     * Filter berdasarkan nama (partial match, case insensitive)
     * Contoh: "John" akan match "John Doe", "Johnny", dll
     */
    private String name;
    
    /**
     * Filter berdasarkan gender
     * Valid values: "M" (Male) atau "F" (Female)
     */
    private String gender;
    
    /**
     * Filter berdasarkan alamat (partial match, case insensitive)
     * Contoh: "Jakarta" akan match "Jakarta Selatan", "Jakarta Utara", dll
     */
    private String address;
    
    /**
     * Filter berdasarkan tanggal lahir mulai dari (inclusive)
     * Format: yyyy-MM-dd
     */
    private LocalDate birthDateFrom;
    
    /**
     * Filter berdasarkan tanggal lahir sampai dengan (inclusive)
     * Format: yyyy-MM-dd
     */
    private LocalDate birthDateTo;
    
    /**
     * Filter berdasarkan umur minimal (dalam tahun)
     */
    private Integer minAge;
    
    /**
     * Filter berdasarkan umur maksimal (dalam tahun)
     */
    private Integer maxAge;
}


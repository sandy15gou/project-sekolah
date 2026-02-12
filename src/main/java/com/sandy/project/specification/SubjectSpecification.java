package com.sandy.project.specification;

import com.sandy.project.domain.Subject;
import com.sandy.project.dto.SubjectFilterDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification class untuk dynamic query filtering Subject
 * Menggunakan JPA Criteria API untuk build query secara dinamis
 *
 * Tujuan: Membangun query SQL secara dinamis berdasarkan filter yang diisi user
 * Analogi: Tukang bangunan yang membangun query SQL sesuai pesanan
 */
public class SubjectSpecification {
    
    /**
     * Build specification berdasarkan SubjectFilterDTO
     * Semua filter akan di-combine menggunakan AND logic
     *
     * @param filter DTO yang berisi kriteria filter
     * @return Specification untuk digunakan di repository
     *
     * Flow:
     * 1. Cek field mana yang diisi user (tidak null)
     * 2. Untuk setiap field yang diisi, tambahkan kondisi WHERE
     * 3. Gabungkan semua kondisi dengan AND
     * 4. Return specification untuk dieksekusi ke database
     */
    public static Specification<Subject> filterBy(SubjectFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            // Wadah untuk menampung semua kondisi WHERE
            List<Predicate> predicates = new ArrayList<>();
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 1: Always filter by deleted = false (soft delete)
            // ════════════════════════════════════════════════════════════════════
            // Breakdown:
            // - criteriaBuilder.equal() → buat kondisi "="
            // - root.get("deleted") → ambil kolom "deleted" dari tabel subjects
            // - false → nilai yang dicari
            // SQL: WHERE deleted = false
            predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 2: Filter by name (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getName() != null && !filter.getName().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getName() → ambil value "Math" dari user
                // 2. .toLowerCase() → convert jadi "math" (case insensitive)
                // 3. "%" + ... + "%" → tambah wildcard jadi "%math%"
                // 4. criteriaBuilder.lower() → LOWER(name) di database
                // 5. criteriaBuilder.like() → operasi LIKE
                //
                // SQL: AND LOWER(name) LIKE '%math%'
                // Tujuan: Cari mata pelajaran yang namanya MENGANDUNG "math"
                //
                // Contoh:
                // Input: "Math"
                // Match: "Mathematics", "Math Basic", "Applied Math", dll
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 3: Filter by description (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getDescription() != null && !filter.getDescription().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getDescription() → ambil value "science" dari user
                // 2. .toLowerCase() → convert jadi "science"
                // 3. "%" + ... + "%" → tambah wildcard jadi "%science%"
                //
                // SQL: AND LOWER(description) LIKE '%science%'
                // Tujuan: Cari mata pelajaran dengan deskripsi yang MENGANDUNG "science"
                //
                // Use case:
                // - Cari semua mata pelajaran kategori science
                // - Filter by kata kunci dalam deskripsi
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + filter.getDescription().toLowerCase() + "%"
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // GABUNGKAN SEMUA KONDISI DENGAN AND
            // ════════════════════════════════════════════════════════════════════
            // Breakdown:
            // - predicates.toArray() → convert List ke Array
            // - criteriaBuilder.and() → gabungkan dengan operator AND
            //
            // Hasil: WHERE deleted = false AND ... AND ...
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}


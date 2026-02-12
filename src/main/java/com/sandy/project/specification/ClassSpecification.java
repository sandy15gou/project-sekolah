package com.sandy.project.specification;

import com.sandy.project.domain.Class;
import com.sandy.project.dto.ClassFilterDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification class untuk dynamic query filtering Class
 * Menggunakan JPA Criteria API untuk build query secara dinamis
 *
 * Tujuan: Membangun query SQL secara dinamis berdasarkan filter yang diisi user
 * Analogi: Tukang bangunan yang membangun query SQL sesuai pesanan
 */
public class ClassSpecification {
    
    /**
     * Build specification berdasarkan ClassFilterDTO
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
    public static Specification<Class> filterBy(ClassFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            // Wadah untuk menampung semua kondisi WHERE
            List<Predicate> predicates = new ArrayList<>();
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 1: Always filter by deleted = false (soft delete)
            // ════════════════════════════════════════════════════════════════════
            // Breakdown:
            // - criteriaBuilder.equal() → buat kondisi "="
            // - root.get("deleted") → ambil kolom "deleted" dari tabel class
            // - false → nilai yang dicari
            // SQL: WHERE deleted = false
            predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 2: Filter by className (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getClassName() != null && !filter.getClassName().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getClassName() → ambil value "X-1" dari user
                // 2. .toLowerCase() → convert jadi "x-1" (case insensitive)
                // 3. "%" + ... + "%" → tambah wildcard jadi "%x-1%"
                // 4. criteriaBuilder.lower() → LOWER(class_name) di database
                // 5. criteriaBuilder.like() → operasi LIKE
                //
                // SQL: AND LOWER(class_name) LIKE '%x-1%'
                // Tujuan: Cari kelas yang MENGANDUNG "x-1" (tidak harus persis)
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("className")),
                        "%" + filter.getClassName().toLowerCase() + "%"
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 3: Filter by gradeLevel (kalau user isi)
            // ════════════════════════════════════���═══════════════════════════════
            if (filter.getGradeLevel() != null && !filter.getGradeLevel().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getGradeLevel() → ambil value "10" dari user
                // 2. .toLowerCase() → convert jadi "10"
                // 3. "%" + ... + "%" → tambah wildcard jadi "%10%"
                //
                // SQL: AND LOWER(grade_level) LIKE '%10%'
                // Tujuan: Cari kelas tingkat yang MENGANDUNG "10"
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("gradeLevel")),
                        "%" + filter.getGradeLevel().toLowerCase() + "%"
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 4: Filter by academicYear (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getAcademicYear() != null && !filter.getAcademicYear().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getAcademicYear() → ambil value "2024" dari user
                // 2. .toLowerCase() → convert jadi "2024"
                // 3. "%" + ... + "%" → tambah wildcard jadi "%2024%"
                //
                // SQL: AND LOWER(academic_year) LIKE '%2024%'
                // Tujuan: Cari tahun ajaran yang MENGANDUNG "2024"
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("academicYear")),
                        "%" + filter.getAcademicYear().toLowerCase() + "%"
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 5: Filter by minimum capacity (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getMinCapacity() != null) {
                // Breakdown:
                // 1. filter.getMinCapacity() → ambil value 20 dari user
                // 2. criteriaBuilder.greaterThanOrEqualTo() → operator ">="
                // 3. root.get("maxCapacity") → kolom max_capacity di database
                //
                // SQL: AND max_capacity >= 20
                // Tujuan: Cari kelas yang kapasitasnya >= 20 siswa
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("maxCapacity"),
                        filter.getMinCapacity()
                    )
                );
            }
            
            // ═════════════════════��══════════════════════════════════════════════
            // KONDISI 6: Filter by maximum capacity (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getMaxCapacity() != null) {
                // Breakdown:
                // 1. filter.getMaxCapacity() → ambil value 40 dari user
                // 2. criteriaBuilder.lessThanOrEqualTo() → operator "<="
                // 3. root.get("maxCapacity") → kolom max_capacity di database
                //
                // SQL: AND max_capacity <= 40
                // Tujuan: Cari kelas yang kapasitasnya <= 40 siswa
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("maxCapacity"),
                        filter.getMaxCapacity()
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
            // Hasil: WHERE deleted = false AND ... AND ... AND ...
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}


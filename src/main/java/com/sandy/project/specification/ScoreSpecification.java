package com.sandy.project.specification;

import com.sandy.project.domain.Score;
import com.sandy.project.dto.ScoreFilterDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification class untuk dynamic query filtering Score
 * Menggunakan JPA Criteria API untuk build query secara dinamis
 *
 * Tujuan: Membangun query SQL secara dinamis berdasarkan filter yang diisi user
 * Analogi: Tukang bangunan yang membangun query SQL sesuai pesanan
 */
public class ScoreSpecification {
    
    /**
     * Build specification berdasarkan ScoreFilterDTO
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
    public static Specification<Score> filterBy(ScoreFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            // Wadah untuk menampung semua kondisi WHERE
            List<Predicate> predicates = new ArrayList<>();
            
            // OPTIMASI N+1: Eager fetch relationships untuk data query (bukan count query)
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("student", jakarta.persistence.criteria.JoinType.LEFT);
                root.fetch("subject", jakarta.persistence.criteria.JoinType.LEFT);
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 1: Always filter by deleted = false (soft delete)
            // ════════════════════════════════════════════════════════════════════
            predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 2: Filter by minScore (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getMinScore() != null) {
                // Breakdown:
                // 1. filter.getMinScore() → ambil value 75 dari user
                // 2. criteriaBuilder.greaterThanOrEqualTo() → operator ">="
                // 3. root.get("score") �� kolom score di database
                //
                // SQL: AND score >= 75
                // Tujuan: Cari nilai yang >= 75 (passing grade)
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("score"),
                        filter.getMinScore()
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 3: Filter by maxScore (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getMaxScore() != null) {
                // Breakdown:
                // 1. filter.getMaxScore() → ambil value 90 dari user
                // 2. criteriaBuilder.lessThanOrEqualTo() → operator "<="
                // 3. root.get("score") → kolom score di database
                //
                // SQL: AND score <= 90
                // Tujuan: Cari nilai yang <= 90
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("score"),
                        filter.getMaxScore()
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 4: Filter by semester (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getSemester() != null && !filter.getSemester().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getSemester() → ambil value "1" dari user
                // 2. .toLowerCase() → convert jadi "1"
                // 3. "%" + ... + "%" → tambah wildcard jadi "%1%"
                //
                // SQL: AND LOWER(semester) LIKE '%1%'
                // Tujuan: Cari nilai semester yang MENGANDUNG "1"
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("semester")),
                        "%" + filter.getSemester().toLowerCase() + "%"
                    )
                );
            }
            
            // ═════════��══════════════════════════════════════════════════════════
            // KONDISI 5: Filter by studentId (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getStudentId() != null && !filter.getStudentId().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getStudentId() → ambil value "uuid-123" dari user
                // 2. root.get("student") → ambil relasi ke tabel Student
                // 3. .get("secureId") → ambil kolom secure_id dari tabel Student
                // 4. criteriaBuilder.equal() → operator "="
                //
                // SQL: AND student.secure_id = 'uuid-123'
                // Tujuan: Cari nilai untuk siswa tertentu (EXACT match)
                predicates.add(
                    criteriaBuilder.equal(
                        root.get("student").get("secureId"),
                        filter.getStudentId()
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 6: Filter by subjectId (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getSubjectId() != null && !filter.getSubjectId().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getSubjectId() → ambil value "uuid-456" dari user
                // 2. root.get("subject") → ambil relasi ke tabel Subject
                // 3. .get("secureId") → ambil kolom secure_id dari tabel Subject
                //
                // SQL: AND subject.secure_id = 'uuid-456'
                // Tujuan: Cari nilai untuk mata pelajaran tertentu (EXACT match)
                predicates.add(
                    criteriaBuilder.equal(
                        root.get("subject").get("secureId"),
                        filter.getSubjectId()
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 7: Filter by grade/huruf mutu (kalau user isi)
            // ═════════════════════════════════════════════════���══════════════════
            if (filter.getGrade() != null && !filter.getGrade().trim().isEmpty()) {
                // Breakdown:
                // Grade mapping:
                // A = 90-100, B = 75-89, C = 60-74, D = 50-59, E = 0-49
                String grade = filter.getGrade().toUpperCase();
                
                switch (grade) {
                    case "A":
                        // SQL: AND score >= 90
                        // Tujuan: Cari nilai grade A
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("score"), 90));
                        break;
                    case "B":
                        // SQL: AND score >= 75 AND score < 90
                        // Tujuan: Cari nilai grade B
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("score"), 75));
                        predicates.add(criteriaBuilder.lessThan(root.get("score"), 90));
                        break;
                    case "C":
                        // SQL: AND score >= 60 AND score < 75
                        // Tujuan: Cari nilai grade C
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("score"), 60));
                        predicates.add(criteriaBuilder.lessThan(root.get("score"), 75));
                        break;
                    case "D":
                        // SQL: AND score >= 50 AND score < 60
                        // Tujuan: Cari nilai grade D
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("score"), 50));
                        predicates.add(criteriaBuilder.lessThan(root.get("score"), 60));
                        break;
                    case "E":
                        // SQL: AND score < 50
                        // Tujuan: Cari nilai grade E (tidak lulus)
                        predicates.add(criteriaBuilder.lessThan(root.get("score"), 50));
                        break;
                }
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 8: Filter by isPassing (lulus/tidak lulus)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getIsPassing() != null) {
                // Breakdown:
                // Passing grade = 75
                // isPassing = true → score >= 75
                // isPassing = false → score < 75
                
                if (filter.getIsPassing()) {
                    // SQL: AND score >= 75
                    // Tujuan: Cari nilai yang sudah lulus
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("score"), 75));
                } else {
                    // SQL: AND score < 75
                    // Tujuan: Cari nilai yang belum lulus
                    predicates.add(criteriaBuilder.lessThan(root.get("score"), 75));
                }
            }
            
            // ════════════════════════════════════════════════════════════════════
            // GABUNGKAN SEMUA KONDISI DENGAN AND
            // ═════════════════════════════════════════════════════════��══════════
            // Breakdown:
            // - predicates.toArray() → convert List ke Array
            // - criteriaBuilder.and() → gabungkan dengan operator AND
            //
            // Hasil: WHERE deleted = false AND ... AND ... AND ...
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}


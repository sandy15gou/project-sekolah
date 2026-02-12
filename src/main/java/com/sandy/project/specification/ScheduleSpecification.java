package com.sandy.project.specification;

import com.sandy.project.domain.Schedule;
import com.sandy.project.dto.ScheduleFilterDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification class untuk dynamic query filtering Schedule
 * Menggunakan JPA Criteria API untuk build query secara dinamis
 *
 * Tujuan: Membangun query SQL secara dinamis berdasarkan filter yang diisi user
 * Analogi: Tukang bangunan yang membangun query SQL sesuai pesanan
 */
public class ScheduleSpecification {
    
    /**
     * Build specification berdasarkan ScheduleFilterDTO
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
    public static Specification<Schedule> filterBy(ScheduleFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            // Wadah untuk menampung semua kondisi WHERE
            List<Predicate> predicates = new ArrayList<>();
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 1: Always filter by deleted = false (soft delete)
            // ════════════════════════════════════════════════════════════════════
            // Breakdown:
            // - criteriaBuilder.equal() → buat kondisi "="
            // - root.get("deleted") → ambil kolom "deleted" dari tabel schedule
            // - false → nilai yang dicari
            // SQL: WHERE deleted = false
            predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 2: Filter by day (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getDay() != null && !filter.getDay().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getDay() → ambil value "Monday" dari user
                // 2. .toLowerCase() → convert jadi "monday" (case insensitive)
                // 3. "%" + ... + "%" → tambah wildcard jadi "%monday%"
                // 4. criteriaBuilder.lower() → LOWER(day) di database
                // 5. criteriaBuilder.like() → operasi LIKE
                //
                // SQL: AND LOWER(day) LIKE '%monday%'
                // Tujuan: Cari jadwal pada hari yang MENGANDUNG "monday"
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("day")),
                        "%" + filter.getDay().toLowerCase() + "%"
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 3: Filter by startTime (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getStartTime() != null && !filter.getStartTime().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getStartTime() → ambil value "08:00" dari user
                // 2. .toLowerCase() → convert jadi "08:00"
                // 3. "%" + ... + "%" → tambah wildcard jadi "%08:00%"
                //
                // SQL: AND LOWER(start_time) LIKE '%08:00%'
                // Tujuan: Cari jadwal yang waktu mulainya MENGANDUNG "08:00"
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("startTime")),
                        "%" + filter.getStartTime().toLowerCase() + "%"
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 4: Filter by endTime (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getEndTime() != null && !filter.getEndTime().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getEndTime() → ambil value "10:00" dari user
                // 2. .toLowerCase() → convert jadi "10:00"
                // 3. "%" + ... + "%" → tambah wildcard jadi "%10:00%"
                //
                // SQL: AND LOWER(end_time) LIKE '%10:00%'
                // Tujuan: Cari jadwal yang waktu selesainya MENGANDUNG "10:00"
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("endTime")),
                        "%" + filter.getEndTime().toLowerCase() + "%"
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 5: Filter by semester (kalau user isi)
            // ════════════════════════════════════════════════════���═══════════════
            if (filter.getSemester() != null && !filter.getSemester().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getSemester() → ambil value "1" dari user
                // 2. .toLowerCase() → convert jadi "1"
                // 3. "%" + ... + "%" → tambah wildcard jadi "%1%"
                //
                // SQL: AND LOWER(semester) LIKE '%1%'
                // Tujuan: Cari jadwal semester yang MENGANDUNG "1"
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("semester")),
                        "%" + filter.getSemester().toLowerCase() + "%"
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 6: Filter by classId (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getClassId() != null && !filter.getClassId().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getClassId() → ambil value "uuid-123" dari user
                // 2. root.get("clazz") → ambil relasi ke tabel Class
                // 3. .get("secureId") → ambil kolom secure_id dari tabel Class
                // 4. criteriaBuilder.equal() → operator "="
                //
                // SQL: AND class.secure_id = 'uuid-123'
                // Tujuan: Cari jadwal untuk kelas tertentu (EXACT match)
                predicates.add(
                    criteriaBuilder.equal(
                        root.get("clazz").get("secureId"),
                        filter.getClassId()
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 7: Filter by subjectId (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getSubjectId() != null && !filter.getSubjectId().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getSubjectId() → ambil value "uuid-456" dari user
                // 2. root.get("subject") → ambil relasi ke tabel Subject
                // 3. .get("secureId") → ambil kolom secure_id dari tabel Subject
                //
                // SQL: AND subject.secure_id = 'uuid-456'
                // Tujuan: Cari jadwal untuk mata pelajaran tertentu (EXACT match)
                predicates.add(
                    criteriaBuilder.equal(
                        root.get("subject").get("secureId"),
                        filter.getSubjectId()
                    )
                );
            }
            
            // ════════════════════════════════════════════════════════════════════
            // KONDISI 8: Filter by teacherId (kalau user isi)
            // ════════════════════════════════════════════════════════════════════
            if (filter.getTeacherId() != null && !filter.getTeacherId().trim().isEmpty()) {
                // Breakdown:
                // 1. filter.getTeacherId() → ambil value "uuid-789" dari user
                // 2. root.get("teacher") → ambil relasi ke tabel Teacher
                // 3. .get("secureId") → ambil kolom secure_id dari tabel Teacher
                //
                // SQL: AND teacher.secure_id = 'uuid-789'
                // Tujuan: Cari jadwal mengajar untuk guru tertentu (EXACT match)
                predicates.add(
                    criteriaBuilder.equal(
                        root.get("teacher").get("secureId"),
                        filter.getTeacherId()
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


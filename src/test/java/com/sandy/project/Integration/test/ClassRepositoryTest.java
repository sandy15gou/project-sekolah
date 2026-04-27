package com.sandy.project.Integration.test;

import com.sandy.project.Integration.BaseIntegrationTest;
import com.sandy.project.Integration.QueryCountAssert;
import com.sandy.project.Integration.QueryLogger;
import com.sandy.project.domain.Class;
import com.sandy.project.repository.ClassRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Test untuk ClassRepository
 *
 * Tujuan utama: Memverifikasi JUMLAH ROUNDTRIP ke database
 * untuk mendeteksi N+1 problem dan memastikan query efisien.
 */
@Transactional
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClassRepositoryTest extends BaseIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ClassRepositoryTest.class);

    @Autowired
    private ClassRepository classRepository;

    @BeforeEach
    void resetQueryCount() {
        QueryCountAssert.reset();
        QueryLogger.reset();
    }

    @Test
    @DisplayName("⚠️  N+1 Demo: findAll() + akses homeroomTeacher di setiap class → 1 + N SELECT")
    void testFindAll_withLazyTeacherAccess_demonstratesNPlusOneProblem() {

        // ─── FASE 1: findAll() ───────────────────────────────────────────────────
        log.warn("\n\n" +
            "┌──────────────────────────────────────────────────────────┐\n" +
            "│  FASE 1 — classRepository.findAll()                      │\n" +
            "│  Ekspektasi: 1 SELECT ke tabel classes                   │\n" +
            "└──────────────────────────────────────────────────────────┘");

        List<Class> classes = classRepository.findAll();
        assertEquals(2, classes.size());

        long selectFase1 = QueryCountAssert.getSelectCount();
        log.warn(
            "┌──────────────────────────────────────────────────────────┐\n" +
            "│  HASIL FASE 1                                             │\n" +
            "│  SELECT  : {}   │  Class dimuat: {}                       │\n" +
            "└──────────────────────────────────────────────────────────┘",
            selectFase1, classes.size());

        // ─── FASE 2: akses lazy homeroomTeacher per class ────────────────────────
        QueryCountAssert.reset();
        QueryLogger.reset();

        log.warn("\n" +
            "┌──────────────────────────────────────────────────────────┐\n" +
            "│  FASE 2 — Akses homeroomTeacher (lazy load)               │\n" +
            "│  Tiap class belum di-cache → 1 SELECT baru per akses      │\n" +
            "└──────────────────────────────────────────────────────────┘");

        StringBuilder detail = new StringBuilder();
        for (Class c : classes) {
            long before = QueryCountAssert.getSelectCount();
            String teacherName = (c.getHomeroomTeacher() != null)
                    ? c.getHomeroomTeacher().getName()
                    : "(no teacher)";
            long selectBaru = QueryCountAssert.getSelectCount() - before;
            detail.append(String.format(
                "  class=%-20s teacher=%-20s SELECT baru=%d%n",
                "'" + c.getClassName() + "'", "'" + teacherName + "'", selectBaru));
        }

        long selectFase2 = QueryCountAssert.getSelectCount();

        // ─── RINGKASAN ───────────────────────────────────────────────────────────
        log.warn("\n\n" +
            "╔══════════════════════════════════════════════════════════════╗\n" +
            "║          RINGKASAN ROUNDTRIP DATABASE                        ║\n" +
            "╠══════════════════════════════════════════════════════════════╣\n" +
            "║  Fase 1  findAll()          → {} SELECT                      ║\n" +
            "║  Fase 2  lazy load teacher  → {} SELECT  ← N+1 di sini!     ║\n" +
            "║  Jumlah class               → {}                             ║\n" +
            "║  Total roundtrip            → {} query                       ║\n" +
            "╠══════════════════════════════════════════════════════════════╣\n" +
            "║  Detail per class:                                           ║\n" +
            "{}║                                                              ║\n" +
            "╠══════════════════════════════════════════════════════════════╣\n" +
            "║  Solusi: pakai JOIN FETCH atau DTO Projection                ║\n" +
            "╚══════════════════════════════════════════════════════════════╝\n",
            selectFase1, selectFase2, classes.size(),
            selectFase1 + selectFase2, detail.toString());

        assertTrue(selectFase2 >= 1,
            "Lazy loading homeroomTeacher harus memicu minimal 1 SELECT tambahan (N+1)");
    }

    @Test
    @DisplayName("✅ JPA Projection: findAllClassQueryDTO() → hanya 1 SELECT dengan LEFT JOIN")
    void testFindAllClassQueryDTO_shouldExecuteOnlyOneQuery() {

        log.warn("\n\n" +
            "╔══════════════════════════════════════════════════════════════╗\n" +
            "║          TEST JPA PROJECTION - SOLUSI N+1 PROBLEM            ║\n" +
            "╠══════════════════════════════════════════════════════════════╣\n" +
            "║  Method: findAllClassQueryDTO()                               ║\n" +
            "║  Ekspektasi: HANYA 1 SELECT dengan LEFT JOIN                 ║\n" +
            "╚══════════════════════════════════════════════════════════════╝\n");

        var results = classRepository.findAllClassQueryDTO();

        long selectCount = QueryCountAssert.getSelectCount();

        StringBuilder detail = new StringBuilder();
        for (var dto : results) {
            detail.append(String.format(
                "  class=%-20s teacher=%-20s%n",
                "'" + dto.className() + "'",
                "'" + (dto.homeroomTeacherName() != null ? dto.homeroomTeacherName() : "(no teacher)") + "'"));
        }

        log.warn("\n" +
            "╔══════════════════════════════════════════════════════════════╗\n" +
            "║          HASIL QUERY JPA PROJECTION                           ║\n" +
            "╠══════════════════════════════════════════════════════════════╣\n" +
            "║  Total SELECT     → {} query (✅ N+1 SOLVED!)                ║\n" +
            "║  Class dimuat     → {}                                        ║\n" +
            "╠══════════════════════════════════════════════════════════════╣\n" +
            "║  Detail per class:                                           ║\n" +
            "{}║                                                              ║\n" +
            "╠══════════════════════════════════════════════════════════════╣\n" +
            "║  Kesimpulan: JPA Projection menggunakan 1 query JOIN         ║\n" +
            "║              untuk fetch Class + Teacher sekaligus            ║\n" +
            "╚══════════════════════════════════════════════════════════════╝\n",
            selectCount, results.size(), detail.toString());

        assertFalse(results.isEmpty(), "Harus ada data class dari test-data.sql");
        assertEquals(1, selectCount,
            "JPA Projection harus menghasilkan HANYA 1 SELECT query (bukan N+1)");
    }
}

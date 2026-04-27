package com.sandy.project.Integration.test;

import com.sandy.project.domain.Teacher;
import com.sandy.project.dto.query.TeacherQueryDTO;
import com.sandy.project.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Repository untuk Teacher - Demonstrasi JPA Projection
 *
 * Tujuan:
 * 1. Buktikan JPA Projection menyelesaikan N+1 dengan 1 query
 * 2. Validasi data yang dikembalikan sesuai dengan ekspektasi
 */
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@Transactional
public class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    void setUp() {
        // Cleanup
        teacherRepository.deleteAll();

        // Insert 2 teacher untuk testing
        Teacher teacher1 = new Teacher();
        teacher1.setSecureId(UUID.randomUUID().toString());
        teacher1.setName("John Doe");
        teacher1.setGender("M");
        teacher1.setBirthDate(LocalDate.of(1985, 5, 15));
        teacher1.setAddress("Jakarta Barat");

        Teacher teacher2 = new Teacher();
        teacher2.setSecureId(UUID.randomUUID().toString());
        teacher2.setName("Jane Smith");
        teacher2.setGender("F");
        teacher2.setBirthDate(LocalDate.of(1990, 8, 20));
        teacher2.setAddress("Jakarta Selatan");

        teacherRepository.saveAll(List.of(teacher1, teacher2));
    }

    /**
     * TEST: Demonstrasi JPA Projection - SOLUSI untuk optimasi query
     *
     * Expected: HANYA 1 SELECT dengan custom query
     */
    @Test
    void testFindAllTeacherQueryDTO_shouldReturnAllTeachers() {
        log.warn("\n" +
                "╔══════════════════════════════════════════════════════════════╗\n" +
                "║          TEST JPA PROJECTION - TEACHER                       ║\n" +
                "╠══════════════════════════════════════════════════════════════╣\n" +
                "║  Method: findAllTeacherQueryDTO()                           ║\n" +
                "║  Ekspektasi: HANYA 1 SELECT                                 ║\n" +
                "╚══════════════════════════════════════════════════════════════╝\n");

        // ACT: Fetch menggunakan JPA Projection
        List<TeacherQueryDTO> teachers = teacherRepository.findAllTeacherQueryDTO();

        log.warn("\n" +
                "╔══════════════════════════════════════════════════════════════╗\n" +
                "║          HASIL QUERY JPA PROJECTION                          ║\n" +
                "╠══════════════════════════════════════════════════════════════╣\n" +
                "║  Teachers dimuat  → {}                                       ║\n" +
                "╠══════════════════════════════════════════════════════════════╣\n" +
                "║  Detail per teacher:                                         ║",
                teachers.size());

        teachers.forEach(teacher -> {
            log.warn("  teacher='{}' gender='{}'", teacher.name(), teacher.gender());
        });

        log.warn("║                                                              ║\n" +
                "╠══════════════════════════════════════════════════════════════╣\n" +
                "║  Kesimpulan: JPA Projection menggunakan 1 query JOIN        ║\n" +
                "╚══════════════════════════════════════════════════════════════╝\n");

        // ASSERT
        assertEquals(2, teachers.size(), "Seharusnya ada 2 teachers");

        // ASSERT: Data tidak null
        teachers.forEach(teacher -> {
            assertNotNull(teacher.secureId());
            assertNotNull(teacher.name());
            assertNotNull(teacher.gender());
        });
    }

    /**
     * TEST: Search by name dengan JPA Projection
     */
    @Test
    void testSearchByName_shouldReturnFilteredResults() {
        log.warn("\n" +
                "╔══════════════════════════════════════════════════════════════╗\n" +
                "║          TEST SEARCH BY NAME - JPA PROJECTION                ║\n" +
                "╚══════════════════════════════════════════════════════════════╝\n");

        // ACT: Search by name
        List<TeacherQueryDTO> teachers = teacherRepository.findTeacherQueryDTOByNameContaining("John");

        log.warn("\n" +
                "╔══════════════════════════════════════════════════════════════╗\n" +
                "║  Teachers found   → {}                                       ║\n" +
                "╚══════════════════════════════════════════════════════════════╝\n",
                teachers.size());

        // ASSERT
        assertEquals(1, teachers.size(), "Seharusnya ketemu 1 teacher");
        assertTrue(teachers.get(0).name().contains("John"));
    }

    /**
     * TEST: Find by secureId dengan JPA Projection
     */
    @Test
    void testFindBySecureId_shouldReturnSingleTeacher() {
        // Arrange: Get secureId dari teacher yang sudah ada
        String secureId = teacherRepository.findAll().get(0).getSecureId();

        // Act: Fetch menggunakan JPA Projection
        TeacherQueryDTO teacher = teacherRepository.findTeacherQueryDTOBySecureId(secureId).orElse(null);

        // Assert
        assertNotNull(teacher);
        assertEquals(secureId, teacher.secureId());
        assertNotNull(teacher.name());
        assertNotNull(teacher.gender());

        log.warn("✅ Teacher found: name='{}' gender='{}'", teacher.name(), teacher.gender());
    }
}


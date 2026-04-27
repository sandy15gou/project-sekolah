package com.sandy.project.Integration.test;

import com.sandy.project.Integration.QueryLogger;
import com.sandy.project.ProjectSekolahApplication;
import com.sandy.project.domain.Subject;
import com.sandy.project.domain.Teacher;
import com.sandy.project.repository.SubjectRepository;
import com.sandy.project.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test untuk SubjectRepository
 * Tujuan: Menganalisis N+1 problem pada relasi Subject ↔ eligibleTeachers
 */
@SpringBootTest(
        classes = ProjectSekolahApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@Slf4j
public class SubjectRepositoryTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    @Transactional
    public void setUp() {
        // Reset query logger
        QueryLogger.reset();

        // Buat 3 teachers
        Teacher teacher1 = new Teacher();
        teacher1.setName("John Doe");
        teacher1.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher1.setGender("M");
        teacher1.setAddress("Address 1");

        Teacher teacher2 = new Teacher();
        teacher2.setName("Jane Smith");
        teacher2.setBirthDate(LocalDate.of(1985, 2, 2));
        teacher2.setGender("F");
        teacher2.setAddress("Address 2");

        Teacher teacher3 = new Teacher();
        teacher3.setName("Bob Wilson");
        teacher3.setBirthDate(LocalDate.of(1990, 3, 3));
        teacher3.setGender("M");
        teacher3.setAddress("Address 3");

        teacherRepository.saveAll(List.of(teacher1, teacher2, teacher3));

        // Buat 2 subjects dengan eligible teachers
        Subject math = new Subject();
        math.setName("Mathematics");
        math.setDescription("Math subject");
        math.setEligibleTeachers(new ArrayList<>(List.of(teacher1, teacher2)));

        Subject physics = new Subject();
        physics.setName("Physics");
        physics.setDescription("Physics subject");
        physics.setEligibleTeachers(new ArrayList<>(List.of(teacher2, teacher3)));

        subjectRepository.saveAll(List.of(math, physics));
        
        // Clear query count dari setup
        QueryLogger.reset();
    }

    @Test
    @Transactional
    public void testFindAllWithTeachers_shouldExecuteOptimizedQuery() {
        log.warn("\n" +
                "╔══════════════════════════════════════════════════════════════╗\n" +
                "║          TEST JPA JOIN FETCH - Subject + Teachers            ║\n" +
                "╠══════════════════════════════════════════════════════════════╣\n" +
                "║  Method: findAllWithTeachers()                                ║\n" +
                "║  Ekspektasi: 1 SELECT dengan LEFT JOIN FETCH                 ║\n" +
                "╚══════════════════════════════════════════════════════════════╝\n");

        // Reset query counter
        QueryLogger.reset();

        // FASE 1: Load subjects dengan teachers (optimized)
        List<Subject> subjects = subjectRepository.findAllWithTeachers();
        int queriesPhase1 = QueryLogger.getQueryCount();

        // FASE 2: Akses eligible teachers (should NOT trigger new queries)
        subjects.forEach(subject -> {
            log.info("Subject: {}, Teachers: {}",
                    subject.getName(),
                    subject.getEligibleTeachers().size());
            subject.getEligibleTeachers().forEach(teacher -> {
                log.info("  - Teacher: {}", teacher.getName());
            });
        });
        
        int totalQueries = QueryLogger.getQueryCount();

        // Assertions
        assertThat(subjects).hasSize(2);
        assertThat(queriesPhase1).isLessThanOrEqualTo(2); // 1-2 query karena DISTINCT + JOIN
        assertThat(totalQueries).isLessThanOrEqualTo(2); // Total tetap sama

        log.warn("\n" +
                "╔══════════════════════════════════════════════════════════════╗\n" +
                "║          HASIL TEST JPA JOIN FETCH                            ║\n" +
                "╠══════════════════════════════════════════════════════════════╣\n" +
                "║  Total SELECT     → " + totalQueries + " query (✅ OPTIMAL!)                    ║\n" +
                "║  Subjects dimuat  → " + subjects.size() + "                                        ║\n" +
                "╠══════════════════════════════════════════════════════════════╣\n" +
                "║  Kesimpulan: JOIN FETCH berhasil load Subject + Teachers     ║\n" +
                "║              dalam 1-2 query tanpa N+1 problem                ║\n" +
                "╚══════════════════════════════════════════════════════════════╝\n");
    }
}


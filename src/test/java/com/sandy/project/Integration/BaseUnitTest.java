package com.sandy.project.Integration;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Abstract Base Class untuk Unit Testing
 *
 * Fitur:
 * - MockitoExtension untuk @Mock dan @InjectMocks
 * - ActiveProfile test tanpa database real
 * - Tidak menggunakan Testcontainers (lebih cepat)
 *
 * Cara pakai:
 * 1. Extend class ini untuk unit test
 * 2. Gunakan @Mock dan @InjectMocks seperti biasa
 * 3. Fokus pada testing logic tanpa database
 *
 * @author Sandy
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public abstract class BaseUnitTest {
    
    /**
     * Helper method untuk print test information
     */
    protected void logTestStart(String testName) {
        System.out.println("=== STARTING " + testName + " ===");
    }
    
    /**
     * Helper method untuk print test completion
     */
    protected void logTestEnd(String testName) {
        System.out.println("=== COMPLETED " + testName + " ===");
    }
}


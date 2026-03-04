package com.sandy.project.Integration;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base class untuk Integration Test dengan PostgreSQL Testcontainer
 *
 * Semua test yang membutuhkan database real bisa extends class ini
 * Testcontainer akan otomatis start PostgreSQL container saat test dimulai
 * dan stop setelah test selesai
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
    
    @LocalServerPort
    protected int port;
    
    /**
     * PostgreSQL Testcontainer - shared across all tests in this class
     *
     * Container akan di-start sekali dan di-reuse untuk semua test method
     * untuk menghemat waktu startup
     */
    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("testdb-container")
                    .withUsername("test")
                    .withPassword("test");
    
    /**
     * Dynamic property source untuk override application properties
     *
     * Ini akan mengonfigurasi Spring untuk menggunakan database dari container
     * bukan database lokal yang didefinisikan di application.yml
     */
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Override datasource properties dengan detail dari container
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        
        // Print info untuk debugging
        System.out.println("=== PostgreSQL Testcontainer Started ===");
        System.out.println("JDBC URL: " + postgreSQLContainer.getJdbcUrl());
    }
    
    /**
     * Cleanup method yang dipanggil setelah semua test selesai
     */
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("=== PostgreSQL Testcontainer Stopped ===");
        }));
    }
}

package com.sandy.project.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DataSourceConfig {
    
    @Value("${spring.datasource.url}")
    private String dbUrl;
    
    @Value("${spring.datasource.username}")
    private String dbUsername;
    
    @Bean
    public CommandLineRunner testDatabaseConnection(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        return args -> {
            log.info("==== VERIFIKASI KONEKSI DATABASE ====");
            log.info("Database URL: {}", dbUrl);
            log.info("Database Username: {}", dbUsername);
            
            try {
                String dbStatus = jdbcTemplate.queryForObject("SELECT 'Database terkoneksi dengan baik' as status", String.class);
                log.info("Status Koneksi: {}", dbStatus);
                
                // Cek apakah table app_user ada
                Integer userCount = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM app_user", Integer.class);
                log.info("Jumlah user dalam database: {}", userCount);
                
                // Cek isi table app_user (tanpa menampilkan password)
                log.info("Daftar user dalam database:");
                jdbcTemplate.query(
                        "SELECT id, username, secure_id FROM app_user",
                        (rs, rowNum) -> {
                            log.info("User ID: {}, Username: {}, SecureID: {}",
                                    rs.getLong("id"),
                                    rs.getString("username"),
                                    rs.getString("secure_id"));
                            return null;
                        });
            } catch (Exception e) {
                log.error("Error koneksi database: ", e);
            }
        };
    }
}
package com.sandy.project.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class PasswordUtil {
    
    @Bean
    public CommandLineRunner testPasswordEncoder() {
        return args -> {
            String rawPassword = "sandy"; // kalau mau nambah role baru cukup ganti di sini aja pw ny,nanti yang hash ny bakalan muncul kalau di run
            // ,Sesuaikan dengan password yang digunakan(walaupun username dan passsword beda di kelas ini,tidak ngaruh ke postman)
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(rawPassword);
            
            log.info("==== PASSWORD DEBUG INFO ====");
            log.info("Raw password: {}", rawPassword);
            log.info("Encoded password: {}", encodedPassword);
            
            // Verify stored password
            String storedPasswordFromDb = "$2a$10$bN7OWEvi6rTqJEYbZxRU8uRCM8nPUQXKAWNy.x0ps6HR5/G8.YEZ2"; // Ganti dengan password di DB Anda
            boolean passwordMatches = encoder.matches(rawPassword, storedPasswordFromDb);
            log.info("Password matches with default: {}", passwordMatches);
        };
    }
}
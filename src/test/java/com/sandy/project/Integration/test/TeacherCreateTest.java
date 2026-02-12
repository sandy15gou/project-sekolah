package com.sandy.project.Integration.test;

import com.sandy.project.dto.TeacherCreateDTO;
import com.sandy.project.repository.TeacherRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeacherCreateTest {
    
    @LocalServerPort
    private Integer port;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        System.out.println("=== Test running on port: " + port + " ===");
    }
    
    @Test
    void contextLoads() {
        System.out.println("=== CONTEXT LOADS TEST - Port: " + port + " ===");
        assertNotNull(port, "Port should not be null");
    }
    
    @Test
    void testTeacherCreateTest_Success() {
        System.out.println("=== STARTING testTeacherCreateTest_Success ===");
        
        // Buat DTO menggunakan setter
        TeacherCreateDTO dto = new TeacherCreateDTO();
        dto.setTeacherName("nirkama");
        dto.setTeacherId("0002");
        // 1997-01-01 = LocalDate.of(1997, 1, 1).toEpochDay() = 9862
        dto.setTeacherBirthDate(9862L);  // Epoch days for 1997-01-01
        dto.setTeacherGender("L");  // L = Laki-laki, P = Perempuan
        dto.setTeacherAddress("Jl. Merdeka No. 02");
        
        System.out.println("=== Sending POST request to /v1/teacher ===");
        System.out.println("=== DTO: " + dto + " ===");
        
        long initSize = teacherRepository.count();
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(List.of(dto))
                .when()
                .post("/v1/teacher");
        
        System.out.println("=== Response Status: " + response.getStatusCode() + " ===");
        System.out.println("=== Response Body: " + response.getBody().asString() + " ===");
        
        response.then()
                .statusCode(201)
                .time(Matchers.lessThan(5000L));
        assertEquals(initSize + 1, teacherRepository.count());
    }
    
    @Test
    void testTeacherCreateTest_ValidationError() {
        
        TeacherCreateDTO dto = new TeacherCreateDTO();
        dto.setTeacherId("");
        dto.setTeacherBirthDate(10000L);  // Epoch days for 1997-05-19
        dto.setTeacherGender("L");  // L = Laki-laki, P = Perempuan
        dto.setTeacherAddress("Jl. Merdeka No. 03");
        
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(List.of(dto))
                .when()
                .post("/v1/teacher");
        response.then()
                .statusCode(400)
                .time(Matchers.lessThan(1000L));
        
    }
}

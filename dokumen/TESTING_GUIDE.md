# Testing Guide - Project Sekolah

## 📋 Overview

Project ini menggunakan **Abstract Base Classes** untuk menghilangkan boilerplate code dan menerapkan prinsip **DRY (Don't Repeat Yourself)** dalam testing.

---

## 🏗️ Struktur Testing

```
src/test/java/com/sandy/project/
├── Integration/
│   ├── BaseIntegrationTest.java      # Abstract class untuk Integration Test
│   ├── BaseUnitTest.java             # Abstract class untuk Unit Test
│   └── test/
│       └── TeacherCreateTest.java    # Contoh implementasi
```

---

## 🎯 BaseIntegrationTest

### Fitur
- ✅ **Testcontainers PostgreSQL** setup otomatis
- ✅ **RestAssured** configuration otomatis
- ✅ **Helper methods** untuk HTTP requests (GET, POST, PUT, PATCH, DELETE)
- ✅ **Logging** otomatis untuk debugging
- ✅ **DRY principle** implementation

### Cara Pakai

#### 1. Extend BaseIntegrationTest

```java
public class YourTest extends BaseIntegrationTest {
    
    @Autowired
    private YourRepository yourRepository;
    
    @Test
    void yourTest() {
        // Your test code here
    }
}
```

#### 2. Gunakan Helper Methods

```java
// POST Request
Response response = postRequest("/v1/endpoint", requestBody);

// GET Request
Response response = getRequest("/v1/endpoint");

// GET dengan path params
Response response = getRequest("/v1/endpoint/{id}", "123");

// PUT Request
Response response = putRequest("/v1/endpoint", requestBody);

// PATCH Request
Response response = patchRequest("/v1/endpoint", requestBody);

// DELETE Request
Response response = deleteRequest("/v1/endpoint");

// DELETE dengan path params
Response response = deleteRequest("/v1/endpoint/{id}", "123");

// Log Response
logResponse(response);
```

#### 3. Tidak Perlu Setup Lagi!

❌ **SEBELUM** (dengan boilerplate):
```java
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class YourTest {
    
    @LocalServerPort
    private Integer port;
    
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = 
            new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("testdb-container")
                .withUsername("test")
                .withPassword("test");
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
    
    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }
    
    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }
    
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }
    
    @Test
    void yourTest() {
        Response response = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(requestBody)
            .when()
            .post("/v1/endpoint");
            
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Body: " + response.getBody().asString());
    }
}
```

✅ **SESUDAH** (tanpa boilerplate):
```java
public class YourTest extends BaseIntegrationTest {
    
    @Test
    void yourTest() {
        Response response = postRequest("/v1/endpoint", requestBody);
        logResponse(response);
    }
}
```

**🎉 Pengurangan kode: ~70%**

---

## 🎯 BaseUnitTest

### Fitur
- ✅ **MockitoExtension** untuk @Mock dan @InjectMocks
- ✅ **Tanpa database** (lebih cepat)
- ✅ Helper methods untuk logging

### Cara Pakai

```java
public class YourServiceTest extends BaseUnitTest {
    
    @Mock
    private YourRepository yourRepository;
    
    @InjectMocks
    private YourService yourService;
    
    @Test
    void yourTest() {
        logTestStart("yourTest");
        
        // Mock behavior
        when(yourRepository.findById(1L))
            .thenReturn(Optional.of(entity));
        
        // Test logic
        YourEntity result = yourService.getById(1L);
        
        // Assertions
        assertNotNull(result);
        
        logTestEnd("yourTest");
    }
}
```

---

## 📊 Perbandingan

| Aspek | Tanpa Base Class | Dengan Base Class |
|-------|-----------------|-------------------|
| **Lines of Code** | ~50-70 lines | ~10-15 lines |
| **Boilerplate** | Banyak repetisi | Minimal |
| **Maintainability** | Sulit | Mudah |
| **Consistency** | Tidak konsisten | Konsisten |
| **Learning Curve** | Tinggi | Rendah |

---

## 🚀 Best Practices

### 1. Naming Convention
- Integration Test: `*IntegrationTest.java` atau `*Test.java`
- Unit Test: `*UnitTest.java` atau `*Test.java`

### 2. Test Organization
```
test/
├── TeacherCreateTest.java        # Teacher create operations
├── TeacherUpdateTest.java        # Teacher update operations
├── TeacherDeleteTest.java        # Teacher delete operations
└── TeacherGetTest.java           # Teacher get operations
```

### 3. Assertion Pattern
```java
// Arrange
TeacherCreateDTO dto = new TeacherCreateDTO();
dto.setTeacherName("John Doe");

// Act
Response response = postRequest("/v1/teacher", List.of(dto));

// Assert
response.then()
    .statusCode(201)
    .time(Matchers.lessThan(5000L));
```

---

## 🔧 Configuration

### application-test.yml
```yaml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
  jpa:
    database: POSTGRESQL
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true
```

### Key Points:
- ✅ Driver: PostgreSQL (bukan H2)
- ✅ Hibernate DDL: create-drop (auto reset setiap test)
- ✅ Show SQL: true (untuk debugging)

---

## 🐛 Troubleshooting

### 1. Driver Mismatch Error
```
Driver org.h2.Driver claims to not accept jdbcUrl, jdbc:postgresql://...
```

**Solusi:** Pastikan `application-test.yml` menggunakan `org.postgresql.Driver`

### 2. Container Not Starting
```
Could not find a valid Docker environment
```

**Solusi:** 
- Pastikan Docker Desktop running
- Check Docker socket accessible

### 3. Port Already in Use
```
Port 8080 is already in use
```

**Solusi:** Test menggunakan `RANDOM_PORT`, seharusnya tidak terjadi

---

## 📚 Dependencies

```xml
<!-- Testcontainers -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <scope>test</scope>
</dependency>

<!-- RestAssured -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>spring-mock-mvc</artifactId>
    <scope>test</scope>
</dependency>
```

---

## 🎓 Contoh Lengkap

Lihat `TeacherCreateTest.java` untuk contoh implementasi lengkap.

---

## 📞 Support

Jika ada pertanyaan atau issue, silakan hubungi tim development.

---

**Happy Testing! 🚀**


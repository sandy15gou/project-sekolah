# 🛡️ VALIDATION GUIDE - Best Practice

## 📌 **PRINSIP UTAMA**

> **Validasi HARUS di DTO, BUKAN di Domain/Entity!**

---

## 🎯 **KENAPA DTO?**

### **1. Separation of Concerns**
```
┌─────────────────────────────────────────┐
│  Layer        │  Responsibility         │
├─────────────────────────────────────────┤
│  DTO          │  Input Validation       │ ← ✅ VALIDASI DI SINI
│  Service      │  Business Logic         │
│  Domain       │  Database Mapping       │
│  Database     │  Constraint (NOT NULL)  │
└─────────────────────────────────────────┘
```

### **2. Fleksibilitas**
- **CREATE DTO** → Semua field wajib
- **UPDATE DTO** → Field opsional (partial update)
- **FILTER DTO** → Tidak butuh validasi (semua optional)

### **3. Error Message User-Friendly**
```java
// ✅ DTO Validation
@NotBlank(message = "Nama kelas tidak boleh kosong")
→ Response: { "className": "Nama kelas tidak boleh kosong" }

// ❌ Domain Validation
@Column(nullable = false)
→ Response: "could not execute statement; SQL [n/a]; nested exception..."
```

---

## 🏗️ **ALUR VALIDASI**

```
📱 User Send JSON
    ↓
🛡️ Controller (@Valid DTO)
    ↓ ✅ Validasi Success
⚙️ Service (Business Logic)
    ↓
🔄 Mapper (DTO → Entity)
    ↓
💾 Repository (Save)
    ↓
🗄️ Database (Check Constraint)
    ↓ ❌ Constraint Violation (nullable, unique, etc)
⚠️ Exception Handler
```

---

## 📚 **CONTOH IMPLEMENTASI**

### **1️⃣ CREATE DTO (Semua Field Wajib)**

```java
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClassRequestDTO {
    
    @NotBlank(message = "Nama kelas tidak boleh kosong")
    private String className;
    
    @NotBlank(message = "Tingkat kelas tidak boleh kosong")
    @Pattern(regexp = "^(10|11|12)$", message = "Tingkat kelas harus 10, 11, atau 12")
    private String gradeLevel;
    
    @NotBlank(message = "Tahun ajaran tidak boleh kosong")
    @Pattern(regexp = "^\\d{4}/\\d{4}$", message = "Format tahun ajaran: YYYY/YYYY")
    private String academicYear;
}
```

**Trigger Validasi di Controller:**
```java
@PostMapping
public ResponseEntity<Void> createClass(
    @RequestBody @Valid List<ClassRequestDTO> dtos  // ← @Valid trigger validasi!
) {
    classService.createNewClass(dtos);
    return ResponseEntity.status(HttpStatus.CREATED).build();
}
```

---

### **2️⃣ UPDATE DTO (Field Opsional)**

```java
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClassUpdateDTO {
    
    // ❌ Tidak pakai @NotBlank (biar bisa partial update)
    private String className;
    
    @Pattern(regexp = "^(10|11|12)$", message = "Tingkat kelas harus 10, 11, atau 12")
    private String gradeLevel;  // Tetap ada format validation
    
    @Pattern(regexp = "^\\d{4}/\\d{4}$", message = "Format tahun ajaran: YYYY/YYYY")
    private String academicYear;
}
```

**Request Example:**
```json
// ✅ Valid - Update hanya className
{
  "class_name": "XI IPA 2"
}

// ✅ Valid - Update semua field
{
  "class_name": "XI IPA 2",
  "grade_level": "11",
  "academic_year": "2025/2026"
}
```

---

### **3️⃣ FILTER DTO (Tidak Butuh Validasi)**

```java
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClassFilterDTO {
    
    // ❌ Tidak ada validasi annotation
    private String className;
    private String gradeLevel;
    private String academicYear;
    
    // Semua field optional untuk filtering
}
```

---

### **4️⃣ DOMAIN/ENTITY (Cuma Database Constraint)**

```java
@Entity
@Table(name = "classes")
public class Class extends AbstractBaseEntity {
    
    @Column(name = "class_name", nullable = false)  // ← Constraint DB
    private String className;
    
    @Column(name = "grade_level", nullable = false)
    private String gradeLevel;
    
    // ❌ TIDAK ADA @NotBlank, @NotNull, dll
}
```

---

## 🔧 **VALIDATION ANNOTATIONS**

### **Standard Annotations**

| Annotation | Kegunaan | Contoh |
|------------|----------|--------|
| `@NotNull` | Field tidak boleh null | `@NotNull(message = "ID tidak boleh null")` |
| `@NotBlank` | String tidak boleh null/empty/whitespace | `@NotBlank(message = "Nama tidak boleh kosong")` |
| `@NotEmpty` | Collection/Array tidak boleh empty | `@NotEmpty(message = "List tidak boleh kosong")` |
| `@Size` | Panjang string/collection | `@Size(min=3, max=100, message = "3-100 karakter")` |
| `@Min` / `@Max` | Nilai minimum/maksimum | `@Min(value=0, message = "Minimal 0")` |
| `@Email` | Format email | `@Email(message = "Format email tidak valid")` |
| `@Pattern` | Regex pattern | `@Pattern(regexp="^[A-Z].*", message = "Harus diawali huruf besar")` |

---

### **Custom Validator**

**1. Buat Annotation:**
```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidAuthorNameValidator.class)
public @interface ValidAuthorName {
    String message() default "Format nama tidak valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

**2. Buat Validator:**
```java
public class ValidAuthorNameValidator implements ConstraintValidator<ValidAuthorName, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // Let @NotBlank handle this
        }
        return value.matches("^[A-Z][a-zA-Z ]{2,}$");
    }
}
```

**3. Pakai di DTO:**
```java
@ValidAuthorName
@NotBlank(message = "Nama tidak boleh kosong")
private String studentName;
```

---

## ⚠️ **COMMON MISTAKES**

### **❌ SALAH: Validasi di Domain**
```java
@Entity
public class Student {
    @NotBlank  // ❌ JANGAN!
    private String name;
}
```

**Kenapa salah?**
- Domain dipake di semua layer (service, repository)
- Validasi akan trigger di semua operasi (find, update, delete)
- Tidak fleksibel untuk berbeda use case

---

### **✅ BENAR: Validasi di DTO**
```java
// CREATE DTO
public class StudentCreateDTO {
    @NotBlank(message = "Nama siswa tidak boleh kosong")
    private String studentName;
}

// UPDATE DTO
public class StudentUpdateDTO {
    // Opsional, biar bisa partial update
    private String studentName;
}
```

---

## 🎯 **SUMMARY**

| Komponen | Validasi Annotation | Database Constraint |
|----------|---------------------|---------------------|
| **DTO** | ✅ **YES** (@NotBlank, @Size, dll) | ❌ NO |
| **Domain/Entity** | ❌ **NO** | ✅ YES (@Column(nullable=false)) |

**Rule of Thumb:**
- **DTO** = Input validation (business rules)
- **Domain** = Database mapping (technical rules)

---

## 📖 **REFERENSI**

- **Project Examples:**
  - `StudentCreateDTO.java` ✅
  - `TeacherCreateDTO.java` ✅
  - `ClassRequestDTO.java` ✅

- **Validator Package:**
  - `com.sandy.project.validator.annotation.ValidAuthorName`
  - `com.sandy.project.validator.ValidAuthorNameValidator`

---

**💡 Tips:** Selalu gunakan `@Valid` di Controller untuk trigger validasi!

```java
@PostMapping
public ResponseEntity<Void> create(@RequestBody @Valid MyDTO dto) {
    // Validasi otomatis dijalankan sebelum masuk method
}
```


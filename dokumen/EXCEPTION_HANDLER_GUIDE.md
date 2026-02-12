# 📚 Panduan Exception Handler - Project Sekolah

## 🎯 Apa itu Exception Handler?

Exception Handler adalah **mekanisme penanganan error secara terpusat** di Spring Boot. Daripada setiap controller menangani error sendiri-sendiri, semua error ditangkap di SATU tempat.

---

## 🔄 Flow Kerja Exception Handler

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           FLOW EXCEPTION HANDLER                            │
└─────────────────────────────────────────────────────────────────────────────┘

  ┌──────────┐     ┌────────────┐     ┌─────────────┐     ┌──────────────────┐
  │  Client  │────▶│ Controller │────▶│   Service   │────▶│   Repository     │
  │ (Request)│     │            │     │             │     │                  │
  └──────────┘     └────────────┘     └─────────────┘     └──────────────────┘
                          │                  │
                          │                  │
                    ❌ Error Terjadi!    ❌ Error Terjadi!
                          │                  │
                          ▼                  ▼
                   ┌─────────────────────────────────────┐
                   │   EXCEPTION DILEMPAR (throw)        │
                   │   Contoh: throw new                 │
                   │   ResourceNotFoundException("...")  │
                   └─────────────────────────────────────┘
                                    │
                                    ▼
                   ┌─────────────────────────────────────┐
                   │   @ControllerAdvice                 │
                   │   ExceptionHandlerAdvice            │
                   │   (MENANGKAP EXCEPTION)             │
                   └─────────────────────────────────────┘
                                    │
                                    ▼
                   ┌─────────────────────────────────────┐
                   │   @ExceptionHandler                 │
                   │   Cari method yang cocok dengan     │
                   │   tipe exception                    │
                   └─────────────────────────────────────┘
                                    │
                                    ▼
                   ┌─────────────────────────────────────┐
                   │   Return ResponseEntity             │
                   │   (Error Response ke Client)        │
                   └─────────────────────────────────────┘
                                    │
                                    ▼
                          ┌──────────────┐
                          │   Client     │
                          │  (Response)  │
                          └──────────────┘
```

---

## 📝 Breakdown Kodingan

### 1. Class Declaration

```java
@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
```

| Bagian | Penjelasan | Tujuan |
|--------|------------|--------|
| `@ControllerAdvice` | Annotation Spring | Menandai class ini sebagai **PENGAWAS GLOBAL** untuk semua Controller |
| `extends ResponseEntityExceptionHandler` | Parent class bawaan Spring | Menyediakan method-method bawaan untuk handle exception umum |

**Analogi:**
```
Tanpa @ControllerAdvice:
┌────────────┐  ┌────────────┐  ┌────────────┐
│ Controller1│  │ Controller2│  │ Controller3│
│ try-catch  │  │ try-catch  │  │ try-catch  │  ← Handle sendiri (repot!)
└────────────┘  └────────────┘  └────────────┘

Dengan @ControllerAdvice:
┌────────────┐  ┌────────────┐  ┌────────────┐
│ Controller1│  │ Controller2│  │ Controller3│
└─────┬──────┘  └─────┬──────┘  └─────┬──────┘
      │               │               │
      └───────────────┼───────────────┘
                      ▼
         ┌────────────────────────┐
         │ ExceptionHandlerAdvice │  ← Handle terpusat! (rapi!)
         └────────────────────────┘
```

---

### 2. Handle ResourceNotFoundException

```java
@ExceptionHandler(ResourceNotFoundException.class)
protected ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
    ResourceNotFoundException ex, 
    WebRequest request
) {
    List<String> details = new ArrayList<String>();
    details.add(ex.getMessage());
    ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
        "data not found", 
        details, 
        ErrorCode.DATA_NOT_FOUND, 
        HttpStatus.BAD_REQUEST
    );
    return ResponseEntity.badRequest().body(errorResponse);
}
```

#### Breakdown per Baris:

| Baris | Kodingan | Penjelasan | Tujuan |
|-------|----------|------------|--------|
| 1 | `@ExceptionHandler(ResourceNotFoundException.class)` | Annotation | Menentukan tipe exception yang ditangkap oleh method ini |
| 2 | `ResourceNotFoundException ex` | Parameter | Exception yang ditangkap, bisa ambil message-nya |
| 3 | `WebRequest request` | Parameter | Info tentang request yang menyebabkan error |
| 4 | `List<String> details = new ArrayList<>()` | Variabel | Wadah untuk menyimpan detail error |
| 5 | `details.add(ex.getMessage())` | Method call | Ambil pesan error dari exception yang dilempar |
| 6-10 | `ErrorResponseDTO.of(...)` | Factory method | Membuat object response error dengan format konsisten |
| 11 | `return ResponseEntity.badRequest().body(errorResponse)` | Return | Kirim response dengan HTTP Status 400 + body error |

#### Diagram Flow:

```
Service melempar exception:
throw new ResourceNotFoundException("Student with id 999 not found")
                    │
                    ▼
┌─────────────────────────────────────────────────────────────┐
│ @ExceptionHandler(ResourceNotFoundException.class)          │
│                                                             │
│ Method ini dipanggil karena COCOK dengan tipe exception    │
└─────────────────────────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────────────────────┐
│ ex.getMessage() = "Student with id 999 not found"          │
│                                                             │
│ Pesan ini dimasukkan ke details                            │
└─────────────────────────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────────────────────┐
│ Client menerima response:                                   │
│ {                                                           │
│   "message": "data not found",                              │
│   "details": ["Student with id 999 not found"],             │
│   "errorCode": "DATA_NOT_FOUND",                            │
│   "status": 400                                             │
│ }                                                           │
└─────────────────────────────────────────────────────────────┘
```

---

### 3. Handle Validation Error

```java
@Override
protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex,
    HttpHeaders headers, 
    HttpStatusCode status, 
    WebRequest request
) {
    List<String> details = new ArrayList<String>();
    for(ObjectError error : ex.getBindingResult().getAllErrors()) {
        details.add(error.getDefaultMessage());
    }
    ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
        "invalid data", 
        details, 
        ErrorCode.INVALID_DATA, 
        HttpStatus.BAD_REQUEST
    );
    return ResponseEntity.badRequest().body(errorResponse);
}
```

#### Breakdown per Baris:

| Baris | Kodingan | Penjelasan | Tujuan |
|-------|----------|------------|--------|
| 1 | `@Override` | Annotation | Menandakan method ini meng-override method dari parent class |
| 2 | `MethodArgumentNotValidException ex` | Parameter | Exception yang dilempar saat validasi `@Valid` gagal |
| 3 | `ex.getBindingResult().getAllErrors()` | Method chain | Mengambil SEMUA error validasi yang terjadi |
| 4 | `for(ObjectError error : ...)` | Loop | Iterasi setiap error validasi |
| 5 | `error.getDefaultMessage()` | Method call | Ambil pesan error dari annotation validasi (contoh: "must not be empty") |

#### Diagram Flow:

```
┌────────────────────────────────────────────────────────────────┐
│ Client Request:                                                │
│ POST /students                                                 │
│ { "name": "", "email": "bukan-email" }                        │
└────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌────────────────────────────────────────────────────────────────┐
│ Controller dengan @Valid:                                      │
│                                                                │
│ @PostMapping                                                   │
│ public ResponseEntity<?> create(@Valid @RequestBody dto)      │
│                           ^^^^^^                               │
│                           Ini yang trigger validasi            │
└────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌────────────────────────────────────────────────────────────────┐
│ DTO dengan Annotation Validasi:                                │
│                                                                │
│ public class StudentDTO {                                      │
│     @NotBlank(message = "Name is required")                   │
│     private String name;                                       │
│                                                                │
│     @Email(message = "Email must be valid")                   │
│     private String email;                                      │
│ }                                                              │
└────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌────────────────────────────────────────────────────────────────┐
│ Validasi GAGAL!                                                │
│ - name kosong → error: "Name is required"                     │
│ - email invalid → error: "Email must be valid"                │
│                                                                │
│ Spring melempar: MethodArgumentNotValidException              │
└────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌────────────────────────────────────────────────────────────────┐
│ handleMethodArgumentNotValid() menangkap                       │
│                                                                │
│ Loop mengumpulkan SEMUA error:                                │
│ for(ObjectError error : ex.getBindingResult().getAllErrors()) │
│     details.add(error.getDefaultMessage());                   │
└────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌────────────────────────────────────────────────────────────────┐
│ Client menerima response:                                      │
│ {                                                              │
│   "message": "invalid data",                                  │
│   "details": [                                                │
│     "Name is required",                                       │
│     "Email must be valid"                                     │
│   ],                                                          │
│   "errorCode": "INVALID_DATA",                                │
│   "status": 400                                               │
│ }                                                              │
└────────────────────────────────────────────────────────────────┘
```

---

## 🎯 Contoh Real Case

### Case 1: Student Tidak Ditemukan

```java
// Di StudentService.java:
public Student getById(Long id) {
    return studentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
        //             │
        //             ▼
        // Exception dilempar jika student tidak ada di database
}
```

**Apa yang terjadi:**
```
1. Client request: GET /students/999
2. Service cari student dengan id 999
3. Student tidak ditemukan di database
4. Service throw ResourceNotFoundException
5. ExceptionHandlerAdvice menangkap
6. Method handleResourceNotFoundException() dipanggil
7. Client terima response error yang rapi
```

### Case 2: Validasi Gagal

```java
// Di StudentDTO.java:
public class StudentDTO {
    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Email must be valid")
    private String email;
    
    @Min(value = 1, message = "Age must be at least 1")
    private Integer age;
}

// Client kirim data:
// { "name": "", "email": "xxx", "age": -5 }
```

**Apa yang terjadi:**
```
1. Client request: POST /students dengan data invalid
2. Spring validasi DTO dengan @Valid
3. 3 validasi gagal:
   - name kosong
   - email tidak valid
   - age kurang dari 1
4. Spring throw MethodArgumentNotValidException
5. ExceptionHandlerAdvice menangkap
6. Method handleMethodArgumentNotValid() dipanggil
7. Semua error dikumpulkan dalam details
8. Client terima response dengan 3 error sekaligus
```

---

## 💡 Kenapa Pakai Pattern Ini?

| Keuntungan | Penjelasan |
|------------|------------|
| ✅ **Centralized** | Semua error handling di SATU tempat |
| ✅ **Consistent** | Response error SELALU format sama |
| ✅ **Clean Code** | Controller tidak perlu try-catch |
| ✅ **Maintainable** | Mudah tambah handler exception baru |
| ✅ **Readable** | Client dapat response yang jelas dan terstruktur |

---

## 📂 File Terkait

| File | Lokasi | Fungsi |
|------|--------|--------|
| `ExceptionHandlerAdvice.java` | `exception/` | Handler utama |
| `ResourceNotFoundException.java` | `exception/` | Custom exception untuk data tidak ditemukan |
| `ErrorResponseDTO.java` | `dto/` | Format response error |
| `ErrorCode.java` | `enums/` | Kode-kode error |

---

## 🔧 Cara Menambah Handler Baru

Jika ingin handle exception baru, tambahkan method di `ExceptionHandlerAdvice`:

```java
@ExceptionHandler(NamaExceptionBaru.class)
protected ResponseEntity<ErrorResponseDTO> handleNamaExceptionBaru(
    NamaExceptionBaru ex, 
    WebRequest request
) {
    List<String> details = new ArrayList<>();
    details.add(ex.getMessage());
    
    ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
        "pesan error",           // Pesan umum
        details,                 // Detail error
        ErrorCode.KODE_ERROR,    // Kode error (tambah di enum jika perlu)
        HttpStatus.BAD_REQUEST   // HTTP Status
    );
    
    return ResponseEntity.badRequest().body(errorResponse);
}
```

---

## 📌 Tips

1. **Jangan tangkap Exception umum** - Lebih baik tangkap exception spesifik
2. **Berikan pesan yang jelas** - Client harus paham apa yang salah
3. **Gunakan HTTP Status yang tepat** - 400 untuk client error, 500 untuk server error
4. **Log error** - Tambahkan logging untuk debugging


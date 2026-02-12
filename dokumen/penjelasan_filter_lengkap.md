# 🎓 PENJELASAN LENGKAP: CARA KERJA FITUR FILTER

---

## 📌 GAMBARAN UMUM (Big Picture)

Bayangkan kamu punya **buku telepon** dengan 1000 nama siswa. 
Filter = **Alat bantu cari** yang bisa kombinasi kriteria.

```
"Cari siswa LAKI-LAKI yang tinggal di JAKARTA dan umurnya 15-18 tahun"
```

---

## 🔄 ALUR KERJA (FLOW) - STEP BY STEP

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           ALUR FITUR FILTER                                  │
└─────────────────────────────────────────────────────────────────────────────┘

STEP 1: USER REQUEST (Browser/Postman)
┌────────────────────────────────────────────────────────────────────────────┐
│  GET /v1/students/filter?name=John&gender=M&minAge=15&page=0&size=10       │
│                                                                            │
│  "Hei Backend, tolong carikan siswa bernama John, laki-laki, umur 15+      │
│   tampilkan halaman 1 dengan 10 data per halaman"                          │
└────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
STEP 2: CONTROLLER (StudentResource.java) - PENERIMA REQUEST
┌────────────────────────────────────────────────────────────────────────────┐
│  @GetMapping("/v1/students/filter")                                        │
│  public ResponseEntity<...> filterStudents(                                │
│      @RequestParam(required = false) String name,     // "John"            │
│      @RequestParam(required = false) String gender,   // "M"               │
│      @RequestParam(required = false) Integer minAge,  // 15                │
│      @RequestParam(defaultValue = "0") int page,      // 0                 │
│      @RequestParam(defaultValue = "10") int size      // 10                │
│  )                                                                         │
│                                                                            │
│  Tugas: Terima parameter dari URL, bungkus ke DTO, kirim ke Service       │
│                                                                            │
│  StudentFilterDTO filter = StudentFilterDTO.builder()                      │
│      .name("John")                                                         │
│      .gender("M")                                                          │
│      .minAge(15)                                                           │
│      .build();                                                             │
│                                                                            │
│  studentService.filterStudents(filter, page, size, sortBy, sortDirection) │
└────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
STEP 3: FILTER DTO (StudentFilterDTO.java) - WADAH DATA FILTER
┌────────────────────────────────────────────────────────────────────────────┐
│  public class StudentFilterDTO {                                           │
│      private String name;        // ← "John"                               │
│      private String gender;      // ← "M"                                  │
│      private String address;     // ← null (tidak diisi user)              │
│      private LocalDate birthDateFrom;  // ← null                           │
│      private LocalDate birthDateTo;    // ← null                           │
│      private Integer minAge;     // ← 15                                   │
│      private Integer maxAge;     // ← null                                 │
│  }                                                                         │
│                                                                            │
│  Tugas: HANYA wadah untuk menyimpan kriteria filter dari user              │
│         Ibarat "formulir pencarian" yang diisi user                        │
└────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
STEP 4: SERVICE (StudentServiceImpl.java) - BUSINESS LOGIC
┌────────────────────────────────────────────────────────────────────────────┐
│  public PagedResponseDTO<StudentResponseDTO> filterStudents(               │
│      StudentFilterDTO filter, int page, int size, ...)                     │
│  {                                                                         │
│      // 1. Validasi input                                                  │
│      if (size > MAX_PAGE_SIZE) size = 50;                                  │
│                                                                            │
│      // 2. Buat Pageable (instruksi halaman mana, berapa data)             │
│      Pageable pageable = PageRequest.of(page, size, Sort.by(direction));   │
│                                                                            │
│      // 3. Build Specification (instruksi query dinamis)                   │
│      Specification<Student> spec = StudentSpecification.filterBy(filter);  │
│                                                                            │
│      // 4. Query ke database                                               │
│      Page<Student> result = studentRepository.findAll(spec, pageable);     │
│                                                                            │
│      // 5. Convert ke DTO dan return                                       │
│      return new PagedResponseDTO<>(result);                                │
│  }                                                                         │
│                                                                            │
│  Tugas: Koordinator - validasi, buat instruksi, kirim ke repository        │
└────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
STEP 5: SPECIFICATION (StudentSpecification.java) - QUERY BUILDER ⭐
┌────────────────────────────────────────────────────────────────────────────┐
│  INI BAGIAN PALING PENTING - MEMBANGUN QUERY SQL SECARA DINAMIS            │
│                                                                            │
│  public static Specification<Student> filterBy(StudentFilterDTO filter) {  │
│      return (root, query, criteriaBuilder) -> {                            │
│          List<Predicate> predicates = new ArrayList<>();                   │
│                                                                            │
│          // Selalu tambah kondisi: deleted = false                         │
│          predicates.add(cb.equal(root.get("deleted"), false));             │
│                                                                            │
│          // Kalau user isi name, tambah kondisi name                       │
│          if (filter.getName() != null) {                                   │
│              predicates.add(cb.like(lower(root.get("name")), "%john%"));   │
│          }                                                                 │
│                                                                            │
│          // Kalau user isi gender, tambah kondisi gender                   │
│          if (filter.getGender() != null) {                                 │
│              predicates.add(cb.equal(root.get("gender"), "M"));            │
│          }                                                                 │
│                                                                            │
│          // Gabungkan semua kondisi dengan AND                             │
│          return cb.and(predicates.toArray(new Predicate[0]));              │
│      };                                                                    │
│  }                                                                         │
│                                                                            │
│  Tugas: Bangun instruksi WHERE berdasarkan kriteria yang diisi user        │
└────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
STEP 6: REPOSITORY (StudentRepository.java) - AKSES DATABASE
┌────────────────────────────────────────────────────────────────────────────┐
│  public interface StudentRepository extends JpaRepository<Student, Long>,  │
│                                             JpaSpecificationExecutor<...>  │
│                                                                            │
│  // Method ini OTOMATIS ada dari JpaSpecificationExecutor                  │
│  Page<Student> findAll(Specification<Student> spec, Pageable pageable);    │
│                                                                            │
│  Tugas: Execute query ke database PostgreSQL                               │
│                                                                            │
│  QUERY YANG DIJALANKAN:                                                    │
│  SELECT * FROM student                                                     │
│  WHERE deleted = false                                                     │
│    AND LOWER(name) LIKE '%john%'                                           │
│    AND gender = 'M'                                                        │
│    AND birth_date <= '2011-01-12'  (umur minimal 15 tahun)                 │
│  ORDER BY name ASC                                                         │
│  LIMIT 10 OFFSET 0                                                         │
└────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
STEP 7: DATABASE (PostgreSQL)
┌────────────────────────────────────────────────────────────────────────────┐
│  +----+----------+--------+-----------+------------+                       │
│  | id | name     | gender | address   | birth_date |                       │
│  +----+----------+--------+-----------+------------+                       │
│  | 5  | John Doe | M      | Jakarta   | 2008-05-15 | ✅ MATCH              │
│  | 12 | Johnny   | M      | Bandung   | 2009-03-20 | ✅ MATCH              │
│  | 23 | Johnson  | M      | Surabaya  | 2007-11-10 | ✅ MATCH              │
│  +----+----------+--------+-----------+------------+                       │
│                                                                            │
│  Return: 3 data yang cocok dengan kriteria                                 │
└────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
STEP 8: RESPONSE KE USER
┌────────────────────────────────────────────────────────────────────────────┐
│  {                                                                         │
│    "content": [                                                            │
│      {"studentName": "John Doe", "birthDate": 13983},                      │
│      {"studentName": "Johnny", "birthDate": 14323},                        │
│      {"studentName": "Johnson", "birthDate": 13828}                        │
│    ],                                                                      │
│    "page": 0,                                                              │
│    "size": 10,                                                             │
│    "totalElements": 3,                                                     │
│    "totalPages": 1,                                                        │
│    "last": true                                                            │
│  }                                                                         │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## 📂 PENJELASAN SETIAP FILE DAN FUNGSINYA

### 1️⃣ **StudentFilterDTO.java** - Formulir Pencarian

```
ANALOGI: Formulir pendaftaran yang harus diisi
```

```java
public class StudentFilterDTO {
    private String name;           // Field 1: Nama (optional)
    private String gender;         // Field 2: Jenis kelamin (optional)
    private String address;        // Field 3: Alamat (optional)
    private LocalDate birthDateFrom; // Field 4: Tanggal lahir dari
    private LocalDate birthDateTo;   // Field 5: Tanggal lahir sampai
    private Integer minAge;        // Field 6: Umur minimal
    private Integer maxAge;        // Field 7: Umur maksimal
}
```

**Fungsi:**
- ✅ Menyimpan kriteria filter yang dikirim user
- ✅ Semua field OPTIONAL (boleh diisi, boleh tidak)
- ✅ Kalau tidak diisi = null = tidak dipakai untuk filter

**Contoh pengisian:**
```
User kirim: ?name=John&gender=M&minAge=15

Hasilnya di DTO:
- name = "John"      ← DIISI
- gender = "M"       ← DIISI
- address = null     ← TIDAK DIISI (diabaikan)
- birthDateFrom = null ← TIDAK DIISI (diabaikan)
- birthDateTo = null   ← TIDAK DIISI (diabaikan)
- minAge = 15        ← DIISI
- maxAge = null      ← TIDAK DIISI (diabaikan)
```

---

### 2️⃣ **StudentResource.java** - Pintu Masuk (Controller)

```
ANALOGI: Resepsionis yang menerima tamu dan mengarahkan ke ruangan yang tepat
```

```java
@GetMapping("/v1/students/filter")
public ResponseEntity<PagedResponseDTO<StudentResponseDTO>> filterStudents(
    @RequestParam(required = false) String name,      // Parameter dari URL
    @RequestParam(required = false) String gender,    
    @RequestParam(required = false) String address,   
    @RequestParam(required = false) String birthDateFrom,
    @RequestParam(required = false) String birthDateTo,
    @RequestParam(required = false) Integer minAge,
    @RequestParam(required = false) Integer maxAge,
    @RequestParam(defaultValue = "0") int page,       // Default halaman 0
    @RequestParam(defaultValue = "10") int size,      // Default 10 per halaman
    @RequestParam(defaultValue = "name") String sortBy,
    @RequestParam(defaultValue = "ASC") String sortDirection
)
```

**Penjelasan tiap bagian:**

| Kode | Penjelasan |
|------|------------|
| `@GetMapping("/v1/students/filter")` | URL endpoint yang bisa diakses |
| `@RequestParam(required = false)` | Parameter ini OPTIONAL, boleh tidak diisi |
| `@RequestParam(defaultValue = "0")` | Kalau tidak diisi, default nilainya "0" |
| `String name` | Variabel untuk menampung nilai dari URL |

**Proses di Controller:**
```java
// 1. Bungkus semua parameter ke dalam DTO
StudentFilterDTO filter = StudentFilterDTO.builder()
    .name(name)         // Masukkan nilai name dari URL
    .gender(gender)     // Masukkan nilai gender dari URL
    .address(address)   // dst...
    .build();

// 2. Kirim ke Service untuk diproses
PagedResponseDTO<StudentResponseDTO> response = 
    studentService.filterStudents(filter, page, size, sortBy, sortDirection);

// 3. Return hasil ke user
return ResponseEntity.ok(response);
```

---

### 3️⃣ **StudentServiceImpl.java** - Otak Bisnis (Business Logic)

```
ANALOGI: Manager yang koordinasi semua pekerjaan
```

```java
@Override
public PagedResponseDTO<StudentResponseDTO> filterStudents(
    StudentFilterDTO filter, 
    int page, 
    int size, 
    String sortBy, 
    String sortDirection
) {
    // STEP 1: Validasi input (jangan sampai ambil 1 juta data sekaligus)
    if (size > MAX_PAGE_SIZE) {
        size = MAX_PAGE_SIZE;  // Maksimal 50 data per halaman
    }
    
    // STEP 2: Validasi field sorting (keamanan)
    if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
        sortBy = "name";  // Kalau field tidak valid, pakai default
    }
    
    // STEP 3: Tentukan arah sorting
    Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") 
        ? Sort.Direction.DESC   // Z → A
        : Sort.Direction.ASC;   // A → Z
    
    // STEP 4: Buat instruksi pagination
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
    // Artinya: Ambil halaman ke-{page}, masing-masing {size} data, urutkan by {sortBy}
    
    // STEP 5: Bangun query dinamis dari filter
    Specification<Student> spec = StudentSpecification.filterBy(filter);
    
    // STEP 6: Jalankan query ke database
    Page<Student> studentPage = studentRepository.findAll(spec, pageable);
    
    // STEP 7: Convert hasil ke DTO yang aman untuk dikirim ke user
    Page<StudentResponseDTO> dtoPage = studentPage.map(student -> {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setStudentName(student.getName());
        dto.setBirthDate(student.getBirthDate().toEpochDay());
        return dto;
    });
    
    // STEP 8: Return hasil
    return new PagedResponseDTO<>(dtoPage);
}
```

---

### 4️⃣ **StudentSpecification.java** - Pembangun Query ⭐⭐⭐

```
ANALOGI: Tukang bangunan yang membangun query SQL sesuai pesanan
```

**INI BAGIAN PALING PENTING!**

```java
public static Specification<Student> filterBy(StudentFilterDTO filter) {
    return (root, query, criteriaBuilder) -> {
        // Wadah untuk menampung semua kondisi WHERE
        List<Predicate> predicates = new ArrayList<>();
        
        // ═══════════════════════════════════════════════════════
        // KONDISI 1: Selalu filter yang tidak dihapus
        // ═══════════════════════════════════════════════════════
        predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
        // SQL: WHERE deleted = false
        
        // ═══════════════════════════════════════════════════════
        // KONDISI 2: Filter by name (kalau user isi)
        // ═══════════════════════════════════════════════════════
        if (filter.getName() != null && !filter.getName().trim().isEmpty()) {
            predicates.add(
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),  // LOWER(name)
                    "%" + filter.getName().toLowerCase() + "%" // '%john%'
                )
            );
        }
        // SQL: AND LOWER(name) LIKE '%john%'
        
        // ═══════════════════════════════════════════════════════
        // KONDISI 3: Filter by gender (kalau user isi)
        // ═══════════════════════════════════════════════════════
        if (filter.getGender() != null && !filter.getGender().trim().isEmpty()) {
            predicates.add(
                criteriaBuilder.equal(
                    criteriaBuilder.upper(root.get("gender")),
                    filter.getGender().toUpperCase()
                )
            );
        }
        // SQL: AND UPPER(gender) = 'M'
        
        // ═══════════════════════════════════════════════════════
        // KONDISI 4: Filter by minimum age (kalau user isi)
        // ═══════════════════════════════════════════════════════
        if (filter.getMinAge() != null) {
            // Logika: minAge = 15 → birthDate harus <= (sekarang - 15 tahun)
            LocalDate maxBirthDate = LocalDate.now().minusYears(filter.getMinAge());
            predicates.add(
                criteriaBuilder.lessThanOrEqualTo(
                    root.get("birthDate"),
                    maxBirthDate
                )
            );
        }
        // SQL: AND birth_date <= '2011-01-12'
        
        // ═══════════════════════════════════════════════════════
        // GABUNGKAN SEMUA KONDISI DENGAN AND
        // ═══════════════════════════════════════════════════════
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
}
```

**HASIL AKHIR QUERY:**
```sql
SELECT * FROM student
WHERE deleted = false
  AND LOWER(name) LIKE '%john%'
  AND UPPER(gender) = 'M'
  AND birth_date <= '2011-01-12'
ORDER BY name ASC
LIMIT 10 OFFSET 0
```

---

### 5️⃣ **StudentRepository.java** - Akses Database

```
ANALOGI: Kurir yang mengambil barang dari gudang (database)
```

```java
public interface StudentRepository extends 
    JpaRepository<Student, Long>,           // Method CRUD standar
    JpaSpecificationExecutor<Student>       // Method untuk query dinamis
{
    // Method ini OTOMATIS tersedia dari JpaSpecificationExecutor:
    // Page<Student> findAll(Specification<Student> spec, Pageable pageable);
}
```

**Fungsi:**
- ✅ Execute query ke database
- ✅ `JpaSpecificationExecutor` = bisa terima Specification (query dinamis)
- ✅ Method `findAll(spec, pageable)` otomatis ada

---

## 🔍 PENJELASAN DETAIL: KODINGAN FILTER NAME

Mari bedah baris per baris:

```java
if (filter.getName() != null && !filter.getName().trim().isEmpty()) {
    predicates.add(
        criteriaBuilder.like(
            criteriaBuilder.lower(root.get("name")),
            "%" + filter.getName().toLowerCase() + "%"
        )
    );
}
```

### Baris 1: Pengecekan
```java
if (filter.getName() != null && !filter.getName().trim().isEmpty())
```

| Bagian | Penjelasan |
|--------|------------|
| `filter.getName()` | Ambil nilai name dari DTO |
| `!= null` | Pastikan user mengisi field ini |
| `!...trim().isEmpty()` | Pastikan bukan string kosong atau spasi doang |

**Tujuan:** Hanya proses kalau user benar-benar mengisi nilai name

### Baris 2: Tambah Kondisi
```java
predicates.add(...)
```

| Bagian | Penjelasan |
|--------|------------|
| `predicates` | List yang menampung semua kondisi WHERE |
| `.add(...)` | Tambahkan kondisi baru ke dalam list |

### Baris 3: Operasi LIKE
```java
criteriaBuilder.like(...)
```

| Bagian | Penjelasan |
|--------|------------|
| `criteriaBuilder` | Tool untuk membangun kondisi SQL |
| `.like(...)` | Operasi SQL LIKE untuk partial match |

**SQL Equivalent:** `... LIKE ...`

### Baris 4: Lower Case
```java
criteriaBuilder.lower(root.get("name"))
```

| Bagian | Penjelasan |
|--------|------------|
| `root` | Representasi tabel Student |
| `.get("name")` | Ambil kolom "name" dari tabel |
| `criteriaBuilder.lower(...)` | Ubah ke huruf kecil |

**SQL Equivalent:** `LOWER(name)`

**Tujuan:** Biar pencarian case-insensitive (John = john = JOHN)

### Baris 5: Pattern Matching
```java
"%" + filter.getName().toLowerCase() + "%"
```

| Bagian | Penjelasan |
|--------|------------|
| `filter.getName()` | Ambil input user, misal "John" |
| `.toLowerCase()` | Ubah ke "john" |
| `"%" + ... + "%"` | Tambah wildcard, jadi "%john%" |

**Contoh:**
```
Input user: "John"
Hasil:      "%john%"

Akan match:
✅ "john"       → mengandung "john"
✅ "John Doe"   → mengandung "john" (setelah di-lower)
✅ "Johnny"     → mengandung "john"
✅ "Big Johnson" → mengandung "john"
```

**SQL Equivalent:** `LOWER(name) LIKE '%john%'`

---

## 🎯 KENAPA PAKAI SPECIFICATION? (Bukan Query Biasa)

### ❌ Cara Lama (Hardcode Query):
```java
// Harus bikin banyak method untuk setiap kombinasi filter
List<Student> findByName(String name);
List<Student> findByGender(String gender);
List<Student> findByNameAndGender(String name, String gender);
List<Student> findByNameAndGenderAndAddress(String name, String gender, String address);
// ... dan seterusnya (bisa 100+ method!)
```

### ✅ Cara Specification (Dynamic Query):
```java
// Cukup 1 method, query dibangun dinamis
Specification<Student> spec = StudentSpecification.filterBy(filter);
studentRepository.findAll(spec, pageable);
```

**Keuntungan:**
- ✅ 1 method untuk semua kombinasi filter
- ✅ Query dibangun otomatis berdasarkan field yang diisi
- ✅ Maintainable dan scalable
- ✅ Mudah ditambah filter baru

---

## 📊 CONTOH SKENARIO PENGGUNAAN

### Skenario 1: Filter hanya by name
```
Request: GET /v1/students/filter?name=John

DTO yang terisi:
- name = "John" ✅
- gender = null
- address = null
- minAge = null
- maxAge = null

Query yang dijalankan:
SELECT * FROM student WHERE deleted = false AND LOWER(name) LIKE '%john%'
```

### Skenario 2: Filter kombinasi
```
Request: GET /v1/students/filter?name=John&gender=M&minAge=15

DTO yang terisi:
- name = "John" ✅
- gender = "M" ✅
- address = null
- minAge = 15 ✅
- maxAge = null

Query yang dijalankan:
SELECT * FROM student 
WHERE deleted = false 
  AND LOWER(name) LIKE '%john%'
  AND UPPER(gender) = 'M'
  AND birth_date <= '2011-01-12'
```

### Skenario 3: Tanpa filter (ambil semua)
```
Request: GET /v1/students/filter?page=0&size=10

DTO yang terisi:
- name = null
- gender = null
- address = null
- minAge = null
- maxAge = null

Query yang dijalankan:
SELECT * FROM student WHERE deleted = false
LIMIT 10 OFFSET 0
```

---

## 🔗 HUBUNGAN ANTAR KOMPONEN

```
┌────────────┐     ┌──────────────────┐     ┌──────────────────────┐
│  Browser   │────▶│  StudentResource │────▶│  StudentServiceImpl  │
│  (User)    │     │   (Controller)   │     │     (Service)        │
└────────────┘     └──────────────────┘     └──────────────────────┘
                          │                          │
                          │                          ▼
                          │                  ┌───────────────────────┐
                          │                  │ StudentSpecification  │
                          │                  │   (Query Builder)     │
                          │                  └───────────────────────┘
                          │                          │
                          ▼                          ▼
                   ┌──────────────────┐     ┌───────────────────────┐
                   │ StudentFilterDTO │     │  StudentRepository    │
                   │    (Wadah)       │     │ (Database Access)     │
                   └──────────────────┘     └───────────────────────┘
                                                     │
                                                     ▼
                                            ┌───────────────────────┐
                                            │   PostgreSQL Database │
                                            └───────────────────────┘
```

**Summary singkat:**
1. **User** → kirim request dengan parameter filter
2. **Controller** → terima parameter, bungkus ke DTO
3. **Service** → validasi, koordinasi, panggil specification
4. **Specification** → bangun query SQL dinamis
5. **Repository** → execute query ke database
6. **Database** → return hasil yang cocok
7. **Response** → kirim balik ke user dalam format JSON

---

Semoga penjelasan ini membantu kamu memahami cara kerja fitur filter! 🎉


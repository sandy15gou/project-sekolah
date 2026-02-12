# 📋 CLASS FILTER - DOKUMENTASI SINGKAT

## ✅ FITUR FILTER CLASS SUDAH SELESAI!

---

## 📂 FILE YANG DIBUAT/DIUBAH

### 1️⃣ **ClassFilterDTO.java** ✅ BARU
**Lokasi**: `src/main/java/com/sandy/project/dto/ClassFilterDTO.java`

**Field Filter:**
- `className` - Filter nama kelas (partial match, case insensitive)
- `gradeLevel` - Filter tingkat kelas (partial match, case insensitive)
- `academicYear` - Filter tahun ajaran (partial match, case insensitive)
- `minCapacity` - Filter kapasitas minimal (>=)
- `maxCapacity` - Filter kapasitas maksimal (<=)

---

### 2️⃣ **ClassSpecification.java** ✅ BARU
**Lokasi**: `src/main/java/com/sandy/project/specification/ClassSpecification.java`

**Fungsi**: Build dynamic query SQL berdasarkan filter yang diisi user

**Query Logic:**
```sql
SELECT * FROM classes
WHERE deleted = false
  AND LOWER(class_name) LIKE '%x%'          -- kalau className diisi
  AND LOWER(grade_level) LIKE '%10%'        -- kalau gradeLevel diisi
  AND LOWER(academic_year) LIKE '%2024%'    -- kalau academicYear diisi
  AND max_capacity >= 20                    -- kalau minCapacity diisi
  AND max_capacity <= 40                    -- kalau maxCapacity diisi
ORDER BY class_name ASC
LIMIT 10 OFFSET 0
```

---

### 3️⃣ **ClassRepository.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/repository/ClassRepository.java`

**Perubahan**: 
- Tambah `JpaSpecificationExecutor<Class>`
- Sekarang bisa terima Specification untuk dynamic query

---

### 4️⃣ **ClassService.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/service/ClassService.java`

**Method Baru**:
```java
PagedResponseDTO<ClassResponseDTO> filterClasses(
    ClassFilterDTO filter, 
    int page, 
    int size, 
    String sortBy, 
    String sortDirection
)
```

---

### 5️⃣ **ClassServiceImpl.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/service/impl/ClassServiceImpl.java`

**Implementasi**: 
- Import `ClassSpecification` dan `Specification`
- Implement method `filterClasses()`
- Validasi input, build spec, query ke DB, convert ke DTO

---

### 6️⃣ **ClassResource.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/web/ClassResource.java`

**Endpoint Baru**:
```
GET /v1/classes/filter
```

**Parameters:**
- `className` (optional) - Filter nama kelas
- `gradeLevel` (optional) - Filter tingkat
- `academicYear` (optional) - Filter tahun ajaran
- `minCapacity` (optional) - Kapasitas minimal
- `maxCapacity` (optional) - Kapasitas maksimal
- `page` (default: 0) - Halaman
- `size` (default: 10) - Data per halaman
- `sortBy` (default: className) - Field sorting
- `sortDirection` (default: ASC) - Arah sorting

---

## 🚀 CARA PENGGUNAAN

### Contoh 1: Filter by className
```
GET /v1/classes/filter?className=X
```
**Hasil**: Semua kelas yang mengandung "X" (X-1, X-2, XI-A, dll)

---

### Contoh 2: Filter by gradeLevel
```
GET /v1/classes/filter?gradeLevel=10
```
**Hasil**: Semua kelas tingkat 10

---

### Contoh 3: Filter by academicYear
```
GET /v1/classes/filter?academicYear=2024
```
**Hasil**: Semua kelas tahun ajaran 2024 (2024/2025, 2024-2025, dll)

---

### Contoh 4: Filter by capacity range
```
GET /v1/classes/filter?minCapacity=25&maxCapacity=35
```
**Hasil**: Kelas dengan kapasitas 25-35 siswa

---

### Contoh 5: Filter kombinasi
```
GET /v1/classes/filter?className=X&gradeLevel=10&academicYear=2024&minCapacity=25&page=0&size=10&sortBy=className&sortDirection=ASC
```
**Hasil**: Kelas yang memenuhi SEMUA kriteria di atas (AND logic)

---

### Contoh 6: Filter dengan pagination
```
GET /v1/classes/filter?academicYear=2024&page=1&size=20&sortBy=gradeLevel&sortDirection=DESC
```
**Hasil**: Halaman ke-2 (20 data per halaman), sorted by gradeLevel descending

---

## 📊 RESPONSE FORMAT

```json
{
  "content": [
    {
      "secureId": "uuid-123",
      "className": "X-1",
      "gradeLevel": "10",
      "academicYear": "2024/2025"
    },
    {
      "secureId": "uuid-456",
      "className": "X-2",
      "gradeLevel": "10",
      "academicYear": "2024/2025"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 2,
  "totalPages": 1,
  "last": true,
  "first": true
}
```

---

## 🔍 CARA KERJA (FLOW)

```
1. USER REQUEST
   ↓
   GET /v1/classes/filter?className=X&gradeLevel=10

2. CONTROLLER (ClassResource)
   ↓
   - Terima parameters dari URL
   - Build ClassFilterDTO
   - Call classService.filterClasses()

3. SERVICE (ClassServiceImpl)
   ↓
   - Validasi input (size, sortBy, sortDirection)
   - Build Pageable
   - Build Specification dari ClassFilterDTO
   - Query ke repository

4. SPECIFICATION (ClassSpecification)
   ↓
   - Build dynamic query WHERE conditions
   - Combine dengan AND logic

5. REPOSITORY (ClassRepository)
   ↓
   - Execute query: findAll(spec, pageable)
   - Return Page<Class>

6. DATABASE (PostgreSQL)
   ↓
   - Execute SQL query
   - Return matching records

7. RESPONSE
   ↓
   - Convert Class entity → ClassResponseDTO
   - Wrap ke PagedResponseDTO
   - Return JSON ke user
```

---

## ✅ VALIDASI & KEAMANAN

1. **Size Limit**: Maksimal 50 data per halaman
2. **Sort Field Whitelist**: Hanya field yang diizinkan (className, gradeLevel, academicYear, createdAt)
3. **Soft Delete**: Selalu filter `deleted = false`
4. **Case Insensitive**: Semua string search tidak case sensitive
5. **Partial Match**: Support pencarian sebagian kata (LIKE query)

---

## 🎯 KEUNTUNGAN PAKAI SPECIFICATION

✅ **1 endpoint untuk semua kombinasi filter**
- Tidak perlu buat endpoint terpisah untuk setiap kombinasi
- User bisa kombinasi filter sesuka hati

✅ **Dynamic Query**
- Query dibangun otomatis berdasarkan field yang diisi
- Field yang null diabaikan

✅ **Maintainable & Scalable**
- Mudah tambah filter baru
- Code clean dan terstruktur

✅ **Type Safe**
- Compile-time checking
- Tidak ada hardcoded SQL string

---

## 📝 TESTED?

Fitur sudah dibuat dengan struktur yang sama dengan Student dan Teacher filter yang sudah ada. 

**Yang perlu di-test:**
1. Filter by className only
2. Filter by gradeLevel only
3. Filter by academicYear only
4. Filter by capacity range
5. Filter kombinasi multiple criteria
6. Pagination & sorting
7. Edge cases (empty result, invalid input, dll)

---

## 🎉 SELESAI!

Fitur filter untuk Class sudah lengkap mengikuti pattern yang sama dengan Student dan Teacher filter!


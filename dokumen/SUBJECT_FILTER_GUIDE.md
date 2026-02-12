# 📚 SUBJECT FILTER - DOKUMENTASI LENGKAP

## ✅ FITUR FILTER SUBJECT SUDAH SELESAI!

---

## 📂 FILE YANG DIBUAT/DIUBAH

### 1️⃣ **SubjectFilterDTO.java** ✅ BARU
**Lokasi**: `src/main/java/com/sandy/project/dto/SubjectFilterDTO.java`

**Field Filter:**
- `name` (String) - Filter nama mata pelajaran (partial match, case insensitive)
- `description` (String) - Filter deskripsi (partial match, case insensitive)

**Catatan**: Subject adalah entity paling sederhana dengan hanya 2 field string untuk filter

---

### 2️⃣ **SubjectSpecification.java** ✅ BARU
**Lokasi**: `src/main/java/com/sandy/project/specification/SubjectSpecification.java`

**Fungsi**: Build dynamic query SQL berdasarkan filter yang diisi user

**Query Logic:**
```sql
SELECT * FROM subjects
WHERE deleted = false
  AND LOWER(name) LIKE '%math%'              -- kalau name diisi
  AND LOWER(description) LIKE '%science%'    -- kalau description diisi
ORDER BY name ASC
LIMIT 10 OFFSET 0
```

---

### 3️⃣ **SubjectRepository.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/repository/SubjectRepository.java`

**Perubahan**: 
- Tambah `JpaSpecificationExecutor<Subject>`
- Sekarang bisa terima Specification untuk dynamic query

---

### 4️⃣ **SubjectService.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/service/SubjectService.java`

**Method Baru**:
```java
PagedResponseDTO<SubjectResponseDTO> filterSubjects(
    SubjectFilterDTO filter, 
    int page, 
    int size, 
    String sortBy, 
    String sortDirection
)
```

---

### 5️⃣ **SubjectServiceImpl.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/service/impl/SubjectServiceImpl.java`

**Implementasi**: 
- Import `SubjectSpecification` dan `Specification`
- Implement method `filterSubjects()`
- Validasi input, build spec, query ke DB, convert ke DTO

---

### 6️⃣ **SubjectResource.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/web/SubjectResource.java`

**Endpoint Baru**:
```
GET /v1/subjects/filter
```

**Parameters:**
- `name` (optional) - Nama mata pelajaran
- `description` (optional) - Deskripsi
- `page` (default: 0) - Halaman
- `size` (default: 10) - Data per halaman
- `sortBy` (default: name) - Field sorting
- `sortDirection` (default: ASC) - Arah sorting

---

## 🚀 CARA PENGGUNAAN

### Contoh 1: Filter by name
```
GET /v1/subjects/filter?name=Math
```
**Hasil**: Semua mata pelajaran yang namanya mengandung "Math"

**Breakdown:**
- Input user: "Math"
- Diconvert: "math" (lowercase)
- Pattern: "%math%" (wildcard)
- SQL: `LOWER(name) LIKE '%math%'`
- Tujuan: Cari mata pelajaran yang namanya MENGANDUNG "math"

**Match:**
- ✅ "Mathematics"
- ✅ "Math Basic"
- ✅ "Applied Math"
- ✅ "Math Advanced"

---

### Contoh 2: Filter by description
```
GET /v1/subjects/filter?description=science
```
**Hasil**: Mata pelajaran dengan deskripsi yang mengandung "science"

**Breakdown:**
- Input user: "science"
- Pattern: "%science%"
- SQL: `LOWER(description) LIKE '%science%'`
- Tujuan: Cari berdasarkan kata kunci dalam deskripsi

**Use case:**
- Cari semua mata pelajaran kategori science
- Filter by keyword dalam deskripsi

---

### Contoh 3: Filter kombinasi
```
GET /v1/subjects/filter?name=English&description=language
```
**Hasil**: Mata pelajaran dengan nama "English" dan deskripsi "language"

**Breakdown:**
- name: "English" → `LIKE '%english%'`
- description: "language" → `LIKE '%language%'`
- SQL: WHERE ... AND name LIKE '%english%' AND description LIKE '%language%'

---

### Contoh 4: Filter dengan pagination & sorting
```
GET /v1/subjects/filter?name=Math&page=0&size=20&sortBy=name&sortDirection=ASC
```
**Hasil**: Mata pelajaran yang mengandung "Math", halaman 1, 20 data, sort A-Z

---

### Contoh 5: Semua mata pelajaran (tanpa filter)
```
GET /v1/subjects/filter?page=0&size=10
```
**Hasil**: Semua mata pelajaran, halaman 1, 10 data per halaman

---

## 📊 RESPONSE FORMAT

```json
{
  "content": [
    {
      "secureId": "uuid-subject-1",
      "name": "Mathematics"
    },
    {
      "secureId": "uuid-subject-2",
      "name": "Math Basic"
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
   GET /v1/subjects/filter?name=Math&description=basic

2. CONTROLLER (SubjectResource)
   ↓
   - Terima parameters dari URL
   - Build SubjectFilterDTO:
     * name = "Math"
     * description = "basic"
   - Call subjectService.filterSubjects()

3. SERVICE (SubjectServiceImpl)
   ↓
   - Validasi input (size ≤ 50, sortBy valid, direction valid)
   - Build Pageable (page, size, sort)
   - Build Specification dari SubjectFilterDTO
   - Query: subjectRepository.findAll(spec, pageable)

4. SPECIFICATION (SubjectSpecification)
   ↓
   - Cek field yang tidak null:
     * name = "Math" → LIKE '%math%'
     * description = "basic" → LIKE '%basic%'
   - Gabung dengan AND
   - Return Predicate

5. REPOSITORY (SubjectRepository)
   ↓
   - Execute query dengan JpaSpecificationExecutor
   - Return Page<Subject>

6. DATABASE (PostgreSQL)
   ↓
   SELECT * FROM subjects
   WHERE deleted = false
     AND LOWER(name) LIKE '%math%'
     AND LOWER(description) LIKE '%basic%'
   ORDER BY name ASC
   LIMIT 10

7. RESPONSE
   ↓
   - Convert Subject → SubjectResponseDTO
   - Wrap ke PagedResponseDTO
   - Return JSON ke user
```

---

## 💡 USE CASES

### Use Case 1: Cari Mata Pelajaran by Nama
```
GET /v1/subjects/filter?name=English
```
→ Tampilkan semua mata pelajaran yang mengandung "English"

---

### Use Case 2: Cari by Kategori (via description)
```
GET /v1/subjects/filter?description=science
```
→ Tampilkan semua mata pelajaran kategori science

---

### Use Case 3: Search Bar (autocomplete)
```
GET /v1/subjects/filter?name=Ma&size=5
```
→ Untuk autocomplete di search bar (tampilkan 5 hasil teratas)

---

### Use Case 4: Browse by Keyword
```
GET /v1/subjects/filter?description=art
```
→ Browse mata pelajaran yang related dengan "art"

---

## ✅ VALIDASI & KEAMANAN

1. **Size Limit**: Max 50 data per halaman
2. **Sort Field Whitelist**: name, createdAt
3. **Soft Delete**: Selalu filter `deleted = false`
4. **Case Insensitive**: String search tidak case sensitive
5. **Secure ID**: Pakai UUID, bukan ID internal

---

## 📊 PERBANDINGAN DENGAN FILTER LAIN

| Feature | Student/Teacher | Class | Schedule | Score | **Subject** |
|---------|----------------|-------|----------|-------|-------------|
| String Filter | ✅ 3 field | ✅ 3 field | ✅ 4 field | ✅ 1 field | **✅ 2 field** |
| Range Filter | ✅ Date/Age | ✅ Capacity | ❌ | ✅ Score | **❌** |
| Relational Filter | ❌ | ❌ | ✅ 3 relasi | ✅ 2 relasi | **❌** |
| **Complexity** | Medium | Medium | Complex | Complex | **Simple** |

**Keunikan Subject Filter:**
- ⭐ **Paling Sederhana** - hanya 2 field string
- ⭐ **Perfect untuk Search** - name & description lookup
- ⭐ **Lightweight** - fast query, no JOIN
- ⭐ **Easy to Use** - simple API, straightforward

---

## 🎯 KENAPA SUBJECT FILTER SIMPEL?

### **1. Entity Sederhana**
Subject hanya punya 2 field yang relevant untuk filter:
- name (String)
- description (String)

### **2. No Complex Logic**
- Tidak ada range filter (date, number)
- Tidak ada business logic (grade, status)
- Tidak perlu JOIN table lain

### **3. Pure String Match**
- Hanya LIKE query
- Case insensitive
- Partial match

### **4. Fast Performance**
- No JOIN → query cepat
- Simple WHERE clause
- Indexed by name

---

## 🚀 BEST PRACTICES

### **1. Use for Autocomplete**
```
GET /v1/subjects/filter?name=M&size=5
```
→ Tampilkan 5 mata pelajaran yang namanya dimulai dengan "M"

### **2. Browse by Category**
```
GET /v1/subjects/filter?description=science
```
→ Semua mata pelajaran science

### **3. Search All**
```
GET /v1/subjects/filter?name=Math&description=basic
```
→ Cari yang namanya "Math" DAN deskripsi "basic"

---

## 🎉 KESIMPULAN

Fitur filter Subject sudah lengkap dengan:
- ✅ 2 kriteria filter (name, description)
- ✅ Dynamic query dengan Specification
- ✅ Pagination & sorting support
- ✅ Case insensitive & partial match
- ✅ **Paling sederhana & lightweight**
- ✅ Perfect untuk search & browse
- ✅ Fast performance (no JOIN)
- ✅ Clean architecture & best practices

**Ready to use!** 🚀

---

## 🌟 HIGHLIGHT

**Subject Filter adalah yang paling sederhana karena:**
1. Hanya 2 field filter (name & description)
2. Pure string matching (LIKE query)
3. No complex logic atau relasi
4. Fast & lightweight
5. Perfect untuk search functionality

**Perfect untuk:**
- 🔍 Search bar / autocomplete
- 📚 Browse catalog
- 🏷️ Category filtering
- 📖 Simple lookup


# 📅 SCHEDULE FILTER - DOKUMENTASI LENGKAP

## ✅ FITUR FILTER SCHEDULE SUDAH SELESAI!

---

## 📂 FILE YANG DIBUAT/DIUBAH

### 1️⃣ **ScheduleFilterDTO.java** ✅ BARU
**Lokasi**: `src/main/java/com/sandy/project/dto/ScheduleFilterDTO.java`

**Field Filter:**
- `day` - Filter hari (partial match, case insensitive) - contoh: "Monday"
- `startTime` - Filter waktu mulai (partial match) - contoh: "08:00"
- `endTime` - Filter waktu selesai (partial match) - contoh: "10:00"
- `semester` - Filter semester (partial match) - contoh: "1"
- `classId` - Filter berdasarkan kelas (exact match dengan secureId)
- `subjectId` - Filter berdasarkan mata pelajaran (exact match dengan secureId)
- `teacherId` - Filter berdasarkan guru (exact match dengan secureId)

---

### 2️⃣ **ScheduleSpecification.java** ✅ BARU
**Lokasi**: `src/main/java/com/sandy/project/specification/ScheduleSpecification.java`

**Fungsi**: Build dynamic query SQL berdasarkan filter yang diisi user

**Query Logic:**
```sql
SELECT * FROM schedules s
LEFT JOIN classes c ON s.class_id = c.id
LEFT JOIN subjects sub ON s.subject_id = sub.id
LEFT JOIN teachers t ON s.teacher_id = t.id
WHERE s.deleted = false
  AND LOWER(s.day) LIKE '%monday%'              -- kalau day diisi
  AND LOWER(s.start_time) LIKE '%08:00%'        -- kalau startTime diisi
  AND LOWER(s.end_time) LIKE '%10:00%'          -- kalau endTime diisi
  AND LOWER(s.semester) LIKE '%1%'              -- kalau semester diisi
  AND c.secure_id = 'uuid-123'                  -- kalau classId diisi
  AND sub.secure_id = 'uuid-456'                -- kalau subjectId diisi
  AND t.secure_id = 'uuid-789'                  -- kalau teacherId diisi
ORDER BY s.day ASC
LIMIT 10 OFFSET 0
```

---

### 3️⃣ **ScheduleRepository.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/repository/ScheduleRepository.java`

**Perubahan**: 
- Tambah `JpaSpecificationExecutor<Schedule>`
- Sekarang bisa terima Specification untuk dynamic query

---

### 4️⃣ **ScheduleService.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/service/ScheduleService.java`

**Method Baru**:
```java
PagedResponseDTO<ScheduleResponseDTO> filterSchedules(
    ScheduleFilterDTO filter, 
    int page, 
    int size, 
    String sortBy, 
    String sortDirection
)
```

---

### 5️⃣ **ScheduleServiceImpl.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/service/impl/ScheduleServiceImpl.java`

**Implementasi**: 
- Import `ScheduleSpecification` dan `Specification`
- Implement method `filterSchedules()`
- Validasi input, build spec, query ke DB, convert ke DTO

---

### 6️⃣ **ScheduleResource.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/web/ScheduleResource.java`

**Endpoint Baru**:
```
GET /v1/schedules/filter
```

**Parameters:**
- `day` (optional) - Filter hari
- `startTime` (optional) - Filter waktu mulai
- `endTime` (optional) - Filter waktu selesai
- `semester` (optional) - Filter semester
- `classId` (optional) - Filter berdasarkan kelas (secureId)
- `subjectId` (optional) - Filter berdasarkan mata pelajaran (secureId)
- `teacherId` (optional) - Filter berdasarkan guru (secureId)
- `page` (default: 0) - Halaman
- `size` (default: 10) - Data per halaman
- `sortBy` (default: day) - Field sorting
- `sortDirection` (default: ASC) - Arah sorting

---

## 🚀 CARA PENGGUNAAN

### Contoh 1: Filter by day
```
GET /v1/schedules/filter?day=Monday
```
**Hasil**: Semua jadwal pada hari Monday

**Breakdown:**
- Input user: "Monday"
- Diconvert: "monday" (lowercase)
- Pattern: "%monday%" (wildcard)
- SQL: `LOWER(day) LIKE '%monday%'`
- Tujuan: Cari jadwal yang harinya mengandung "monday"

---

### Contoh 2: Filter by semester
```
GET /v1/schedules/filter?semester=1
```
**Hasil**: Semua jadwal semester 1

**Breakdown:**
- Input user: "1"
- Pattern: "%1%"
- SQL: `LOWER(semester) LIKE '%1%'`
- Match: "Semester 1", "1/2024", "2024-1", dll

---

### Contoh 3: Filter by classId (jadwal untuk kelas tertentu)
```
GET /v1/schedules/filter?classId=uuid-class-123
```
**Hasil**: Semua jadwal untuk kelas dengan secureId = uuid-class-123

**Breakdown:**
- Input user: "uuid-class-123"
- SQL: `class.secure_id = 'uuid-class-123'`
- Tujuan: Cari jadwal untuk kelas tertentu (EXACT match)

---

### Contoh 4: Filter by teacherId (jadwal mengajar guru)
```
GET /v1/schedules/filter?teacherId=uuid-teacher-456
```
**Hasil**: Semua jadwal mengajar untuk guru dengan secureId = uuid-teacher-456

**Breakdown:**
- Input user: "uuid-teacher-456"
- SQL: `teacher.secure_id = 'uuid-teacher-456'`
- Tujuan: Lihat jadwal mengajar guru tertentu

---

### Contoh 5: Filter by time range
```
GET /v1/schedules/filter?startTime=08:00&endTime=10:00
```
**Hasil**: Jadwal yang waktu mulainya mengandung "08:00" dan selesainya "10:00"

**Breakdown:**
- startTime: "08:00" → pattern "%08:00%"
- endTime: "10:00" → pattern "%10:00%"
- SQL: `LOWER(start_time) LIKE '%08:00%' AND LOWER(end_time) LIKE '%10:00%'`

---

### Contoh 6: Filter kombinasi (jadwal lengkap)
```
GET /v1/schedules/filter?day=Monday&semester=1&classId=uuid-123&teacherId=uuid-456&page=0&size=10&sortBy=startTime&sortDirection=ASC
```
**Hasil**: Jadwal untuk kelas tertentu, guru tertentu, hari Monday, semester 1

**Breakdown:**
- Semua kondisi digabung dengan AND
- SQL: WHERE deleted = false AND day LIKE '%monday%' AND semester LIKE '%1%' AND class.secure_id = 'uuid-123' AND teacher.secure_id = 'uuid-456'

---

### Contoh 7: Filter dengan pagination & sorting
```
GET /v1/schedules/filter?day=Monday&page=1&size=20&sortBy=startTime&sortDirection=DESC
```
**Hasil**: Halaman ke-2 (20 data), jadwal hari Monday, diurutkan berdasarkan waktu mulai (terbaru ke lama)

---

## 📊 RESPONSE FORMAT

```json
{
  "content": [
    {
      "secureId": "uuid-schedule-1",
      "day": "Monday",
      "time": "08:00 - 10:00",
      "className": "X-1",
      "subjectName": "Mathematics",
      "teacherName": "John Doe"
    },
    {
      "secureId": "uuid-schedule-2",
      "day": "Monday",
      "time": "10:00 - 12:00",
      "className": "X-1",
      "subjectName": "Physics",
      "teacherName": "Jane Smith"
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
   GET /v1/schedules/filter?day=Monday&classId=uuid-123

2. CONTROLLER (ScheduleResource)
   ↓
   - Terima parameters dari URL
   - Build ScheduleFilterDTO:
     * day = "Monday"
     * classId = "uuid-123"
     * others = null
   - Call scheduleService.filterSchedules()

3. SERVICE (ScheduleServiceImpl)
   ↓
   - Validasi input (size ≤ 50, sortBy valid, direction valid)
   - Build Pageable (page, size, sort)
   - Build Specification dari ScheduleFilterDTO
   - Query: scheduleRepository.findAll(spec, pageable)

4. SPECIFICATION (ScheduleSpecification)
   ↓
   - Cek field yang tidak null:
     * day = "Monday" → LIKE '%monday%'
     * classId = "uuid-123" → class.secure_id = 'uuid-123'
   - Gabung dengan AND
   - Return Predicate

5. REPOSITORY (ScheduleRepository)
   ↓
   - Execute query dengan JpaSpecificationExecutor
   - Join dengan tables: classes, subjects, teachers
   - Return Page<Schedule>

6. DATABASE (PostgreSQL)
   ↓
   SELECT s.*, c.class_name, sub.name, t.name
   FROM schedules s
   LEFT JOIN classes c ON s.class_id = c.id
   LEFT JOIN subjects sub ON s.subject_id = sub.id
   LEFT JOIN teachers t ON s.teacher_id = t.id
   WHERE s.deleted = false
     AND LOWER(s.day) LIKE '%monday%'
     AND c.secure_id = 'uuid-123'
   ORDER BY s.day ASC
   LIMIT 10

7. RESPONSE
   ↓
   - Convert Schedule → ScheduleResponseDTO
   - Wrap ke PagedResponseDTO
   - Return JSON ke user
```

---

## 💡 FITUR KHUSUS SCHEDULE FILTER

### 1️⃣ **Filter by Relationship (JOIN)**

Schedule punya relasi ke Class, Subject, dan Teacher. Filter bisa dilakukan berdasarkan relasi:

```java
// Filter by classId
if (filter.getClassId() != null) {
    predicates.add(
        criteriaBuilder.equal(
            root.get("clazz").get("secureId"),  // JOIN ke table classes
            filter.getClassId()
        )
    );
}
```

**SQL yang dihasilkan:**
```sql
SELECT s.* FROM schedules s
INNER JOIN classes c ON s.class_id = c.id
WHERE c.secure_id = 'uuid-123'
```

**Tujuan**: Cari jadwal untuk kelas tertentu tanpa perlu tahu ID internal

---

### 2️⃣ **Partial Match untuk String**

Semua field String (day, startTime, endTime, semester) menggunakan LIKE query:

```java
criteriaBuilder.like(
    criteriaBuilder.lower(root.get("day")),
    "%" + filter.getDay().toLowerCase() + "%"
)
```

**Keuntungan:**
- ✅ Case insensitive: "Monday" = "monday" = "MONDAY"
- ✅ Partial match: "Mon" akan match "Monday"
- ✅ Flexible search

---

### 3️⃣ **Exact Match untuk ID**

Field relasi (classId, subjectId, teacherId) menggunakan exact match:

```java
criteriaBuilder.equal(
    root.get("teacher").get("secureId"),
    filter.getTeacherId()
)
```

**Tujuan**: Cari data specific, tidak perlu partial match

---

## 🎯 USE CASES

### Use Case 1: Lihat Jadwal Kelas
```
GET /v1/schedules/filter?classId=uuid-class-x1
```
→ Tampilkan semua jadwal untuk kelas X-1

---

### Use Case 2: Lihat Jadwal Mengajar Guru
```
GET /v1/schedules/filter?teacherId=uuid-teacher-john
```
→ Tampilkan semua jadwal mengajar untuk guru John Doe

---

### Use Case 3: Cari Jadwal Mata Pelajaran
```
GET /v1/schedules/filter?subjectId=uuid-subject-math
```
��� Tampilkan semua jadwal mata pelajaran Matematika

---

### Use Case 4: Jadwal Hari Tertentu
```
GET /v1/schedules/filter?day=Monday&semester=1
```
→ Tampilkan jadwal hari Monday semester 1

---

### Use Case 5: Jadwal Pagi (berdasarkan waktu)
```
GET /v1/schedules/filter?startTime=08:00
```
→ Tampilkan jadwal yang mulai jam 08:00

---

## ✅ VALIDASI & KEAMANAN

1. **Size Limit**: Maksimal 50 data per halaman
2. **Sort Field Whitelist**: day, startTime, semester, createdAt
3. **Soft Delete**: Selalu filter `deleted = false`
4. **Case Insensitive**: String search tidak case sensitive
5. **Secure ID**: Relasi menggunakan secureId (UUID), bukan ID internal

---

## 📊 PERBANDINGAN DENGAN FILTER LAIN

| Feature | Student/Teacher | Class | Schedule |
|---------|----------------|-------|----------|
| String Filter | ��� LIKE | ✅ LIKE | ✅ LIKE |
| Date Filter | ✅ Range | ❌ | ❌ |
| Integer Filter | ✅ Range | ✅ Range | ❌ |
| **Relational Filter** | ❌ | ❌ | **✅ UNIQUE!** |

**Keunikan Schedule Filter:**
- ✅ Bisa filter berdasarkan relasi (classId, subjectId, teacherId)
- ✅ Support JOIN query dengan tabel lain
- ✅ Cocok untuk query complex relationship

---

## 🎉 KESIMPULAN

Fitur filter Schedule sudah lengkap dengan:
- ✅ 7 kriteria filter (4 string + 3 relasi)
- ✅ Dynamic query dengan Specification
- ✅ Pagination & sorting support
- ✅ Case insensitive & partial match
- ✅ Relational filtering (JOIN)
- ✅ Clean architecture & best practices
- ✅ Penjelasan breakdown di setiap bagian kode

**Ready to use!** 🚀


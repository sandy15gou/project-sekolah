### ✅ Dynamic Query
- Query dibangun otomatis berdasarkan field yang diisi
- Field yang null diabaikan
- Tidak perlu buat banyak method di repository

### ✅ Kombinasi Filter
- Semua kriteria digabung dengan AND logic
- Bisa kombinasi field sesuka hati
- Flexible & powerful

### ✅ Case Insensitive
- Semua string search tidak case sensitive
- "John" = "john" = "JOHN"

### ✅ Partial Match (LIKE)
- String field support partial match
- "John" match "Johnny", "Johnson", dll
- Pakai wildcard %...%

### ✅ Exact Match (ID)
- Field ID/relasi pakai exact match
- Untuk precision search

### ✅ Range Filter
- Support range untuk tanggal & angka
- birthDateFrom - birthDateTo
- minAge - maxAge
- minCapacity - maxCapacity

### ✅ Relational Filter (Schedule Only)
- Filter berdasarkan relasi
- classId, subjectId, teacherId
- JOIN query dengan tabel lain

### ✅ Pagination & Sorting
- Support pagination (page, size)
- Support sorting (sortBy, sortDirection)
- Max 50 data per halaman (security)

### ✅ Soft Delete Aware
- Selalu filter deleted = false
- Data yang dihapus tidak muncul

---

## 🔐 KEAMANAN

1. **Size Limit**: Max 50 data per halaman
2. **Sort Field Whitelist**: Hanya field yang diizinkan
3. **Secure ID**: Pakai UUID, bukan ID internal
4. **Input Validation**: Size, sortBy, sortDirection divalidasi
5. **SQL Injection Safe**: Pakai JPA Criteria API

---

## 📝 CONTOH PENGGUNAAN KOMBINASI

### Scenario 1: Cari siswa laki-laki di Jakarta umur 15-18
```
GET /v1/students/filter?gender=M&address=Jakarta&minAge=15&maxAge=18&page=0&size=20
```

### Scenario 2: Cari guru perempuan yang lahir tahun 1980-1990
```
GET /v1/teachers/filter?gender=F&birthDateFrom=1980-01-01&birthDateTo=1990-12-31&page=0&size=10
```

### Scenario 3: Cari kelas tingkat 10 tahun ajaran 2024 dengan kapasitas 25-35
```
GET /v1/classes/filter?gradeLevel=10&academicYear=2024&minCapacity=25&maxCapacity=35&page=0&size=10
```

### Scenario 4: Cari jadwal mengajar guru tertentu pada hari Monday semester 1
```
GET /v1/schedules/filter?teacherId=uuid-teacher-123&day=Monday&semester=1&page=0&size=10
```

---

## 🎉 SUMMARY

**Total Filter Selesai**: 6 entity (ALL MAIN ENTITIES!) 🎊
- ✅ Student (7 kriteria)
- ✅ Teacher (7 kriteria)
- ✅ Class (5 kriteria)
- ✅ Schedule (7 kriteria - 4 string + 3 relasi)
- ✅ Score (7 kriteria - 2 range + 1 string + 2 relasi + 2 business logic)
- ✅ Subject (2 kriteria - 2 string)

**Total Endpoint**: 6 endpoint baru
- `GET /v1/students/filter`
- `GET /v1/teachers/filter`
- `GET /v1/classes/filter`
- `GET /v1/schedules/filter`
- `GET /v1/scores/filter`
- `GET /v1/subjects/filter`

**Total File Dibuat**: 12 file baru
- 6 FilterDTO
- 6 Specification

**Total File Diubah**: 18 file
- 6 Repository (+ JpaSpecificationExecutor)
- 6 Service interface (+ filter method)
- 6 Service impl (+ implementation)
- 6 Resource (+ endpoint)

**Dokumentasi**: 
- ✅ 4 guide baru (CLASS, SCHEDULE, SCORE, SUBJECT)
- ✅ 1 penjelasan lengkap (cara kerja filter)

---

## ✨ HIGHLIGHT PERUBAHAN HARI INI

### Class Filter (DONE)
- ✅ ClassFilterDTO
- ✅ ClassSpecification
- ✅ Update ClassRepository
- ✅ Update ClassService & ClassServiceImpl
- ✅ Update ClassResource
- ✅ CLASS_FILTER_GUIDE.md

### Schedule Filter (DONE)
- ✅ ScheduleFilterDTO
- ✅ ScheduleSpecification
- ✅ Update ScheduleRepository
- ✅ Update ScheduleService & ScheduleServiceImpl
- ✅ Update ScheduleResource
- ✅ SCHEDULE_FILTER_GUIDE.md
- ⭐ **BONUS**: Relational filter (JOIN query)

### Score Filter (DONE)
- ✅ ScoreFilterDTO
- ✅ ScoreSpecification
- ✅ Update ScoreRepository
- ✅ Update ScoreService & ScoreServiceImpl
- ✅ Update ScoreResource
- ✅ SCORE_FILTER_GUIDE.md
- ⭐ **BONUS**: Grade mapping (A, B, C, D, E)
- ⭐ **BONUS**: Passing status filter (>= 75)
- ⭐ **BONUS**: Business logic terintegrasi

### Subject Filter (DONE - LATEST!)
- ✅ SubjectFilterDTO
- ✅ SubjectSpecification
- ✅ Update SubjectRepository
- ✅ Update SubjectService & SubjectServiceImpl
- ✅ Update SubjectResource
- ✅ SUBJECT_FILTER_GUIDE.md
- ⭐ **BONUS**: Paling sederhana (2 field only)
- ⭐ **BONUS**: Lightweight & fast (no JOIN)
- ⭐ **BONUS**: Perfect untuk search/autocomplete

---

## 🚀 READY TO USE!

Semua fitur filter sudah siap digunakan. Tidak ada compile error, hanya warning minor yang tidak masalah.

**Test dengan Postman/Browser:**
1. Start application
2. Hit endpoint filter yang diinginkan
3. Coba berbagai kombinasi filter
4. Check hasil & metadata pagination

**Dokumentasi lengkap ada di:**
- `dokumen/penjelasan_filter_lengkap.md` - Cara kerja filter
- `dokumen/CLASS_FILTER_GUIDE.md` - Guide Class filter
- `dokumen/SCHEDULE_FILTER_GUIDE.md` - Guide Schedule filter

---

Selamat! Fitur filter untuk Class dan Schedule sudah selesai! 🎉🚀
- ✅ `academicYear` - Tahun ajaran (partial, case insensitive)
- ✅ `minCapacity` - Kapasitas minimal (>=)
- ✅ `maxCapacity` - Kapasitas maksimal (<=)

**Contoh:**
```
GET /v1/classes/filter?className=X&gradeLevel=10&academicYear=2024&minCapacity=25&page=0&size=10
```

**Dokumentasi**: `dokumen/CLASS_FILTER_GUIDE.md`

---

### 4️⃣ SCHEDULE FILTER (BARU!)
**Endpoint**: `GET /v1/schedules/filter`

**Filter Criteria (7 field):**
- ✅ `day` - Hari (partial, case insensitive) - "Monday"
- ✅ `startTime` - Waktu mulai (partial) - "08:00"
- ✅ `endTime` - Waktu selesai (partial) - "10:00"
- ✅ `semester` - Semester (partial) - "1"
- ✅ `classId` - Kelas (exact, secureId)
- ✅ `subjectId` - Mata pelajaran (exact, secureId)
- ✅ `teacherId` - Guru (exact, secureId)

**Contoh:**
```
GET /v1/schedules/filter?day=Monday&semester=1&classId=uuid-123&page=0&size=10
```

**Dokumentasi**: `dokumen/SCHEDULE_FILTER_GUIDE.md`

**Keunikan**: 
- ⭐ **Support Relational Filter** (classId, subjectId, teacherId)
- ⭐ **JOIN Query** dengan tabel lain

---

### 5️⃣ SCORE FILTER (BARU!)
**Endpoint**: `GET /v1/scores/filter`

**Filter Criteria (7 field):**
- ✅ `minScore` - Nilai minimal (>=) - 0-100
- ✅ `maxScore` - Nilai maksimal (<=) - 0-100
- ✅ `semester` - Semester (partial, case insensitive)
- ✅ `studentId` - Student secureId (exact match)
- ✅ `subjectId` - Subject secureId (exact match)
- ✅ `grade` - Huruf mutu (A, B, C, D, E)
- ✅ `isPassing` - Status lulus (true/false)

**Contoh:**
```
GET /v1/scores/filter?minScore=75&studentId=uuid-123&semester=1&page=0&size=10
```

**Dokumentasi**: `dokumen/SCORE_FILTER_GUIDE.md`

**Keunikan**: 
- ⭐ **Business Logic Filter** (grade & isPassing)
- ⭐ **Grade Mapping** (A: 90-100, B: 75-89, C: 60-74, D: 50-59, E: 0-49)
- ⭐ **Passing Status** (>= 75 = lulus)
- ⭐ **Range Filter** (minScore & maxScore)
- ⭐ **Perfect untuk Reporting** (rapor, ranking, remedial)

---

### 6️⃣ SUBJECT FILTER (BARU!)
**Endpoint**: `GET /v1/subjects/filter`

**Filter Criteria (2 field):**
- ✅ `name` - Nama mata pelajaran (partial, case insensitive)
- ✅ `description` - Deskripsi (partial, case insensitive)

**Contoh:**
```
GET /v1/subjects/filter?name=Math&description=basic&page=0&size=10
```

**Dokumentasi**: `dokumen/SUBJECT_FILTER_GUIDE.md`

**Keunikan**: 
- ⭐ **Paling Sederhana** (hanya 2 field string)
- ⭐ **Lightweight** (fast query, no JOIN)
- ⭐ **Perfect untuk Search** (autocomplete, browse)
- ⭐ **Pure String Match** (LIKE query only)

---

## 🏗️ STRUKTUR IMPLEMENTASI

Semua filter mengikuti pattern yang sama:

```
1. FilterDTO        → Wadah kriteria filter
2. Specification    → Query builder (JPA Criteria API)
3. Repository       → extends JpaSpecificationExecutor
4. Service          → Business logic & validasi
5. Resource         → REST endpoint
```

**File Structure:**
```
src/main/java/com/sandy/project/
├── dto/
│   ├── StudentFilterDTO.java      ✅
│   ├── TeacherFilterDTO.java      ✅
│   ├── ClassFilterDTO.java        ✅ BARU
│   └── ScheduleFilterDTO.java     ✅ BARU
│
├── specification/
│   ├── StudentSpecification.java  ✅
│   ├── TeacherSpecification.java  ✅
│   ├── ClassSpecification.java    ✅ BARU
│   └── ScheduleSpecification.java ✅ BARU
│
├── repository/
│   ├── StudentRepository.java     ✅ (+ JpaSpecificationExecutor)
│   ├── TeacherRepository.java     ✅ (+ JpaSpecificationExecutor)
│   ├── ClassRepository.java       ✅ (+ JpaSpecificationExecutor)
│   └── ScheduleRepository.java    ✅ (+ JpaSpecificationExecutor)
│
├── service/
│   ├── StudentService.java        ✅ (+ filterStudents method)
│   ├── TeacherService.java        ✅ (+ filterTeachers method)
│   ├── ClassService.java          ✅ (+ filterClasses method)
│   └── ScheduleService.java       ✅ (+ filterSchedules method)
│
├── service/impl/
│   ├── StudentServiceImpl.java    ✅ (+ implementation)
│   ├── TeacherServiceImpl.java    ✅ (+ implementation)
│   ├── ClassServiceImpl.java      ✅ (+ implementation)
│   └── ScheduleServiceImpl.java   ✅ (+ implementation)
│
└── web/
    ├── StudentResource.java       ✅ (+ filter endpoint)
    ├── TeacherResource.java       ✅ (+ filter endpoint)
    ├── ClassResource.java         ✅ (+ filter endpoint)
    └── ScheduleResource.java      ✅ (+ filter endpoint)
```

---

## 📚 DOKUMENTASI

Semua dokumentasi tersimpan di: `dokumen/`

```
dokumen/
├── README.md                          → Project README
├── BREAKDOWN_FORMAT_GUIDE.md          → Format breakdown code
├── PAGINATION_GUIDE.md                → Guide pagination
├── penjelasan_filter_lengkap.md       → ⭐ Penjelasan filter lengkap
├── CLASS_FILTER_GUIDE.md              → ✨ BARU - Class filter guide
└── SCHEDULE_FILTER_GUIDE.md           → ✨ BARU - Schedule filter guide
```

---

## 🎯 FITUR UTAMA

# 🎯 SUMMARY: FITUR FILTER PROJECT SEKOLAH

**Status Update**: January 12, 2026  
**Last Update**: Subject Filter COMPLETED ✅  
**Status**: 🎉 **ALL 6 FILTERS COMPLETED!**

---

## ✅ COMPLETED: FITUR FILTER YANG SUDAH ADA

| No | Entity | Status | Endpoint | Dokumentasi |
|----|--------|--------|----------|-------------|
| 1️⃣ | **Student** | ✅ DONE | `GET /v1/students/filter` | ✅ |
| 2️⃣ | **Teacher** | ✅ DONE | `GET /v1/teachers/filter` | ✅ |
| 3️⃣ | **Class** | ✅ DONE | `GET /v1/classes/filter` | ✅ CLASS_FILTER_GUIDE.md |
| 4️⃣ | **Schedule** | ✅ DONE | `GET /v1/schedules/filter` | ✅ SCHEDULE_FILTER_GUIDE.md |
| 5️⃣ | **Score** | ✅ DONE | `GET /v1/scores/filter` | ✅ SCORE_FILTER_GUIDE.md |
| 6️⃣ | **Subject** | ✅ DONE | `GET /v1/subjects/filter` | ✅ SUBJECT_FILTER_GUIDE.md |

---

## ❌ BELUM ADA: FITUR FILTER YANG BELUM DIBUAT

| No | Entity | Status | Reason |
|----|--------|--------|---------|
| 7️⃣ | **AppUser** | ❌ TODO | Biasanya tidak perlu filter complex |

**Note**: Semua entity penting sudah punya filter! 🎉

---

## 📊 DETAIL FITUR FILTER

### 1️⃣ STUDENT FILTER
**Endpoint**: `GET /v1/students/filter`

**Filter Criteria (7 field):**
- ✅ `name` - Nama siswa (partial, case insensitive)
- ✅ `gender` - Jenis kelamin (M/F, exact)
- ✅ `address` - Alamat (partial, case insensitive)
- ✅ `birthDateFrom` - Tanggal lahir dari (>=)
- ✅ `birthDateTo` - Tanggal lahir sampai (<=)
- ✅ `minAge` - Umur minimal (>=)
- ✅ `maxAge` - Umur maksimal (<=)

**Contoh:**
```
GET /v1/students/filter?name=John&gender=M&minAge=15&page=0&size=10
```

---

### 2️⃣ TEACHER FILTER
**Endpoint**: `GET /v1/teachers/filter`

**Filter Criteria (7 field):**
- ✅ `name` - Nama guru (partial, case insensitive)
- ✅ `gender` - Jenis kelamin (M/F, exact)
- ✅ `address` - Alamat (partial, case insensitive)
- ✅ `birthDateFrom` - Tanggal lahir dari (>=)
- ✅ `birthDateTo` - Tanggal lahir sampai (<=)
- ✅ `minAge` - Umur minimal (>=)
- ✅ `maxAge` - Umur maksimal (<=)

**Contoh:**
```
GET /v1/teachers/filter?name=Maria&gender=F&minAge=30&page=0&size=10
```

---

### 3️⃣ CLASS FILTER (BARU!)
**Endpoint**: `GET /v1/classes/filter`

**Filter Criteria (5 field):**
- ✅ `className` - Nama kelas (partial, case insensitive)
- ✅ `gradeLevel` - Tingkat kelas (partial, case insensitive)


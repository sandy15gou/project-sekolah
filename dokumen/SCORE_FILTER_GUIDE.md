# 📝 SCORE FILTER - DOKUMENTASI LENGKAP

## ✅ FITUR FILTER SCORE SUDAH SELESAI!

---

## 📂 FILE YANG DIBUAT/DIUBAH

### 1️⃣ **ScoreFilterDTO.java** ✅ BARU
**Lokasi**: `src/main/java/com/sandy/project/dto/ScoreFilterDTO.java`

**Field Filter:**
- `minScore` (Integer) - Filter nilai minimal (>= passing grade: 75)
- `maxScore` (Integer) - Filter nilai maksimal (<= perfect score: 100)
- `semester` (String) - Filter semester (partial match)
- `studentId` (String) - Filter berdasarkan student secureId (exact match)
- `subjectId` (String) - Filter berdasarkan subject secureId (exact match)
- `grade` (String) - Filter berdasarkan huruf mutu (A, B, C, D, E)
- `isPassing` (Boolean) - Filter berdasarkan status lulus/tidak (>= 75)

---

### 2️⃣ **ScoreSpecification.java** ✅ BARU
**Lokasi**: `src/main/java/com/sandy/project/specification/ScoreSpecification.java`

**Fungsi**: Build dynamic query SQL berdasarkan filter yang diisi user

**Query Logic:**
```sql
SELECT s.* FROM scores s
LEFT JOIN students st ON s.student_id = st.id
LEFT JOIN subjects sub ON s.subject_id = sub.id
WHERE s.deleted = false
  AND s.score >= 75                         -- kalau minScore diisi
  AND s.score <= 100                        -- kalau maxScore diisi
  AND LOWER(s.semester) LIKE '%1%'          -- kalau semester diisi
  AND st.secure_id = 'uuid-123'             -- kalau studentId diisi
  AND sub.secure_id = 'uuid-456'            -- kalau subjectId diisi
  AND s.score >= 90                         -- kalau grade = "A" diisi
  AND s.score >= 75                         -- kalau isPassing = true diisi
ORDER BY s.score DESC
LIMIT 10 OFFSET 0
```

**Grade Mapping:**
- Grade A: score >= 90
- Grade B: score >= 75 AND score < 90
- Grade C: score >= 60 AND score < 75
- Grade D: score >= 50 AND score < 60
- Grade E: score < 50

---

### 3️⃣ **ScoreRepository.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/repository/ScoreRepository.java`

**Perubahan**: 
- Tambah `JpaSpecificationExecutor<Score>`
- Sekarang bisa terima Specification untuk dynamic query

---

### 4️⃣ **ScoreService.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/service/ScoreService.java`

**Method Baru**:
```java
PagedResponseDTO<ScoreResponseDTO> filterScores(
    ScoreFilterDTO filter, 
    int page, 
    int size, 
    String sortBy, 
    String sortDirection
)
```

---

### 5️⃣ **ScoreServiceImpl.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/service/impl/ScoreServiceImpl.java`

**Implementasi**: 
- Import `ScoreSpecification` dan `Specification`
- Implement method `filterScores()`
- Validasi input, build spec, query ke DB, convert ke DTO

---

### 6️⃣ **ScoreResource.java** ✅ DIUBAH
**Lokasi**: `src/main/java/com/sandy/project/web/ScoreResource.java`

**Endpoint Baru**:
```
GET /v1/scores/filter
```

**Parameters:**
- `minScore` (optional) - Nilai minimal
- `maxScore` (optional) - Nilai maksimal
- `semester` (optional) - Semester
- `studentId` (optional) - Student secureId
- `subjectId` (optional) - Subject secureId
- `grade` (optional) - Huruf mutu (A/B/C/D/E)
- `isPassing` (optional) - Status lulus (true/false)
- `page` (default: 0) - Halaman
- `size` (default: 10) - Data per halaman
- `sortBy` (default: score) - Field sorting
- `sortDirection` (default: DESC) - Arah sorting

---

## 🚀 CARA PENGGUNAAN

### Contoh 1: Filter siswa yang lulus (passing grade >= 75)
```
GET /v1/scores/filter?isPassing=true
```
**Hasil**: Semua nilai yang >= 75 (lulus)

**Breakdown:**
- Input user: isPassing = true
- Logic: score >= 75
- SQL: `WHERE score >= 75`
- Tujuan: Cari nilai yang sudah mencapai passing grade

---

### Contoh 2: Filter by score range (80-100)
```
GET /v1/scores/filter?minScore=80&maxScore=100
```
**Hasil**: Semua nilai antara 80-100

**Breakdown:**
- Input user: minScore = 80, maxScore = 100
- SQL: `WHERE score >= 80 AND score <= 100`
- Tujuan: Cari nilai di range tertentu

---

### Contoh 3: Filter by grade A (90-100)
```
GET /v1/scores/filter?grade=A
```
**Hasil**: Semua nilai dengan huruf mutu A

**Breakdown:**
- Input user: grade = "A"
- Logic: A = score >= 90
- SQL: `WHERE score >= 90`
- Tujuan: Cari nilai dengan grade A

---

### Contoh 4: Filter by grade B (75-89)
```
GET /v1/scores/filter?grade=B
```
**Hasil**: Semua nilai dengan huruf mutu B

**Breakdown:**
- Input user: grade = "B"
- Logic: B = score >= 75 AND score < 90
- SQL: `WHERE score >= 75 AND score < 90`
- Tujuan: Cari nilai dengan grade B

---

### Contoh 5: Filter by studentId (nilai satu siswa)
```
GET /v1/scores/filter?studentId=uuid-student-123
```
**Hasil**: Semua nilai untuk siswa tertentu

**Breakdown:**
- Input user: studentId = "uuid-student-123"
- SQL: `WHERE student.secure_id = 'uuid-student-123'`
- Tujuan: Lihat semua nilai siswa tertentu (EXACT match)

---

### Contoh 6: Filter by subjectId (nilai satu mata pelajaran)
```
GET /v1/scores/filter?subjectId=uuid-subject-456
```
**Hasil**: Semua nilai untuk mata pelajaran tertentu

**Breakdown:**
- Input user: subjectId = "uuid-subject-456"
- SQL: `WHERE subject.secure_id = 'uuid-subject-456'`
- Tujuan: Lihat semua nilai mata pelajaran tertentu

---

### Contoh 7: Filter by semester
```
GET /v1/scores/filter?semester=1
```
**Hasil**: Semua nilai semester 1

**Breakdown:**
- Input user: semester = "1"
- Pattern: "%1%"
- SQL: `LOWER(semester) LIKE '%1%'`
- Match: "1", "Semester 1", "2024-1", dll

---

### Contoh 8: Filter siswa yang tidak lulus
```
GET /v1/scores/filter?isPassing=false&semester=1
```
**Hasil**: Nilai siswa yang tidak lulus (< 75) di semester 1

**Breakdown:**
- Input user: isPassing = false, semester = "1"
- SQL: `WHERE score < 75 AND semester LIKE '%1%'`
- Tujuan: Cari siswa yang perlu remedial

---

### Contoh 9: Filter kombinasi complex
```
GET /v1/scores/filter?studentId=uuid-123&semester=1&minScore=75&page=0&size=10&sortBy=score&sortDirection=DESC
```
**Hasil**: Nilai siswa tertentu di semester 1 yang lulus, diurutkan dari tertinggi

**Breakdown:**
- studentId: "uuid-123" → student.secure_id = 'uuid-123'
- semester: "1" → semester LIKE '%1%'
- minScore: 75 → score >= 75
- sortBy: score → ORDER BY score
- sortDirection: DESC → dari yang tertinggi

---

### Contoh 10: Cari nilai perfect (100)
```
GET /v1/scores/filter?minScore=100&maxScore=100
```
**Hasil**: Semua nilai perfect score (100)

---

## 📊 RESPONSE FORMAT

```json
{
  "content": [
    {
      "secureId": "uuid-score-1",
      "studentId": "uuid-student-123",
      "studentName": "John Doe",
      "subjectId": "uuid-subject-456",
      "subjectName": "Mathematics",
      "score": 95,
      "semester": "1",
      "grade": "A",
      "isPassing": true
    },
    {
      "secureId": "uuid-score-2",
      "studentId": "uuid-student-123",
      "studentName": "John Doe",
      "subjectId": "uuid-subject-789",
      "subjectName": "Physics",
      "score": 80,
      "semester": "1",
      "grade": "B",
      "isPassing": true
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
   GET /v1/scores/filter?minScore=75&studentId=uuid-123

2. CONTROLLER (ScoreResource)
   ↓
   - Terima parameters dari URL
   - Build ScoreFilterDTO:
     * minScore = 75
     * studentId = "uuid-123"
     * others = null
   - Call scoreService.filterScores()

3. SERVICE (ScoreServiceImpl)
   ↓
   - Validasi input (size ≤ 50, sortBy valid, direction valid)
   - Build Pageable (page, size, sort)
   - Build Specification dari ScoreFilterDTO
   - Query: scoreRepository.findAll(spec, pageable)

4. SPECIFICATION (ScoreSpecification)
   ↓
   - Cek field yang tidak null:
     * minScore = 75 → score >= 75
     * studentId = "uuid-123" → student.secure_id = 'uuid-123'
   - Gabung dengan AND
   - Return Predicate

5. REPOSITORY (ScoreRepository)
   ↓
   - Execute query dengan JpaSpecificationExecutor
   - JOIN dengan tables: students, subjects
   - Return Page<Score>

6. DATABASE (PostgreSQL)
   ↓
   SELECT s.*, st.name, sub.name
   FROM scores s
   INNER JOIN students st ON s.student_id = st.id
   INNER JOIN subjects sub ON s.subject_id = sub.id
   WHERE s.deleted = false
     AND s.score >= 75
     AND st.secure_id = 'uuid-123'
   ORDER BY s.score DESC
   LIMIT 10

7. RESPONSE
   ↓
   - Convert Score → ScoreResponseDTO
   - Include: score, grade, isPassing (business logic)
   - Wrap ke PagedResponseDTO
   - Return JSON ke user
```

---

## 💡 FITUR KHUSUS SCORE FILTER

### 1️⃣ **Filter by Grade (Huruf Mutu)**

```java
if (filter.getGrade() != null) {
    String grade = filter.getGrade().toUpperCase();
    switch (grade) {
        case "A": predicates.add(cb.greaterThanOrEqualTo(root.get("score"), 90)); break;
        case "B": 
            predicates.add(cb.greaterThanOrEqualTo(root.get("score"), 75));
            predicates.add(cb.lessThan(root.get("score"), 90));
            break;
        // dst...
    }
}
```

**Mapping Grade:**
| Grade | Range | SQL |
|-------|-------|-----|
| A | 90-100 | score >= 90 |
| B | 75-89 | score >= 75 AND score < 90 |
| C | 60-74 | score >= 60 AND score < 75 |
| D | 50-59 | score >= 50 AND score < 60 |
| E | 0-49 | score < 50 |

---

### 2️⃣ **Filter by Passing Status**

```java
if (filter.getIsPassing() != null) {
    if (filter.getIsPassing()) {
        // Lulus: >= 75
        predicates.add(cb.greaterThanOrEqualTo(root.get("score"), 75));
    } else {
        // Tidak lulus: < 75
        predicates.add(cb.lessThan(root.get("score"), 75));
    }
}
```

**Use Case:**
- `isPassing=true` → Cari siswa yang lulus (untuk promosi kelas)
- `isPassing=false` → Cari siswa yang butuh remedial

---

### 3️⃣ **Filter by Score Range**

```java
// Minimum score
if (filter.getMinScore() != null) {
    predicates.add(cb.greaterThanOrEqualTo(root.get("score"), filter.getMinScore()));
}

// Maximum score
if (filter.getMaxScore() != null) {
    predicates.add(cb.lessThanOrEqualTo(root.get("score"), filter.getMaxScore()));
}
```

**Use Case:**
- `minScore=90&maxScore=100` → Siswa berprestasi tinggi
- `minScore=0&maxScore=50` → Siswa yang perlu perhatian khusus

---

## 🎯 USE CASES

### Use Case 1: Lihat Rapor Siswa (Semua Nilai)
```
GET /v1/scores/filter?studentId=uuid-student-123&semester=1
```
→ Tampilkan semua nilai siswa di semester 1

---

### Use Case 2: Cari Siswa Berprestasi (Grade A)
```
GET /v1/scores/filter?grade=A&semester=1
```
→ Tampilkan semua siswa yang mendapat nilai A

---

### Use Case 3: Remedial List (Tidak Lulus)
```
GET /v1/scores/filter?isPassing=false&subjectId=uuid-subject-math
```
→ Tampilkan siswa yang perlu remedial Matematika

---

### Use Case 4: Top Students (90-100)
```
GET /v1/scores/filter?minScore=90&sortBy=score&sortDirection=DESC
```
→ Ranking siswa dengan nilai tertinggi

---

### Use Case 5: Subject Performance
```
GET /v1/scores/filter?subjectId=uuid-subject-456&minScore=75
```
→ Lihat performa mata pelajaran tertentu (yang lulus)

---

## ✅ VALIDASI & KEAMANAN

1. **Size Limit**: Max 50 data per halaman
2. **Sort Field Whitelist**: score, semester, createdAt
3. **Soft Delete**: Selalu filter `deleted = false`
4. **Score Range**: Nilai 0-100 (validated di entity)
5. **Secure ID**: Pakai UUID, bukan ID internal
6. **Grade Validation**: Hanya A, B, C, D, E yang valid

---

## 📊 PERBANDINGAN DENGAN FILTER LAIN

| Feature | Student/Teacher | Class | Schedule | **Score** |
|---------|----------------|-------|----------|-----------|
| String Filter | ✅ LIKE | ✅ LIKE | ✅ LIKE | ✅ LIKE |
| Range Filter | ✅ Date/Age | ✅ Capacity | ❌ | **✅ Score** |
| Relational Filter | ❌ | ❌ | ✅ 3 relasi | **✅ 2 relasi** |
| **Grade Filter** | ❌ | ❌ | ❌ | **✅ UNIQUE!** |
| **Passing Status** | ❌ | ❌ | ❌ | **✅ UNIQUE!** |

**Keunikan Score Filter:**
- ✅ Filter berdasarkan grade (A, B, C, D, E)
- ✅ Filter berdasarkan passing status (lulus/tidak)
- ✅ Range filter untuk nilai (min/max)
- ✅ Relational filter (studentId, subjectId)
- ✅ Business logic terintegrasi (grade & isPassing)

---

## 🎉 KESIMPULAN

Fitur filter Score sudah lengkap dengan:
- ✅ 7 kriteria filter (2 range + 1 string + 2 relasi + 2 business logic)
- ✅ Dynamic query dengan Specification
- ✅ Pagination & sorting support
- ✅ Grade mapping (A, B, C, D, E)
- ✅ Passing status check (>= 75)
- ✅ Relational filtering (JOIN dengan students & subjects)
- ✅ Clean architecture & best practices
- ✅ Penjelasan breakdown di setiap bagian kode

**Ready to use!** 🚀

---

## 🌟 HIGHLIGHT

**Filter Score adalah yang paling powerful karena:**
1. Support **business logic** (grade & passing status)
2. Support **range filter** (min/max score)
3. Support **relational filter** (student & subject)
4. Cocok untuk **reporting** (rapor, ranking, remedial list)
5. Cocok untuk **analytics** (subject performance, class performance)

**Perfect untuk fitur:**
- 📊 Dashboard nilai siswa
- 🏆 Ranking & leaderboard
- 📝 Remedial management
- 📈 Performance analytics
- 🎓 Graduation requirements check


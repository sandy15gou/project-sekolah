-- Cleanup SQL untuk Test
-- Hapus data test setelah test selesai

-- Hapus data dari tabel dengan foreign key terlebih dahulu
DELETE FROM scores WHERE id > 0;
DELETE FROM schedules WHERE id > 0;
DELETE FROM class_students WHERE class_id > 0;
DELETE FROM subject_eligible_teachers WHERE subject_id > 0;

-- Hapus data dari tabel utama
DELETE FROM students WHERE id > 0;
DELETE FROM classes WHERE id > 0;
DELETE FROM subjects WHERE id > 0;
DELETE FROM teachers WHERE id > 0;

-- Reset sequence jika diperlukan (optional)
-- ALTER SEQUENCE student_seq RESTART WITH 1;
-- ALTER SEQUENCE class_seq RESTART WITH 1;
-- ALTER SEQUENCE teacher_seq RESTART WITH 1;
-- ALTER SEQUENCE score_seq RESTART WITH 1;

-- Seed data for ClassRepositoryTest
-- Schema already created by Flyway migration V1__create_schema.sql
-- This file only inserts test data

-- Teachers (must insert first due to FK in classes)
INSERT INTO teachers (id, deleted, secure_id, address, birth_date, gender, name) VALUES
(4, false, '3684e8bf-4b2d-4636-bb78-267c8586635e', 'Bandung', '2022-04-18', 'F', 'Jane Smith');

-- Classes (depends on teachers)
INSERT INTO classes (id, academic_year, class_name, grade_level, secure_id, homeroom_teacher_id, deleted, description, max_capacity) VALUES
(1, '2024/2025', 'X IPA 1', '10', '215ce3d2-510c-43d6-917b-029581287111', 4, false, NULL, 30),
(2, '2024/2025', 'X IPA 2', '10', '097d6730-d453-41b1-a956-147324c45f5f', 4, false, NULL, 30);

-- Students (optional for class repository test)
INSERT INTO students (id, deleted, secure_id, birth_date, name, address, gender) VALUES
(2, false, '6aa38d68-443b-4e6a-924a-ca3947e197d8', '2005-05-10', 'John Doe', 'Jakarta', 'M');

-- Subjects (optional)
INSERT INTO subjects (id, deleted, secure_id, description, name) VALUES
(1, false, '0ae371f0-b0e3-458e-8a76-cef8f98d52e9', 'Mata pelajaran Matematika untuk tingkat SMA', 'Matematika');

-- Users and Roles (optional)
INSERT INTO role (id, name) VALUES (1, 'USER'), (2, 'ADMIN');

INSERT INTO app_user (id, deleted, secure_id, password, username) VALUES
(2, false, 'test-secure-id-user-001', '$2a$10$oS/nz1waKLtXBORaBARftORfbjolMmt/lI951oCrxnqAUH7XlTghK', 'testuser');

INSERT INTO user_role (user_id, role_id) VALUES (2, 1);


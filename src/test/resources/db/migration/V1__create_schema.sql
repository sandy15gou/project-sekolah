-- Flyway Migration V1: Create initial schema
-- File: V1__create_schema.sql
-- This runs ONCE per test container to create tables

-- Create sequences
CREATE SEQUENCE IF NOT EXISTS class_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS score_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS student_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS teacher_seq START WITH 1 INCREMENT BY 50;

-- Create app_user table
CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    secure_id VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE
);

CREATE INDEX idx_app_user_username ON app_user(username);

-- Create role table
CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Create teachers table
CREATE TABLE teachers (
    id BIGINT PRIMARY KEY,
    deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    secure_id VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(255),
    birth_date DATE,
    gender VARCHAR(1) NOT NULL CHECK (gender IN ('M', 'F')),
    name VARCHAR(255) NOT NULL
);

-- Create classes table
CREATE TABLE classes (
    id BIGINT PRIMARY KEY,
    deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    secure_id VARCHAR(255) NOT NULL UNIQUE,
    academic_year VARCHAR(255) NOT NULL,
    class_name VARCHAR(255) NOT NULL,
    grade_level VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    max_capacity INTEGER DEFAULT 30,
    homeroom_teacher_id BIGINT,
    CONSTRAINT fk_classes_teacher FOREIGN KEY (homeroom_teacher_id) REFERENCES teachers(id)
);

-- Create students table
CREATE TABLE students (
    id BIGINT PRIMARY KEY,
    deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    secure_id VARCHAR(255) NOT NULL UNIQUE,
    birth_date DATE,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    gender VARCHAR(1) NOT NULL CHECK (gender IN ('M', 'F', 'U')),
    class_id BIGINT,
    CONSTRAINT fk_students_class FOREIGN KEY (class_id) REFERENCES classes(id)
);

-- Create subjects table
CREATE TABLE subjects (
    id BIGSERIAL PRIMARY KEY,
    deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    secure_id VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    name VARCHAR(255) NOT NULL
);

-- Create schedules table
CREATE TABLE schedules (
    id BIGSERIAL PRIMARY KEY,
    deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    secure_id VARCHAR(255) NOT NULL UNIQUE,
    schedule_day VARCHAR(255) NOT NULL,
    start_time VARCHAR(255) NOT NULL,
    end_time VARCHAR(255) NOT NULL,
    semester VARCHAR(255) NOT NULL,
    class_id BIGINT,
    subject_id BIGINT,
    teacher_id BIGINT,
    CONSTRAINT fk_schedules_class FOREIGN KEY (class_id) REFERENCES classes(id),
    CONSTRAINT fk_schedules_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
    CONSTRAINT fk_schedules_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);

-- Create scores table
CREATE TABLE scores (
    id BIGINT PRIMARY KEY,
    deleted BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    secure_id VARCHAR(255) NOT NULL UNIQUE,
    score INTEGER NOT NULL,
    semester VARCHAR(1) NOT NULL CHECK (semester IN ('1', '2')),
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    CONSTRAINT fk_scores_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_scores_subject FOREIGN KEY (subject_id) REFERENCES subjects(id)
);

-- Create junction tables
CREATE TABLE class_students (
    class_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    PRIMARY KEY (class_id, student_id),
    CONSTRAINT fk_class_students_class FOREIGN KEY (class_id) REFERENCES classes(id),
    CONSTRAINT fk_class_students_student FOREIGN KEY (student_id) REFERENCES students(id)
);

CREATE TABLE subject_eligible_teachers (
    subject_id BIGINT NOT NULL,
    teacher_id BIGINT NOT NULL,
    PRIMARY KEY (subject_id, teacher_id),
    CONSTRAINT fk_subject_teachers_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
    CONSTRAINT fk_subject_teachers_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);

CREATE TABLE user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role(id)
);


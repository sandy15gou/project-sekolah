package com.sandy.project.Integration.testdata;

import com.sandy.project.domain.*;
import com.sandy.project.domain.Class;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Object Mother Pattern untuk Test Data
 *
 * Pattern ini membuat pembuatan test data lebih:
 * - Reusable: Satu method bisa dipakai di banyak test
 * - Maintainable: Kalau struktur entity berubah, ubah di satu tempat
 * - Readable: Test code jadi lebih clean
 *
 * Contoh:
 * <pre>
 * Teacher teacher = ObjectMother.createTeacher("Jane Smith");
 * Class class = ObjectMother.createClass("X IPA 1", teacher);
 * </pre>
 *
 * @author Sandy
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectMother {

    // ==================== TEACHERS ====================
    
    public static Teacher createTeacher(String name) {
        Teacher teacher = new Teacher();
        teacher.setSecureId(UUID.randomUUID().toString());
        teacher.setName(name);
        teacher.setGender("F");
        teacher.setAddress("Jakarta");
        teacher.setBirthDate(LocalDate.of(1980, 1, 1));
        teacher.setDeleted(false);
        return teacher;
    }

    public static Teacher createJaneSmith() {
        Teacher teacher = new Teacher();
        teacher.setId(4L);
        teacher.setSecureId("3684e8bf-4b2d-4636-bb78-267c8586635e");
        teacher.setName("Jane Smith");
        teacher.setGender("F");
        teacher.setAddress("Bandung");
        teacher.setBirthDate(LocalDate.of(2022, 4, 18));
        teacher.setDeleted(false);
        return teacher;
    }

    // ==================== CLASSES ====================
    
    public static Class createClass(String className, Teacher homeroomTeacher) {
        Class clazz = new Class();
        clazz.setSecureId(UUID.randomUUID().toString());
        clazz.setClassName(className);
        clazz.setGradeLevel("10");
        clazz.setAcademicYear("2024/2025");
        clazz.setMaxCapacity(30);
        clazz.setHomeroomTeacher(homeroomTeacher);
        clazz.setDeleted(false);
        return clazz;
    }

    public static Class createClassXIPA1WithJaneSmith() {
        Teacher teacher = createJaneSmith();
        
        Class clazz = new Class();
        clazz.setId(1L);
        clazz.setSecureId("215ce3d2-510c-43d6-917b-029581287111");
        clazz.setClassName("X IPA 1");
        clazz.setGradeLevel("10");
        clazz.setAcademicYear("2024/2025");
        clazz.setMaxCapacity(30);
        clazz.setHomeroomTeacher(teacher);
        clazz.setDeleted(false);
        return clazz;
    }

    // ==================== STUDENTS ====================
    
    public static Student createStudent(String name) {
        Student student = new Student();
        student.setSecureId(UUID.randomUUID().toString());
        student.setName(name);
        student.setGender("M");
        student.setBirthDate(LocalDate.of(2005, 1, 1));
        student.setAddress("Jakarta");
        student.setDeleted(false);
        return student;
    }

    public static Student createJohnDoe() {
        Student student = new Student();
        student.setId(2L);
        student.setSecureId("6aa38d68-443b-4e6a-924a-ca3947e197d8");
        student.setName("John Doe");
        student.setGender("M");
        student.setBirthDate(LocalDate.of(2005, 5, 10));
        student.setDeleted(false);
        return student;
    }

    // ==================== SUBJECTS ====================
    
    public static Subject createSubject(String name) {
        Subject subject = new Subject();
        subject.setSecureId(UUID.randomUUID().toString());
        subject.setName(name);
        subject.setDescription("Mata pelajaran " + name);
        subject.setDeleted(false);
        return subject;
    }

    public static Subject createMathSubject() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setSecureId("0ae371f0-b0e3-458e-8a76-cef8f98d52e9");
        subject.setName("Matematika");
        subject.setDescription("Mata pelajaran Matematika untuk tingkat SMA");
        subject.setDeleted(false);
        return subject;
    }

    // ==================== USERS ====================
    
    public static AppUser createUser(String username, String password) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setDeleted(false);
        return user;
    }

    // ==================== ROLES ====================
    
    public static Role createRole(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }
}


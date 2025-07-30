package com.sandy.project.repository;
import java.util.List;
import java.util.Optional;

import com.sandy.project.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentRepository extends JpaRepository<Student, Long> {
    //method name convention
    //find+keyword
    //sql -> select * from Student s where s.id= :studentId
    public Optional<Student> findById(Long id);
    
    public List<Student> findBySecureIdIn(List<String> studentIdList);
    
    public Optional<Student> findBySecureId(String id);
    
    //where id = :id AND deleted=false
    public Optional<Student> findByIdAndDeletedFalse(Long id);
    
    
    //sql -> select s from Student s where s.student_name = :studentName
    public List<Student> findByNameLike(String studentName);
}


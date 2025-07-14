package com.sandy.project.service;

import com.sandy.project.dto.StudentCreateDTO;
import com.sandy.project.dto.StudentDetailDTO;
import com.sandy.project.dto.StudentResponseDTO;
import com.sandy.project.dto.StudentUpdateDTO;

import java.util.List;

public interface StudentService {
    
    
    public StudentResponseDTO findStudentById(String id);
    
    public void createNewStudent(List<StudentCreateDTO> dto);
    
    public void updateStudent(String studentId, StudentUpdateDTO dto);
    
    public void deleteStudent(String studentId);
    
    StudentDetailDTO findStudentDetail(String id);
    
    //	public List<Author> findAuthors(List<String> authorIdList);
    //
    //	public List<AuthorResponseDTO> constructDTO(List<Author> authors);
    //
    //	public Map<Long, List<String>> findAuthorMaps(List<Long> authorIdList);
    
    //jangang Lupa di bikin
    

}

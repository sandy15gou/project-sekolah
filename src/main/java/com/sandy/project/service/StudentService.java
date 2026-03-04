package com.sandy.project.service;

import com.sandy.project.dto.PagedResponseDTO;
import com.sandy.project.dto.StudentCreateDTO;
import com.sandy.project.dto.StudentDetailDTO;
import com.sandy.project.dto.StudentFilterDTO;
import com.sandy.project.dto.StudentResponseDTO;
import com.sandy.project.dto.StudentUpdateDTO;

import java.util.List;

public interface StudentService {
    
    
    public StudentResponseDTO findStudentById(String id);
    
    public void createNewStudent(List<StudentCreateDTO> dto);
    
    public void updateStudent(String studentId, StudentUpdateDTO dto);
    
    public void deleteStudent(String studentId);
    
    StudentDetailDTO findStudentDetail(String id);
    
    /**
     * Find all students menggunakan JPA Projection - SOLUSI N+1 Problem
     * Mengambil Student + Class info dalam 1 query JOIN
     * @return List of StudentDetailDTO
     */
    List<StudentDetailDTO> findAllStudents();
    
    // ========== PAGINATION METHODS ========= =
    
    /**
     * Find all students dengan pagination dan sorting
     * @param page Page number (0-based)
     * @param size Items per page (max 50)
     * @param sortBy Field untuk sorting (hanya: name, createdAt)
     * @param sortDirection Arah sorting (ASC atau DESC)
     * @return PagedResponseDTO berisi list students dan metadata pagination
     */
    PagedResponseDTO<StudentResponseDTO> findAllStudentsPaged(int page, int size, String sortBy, String sortDirection);
    
    /**
     * Search students by name dengan pagination
     * @param name Nama yang dicari (partial match)
     * @param page Page number (0-based)
     * @param size Items per page (max 50)
     * @param sortBy Field untuk sorting (hanya: name, createdAt)
     * @param sortDirection Arah sorting (ASC atau DESC)
     * @return PagedResponseDTO berisi hasil pencarian
     */
    PagedResponseDTO<StudentResponseDTO> searchStudentsByNamePaged(String name, int page, int size, String sortBy, String sortDirection);
    
    /**
     * Filter students dengan multiple criteria (dynamic query)
     * Semua criteria di filter akan di-combine dengan AND logic
     *
     * @param filter StudentFilterDTO berisi kriteria filter (nama, gender, alamat, tanggal lahir, umur)
     * @param page Page number (0-based)
     * @param size Items per page (max 50)
     * @param sortBy Field untuk sorting (hanya: name, createdAt, birthDate)
     * @param sortDirection Arah sorting (ASC atau DESC)
     * @return PagedResponseDTO berisi hasil filter
     */
    PagedResponseDTO<StudentResponseDTO> filterStudents(StudentFilterDTO filter, int page, int size, String sortBy, String sortDirection);
    
    //	public List<Author> findAuthors(List<String> authorIdList);
    //
    //	public List<AuthorResponseDTO> constructDTO(List<Author> authors);
    //
    //	public Map<Long, List<String>> findAuthorMaps(List<Long> authorIdList);
    
    //jangang Lupa di bikin
    

}

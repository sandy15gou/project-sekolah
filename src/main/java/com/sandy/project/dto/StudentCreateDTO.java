package com.sandy.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sandy.project.validator.annotation.ValidAuthorName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StudentCreateDTO implements Serializable {


    private static final long serialVersionUID = -279143110961737209L;
    
    @NotBlank(message = "Nama siswa tidak boleh kosong")
    private String studentName;
    
    @ValidAuthorName
    @NotBlank(message = "NIS tidak boleh kosong")
    private String studentId; // Student Id
    
    @NotNull(message = "Tanggal lahir tidak boleh kosong")
    private Long studentBirthDate;
    
    @NotBlank(message = "Jenis kelamin tidak boleh kosong")
    private String studentGender;
    
    @NotBlank(message = "Alamat tidak boleh kosong")
    private String studentAddress;
    
    private String teacherName;
}

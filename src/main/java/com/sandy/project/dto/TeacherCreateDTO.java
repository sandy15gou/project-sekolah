package com.sandy.project.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sandy.project.validator.annotation.ValidAuthorName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class TeacherCreateDTO implements Serializable {
    
    private static final long serialVersionUID = -8000296245880876264L;
    @NotBlank(message = "Nama Guru tidak boleh kosong")
    private String teacherName;
    
    @ValidAuthorName
    @NotBlank(message = "NIP tidak boleh kosong")
    private String teacherId;
    
    @NotNull(message = "Tanggal lahir tidak boleh kosong")
    private Long teacherBirthDate;
    
    @NotBlank(message = "Jenis kelamin tidak boleh kosong")
    private String teacherGender;
    
    @NotBlank(message = "Alamat tidak boleh kosong")
    private String teacherAddress;
    
}

package com.sandy.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class TeacherUpdateDTO implements Serializable {
    
    private static final long serialVersionUID = 6672740006152861881L;
    
    @NotBlank(message = "Nama siswa tidak boleh kosong")
    private String teacherName;
    
    @NotBlank(message = "NIP tidak boleh kosong")
    private String teacherId;
    
    @NotNull(message = "Tanggal lahir tidak boleh kosong")
    private Long teacherBirthDate;
    
    @NotBlank(message = "Jenis kelamin tidak boleh kosong")
    private String teacherGender;
    
    @NotBlank(message = "Alamat tidak boleh kosong")
    private String teacherAddress;
}

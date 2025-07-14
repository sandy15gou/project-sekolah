package com.sandy.project.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class TeacherUpdateDTO implements Serializable {
    
    private static final long serialVersionUID = 6672740006152861881L;
    
    @NotBlank(message = "Nama siswa tidak boleh kosong")
    private String teacherName;
    
    @NotBlank(message = "NIP tidak boleh kosong")
    private String teacherId;
    
    @NotBlank(message = "Tanggal lahir tidak boleh kosong")
    private String teacherBirthDate;
    
    @NotBlank(message = "Jenis kelamin tidak boleh kosong")
    private String teacherGender;
    
    @NotBlank(message = "Alamat tidak boleh kosong")
    private String teacherAddress;
}

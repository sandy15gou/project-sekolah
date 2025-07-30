package com.sandy.project.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class SubjectDetailDTO implements Serializable {
    private static final long serialVersionUID = 5696733373528887588L;
    private String name;
    private String description;
    private List<String> eligibleTeacherIds; // secureId guru
}

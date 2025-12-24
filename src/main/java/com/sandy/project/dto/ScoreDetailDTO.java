package com.sandy.project.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ScoreDetailDTO implements Serializable {
    
    private static final long serialVersionUID = 5429783300660961393L;
    
    private String scoreId;
    private String studentId;
    private String studentName;
    private String subjectId;
    private String subjectName;
    private String score;
    private String semester;
}

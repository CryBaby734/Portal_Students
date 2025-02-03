package org.example.studentportal.dto;

import lombok.Data;


@Data
public class LessonRequest {
    private Long groupId;
    private String subject;
    private String teacher;
    private String schedule;
}

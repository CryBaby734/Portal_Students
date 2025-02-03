package org.example.studentportal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentGroupRequest {
    private String studentEmail;
    private Long groupId;
}

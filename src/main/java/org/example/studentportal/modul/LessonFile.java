package org.example.studentportal.modul;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "lesson_files")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileUrl;

    @ManyToOne
    @JoinColumn(name= "lesson_id")
    private Lesson lesson;
}

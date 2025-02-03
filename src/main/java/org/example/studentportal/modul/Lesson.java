package org.example.studentportal.modul;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String teacher;

    @Column(nullable = false)
    private String schedule;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @JsonIgnore
    private StudentGroup group; // Урок привязан к группе

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<LessonFile> files; // Файлы, прикрепленные к уроку
}

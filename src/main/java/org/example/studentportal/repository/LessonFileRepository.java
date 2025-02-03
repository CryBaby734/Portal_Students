package org.example.studentportal.repository;


import org.example.studentportal.modul.Lesson;
import org.example.studentportal.modul.LessonFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonFileRepository extends JpaRepository<LessonFile, Long> {
    List<LessonFile> findByLesson(Lesson lesson);
}

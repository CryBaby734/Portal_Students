package org.example.studentportal.service;

import lombok.RequiredArgsConstructor;
import org.example.studentportal.modul.Lesson;
import org.example.studentportal.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;

    public List<Lesson> getStudentLessons(Long studentId) {
        return lessonRepository.findByStudentId(studentId);
    }

    public Lesson createLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public void deleteLesson(Long lessonId) {
        lessonRepository.deleteById(lessonId);
    }
}

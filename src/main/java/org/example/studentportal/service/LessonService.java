package org.example.studentportal.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.studentportal.modul.Lesson;
import org.example.studentportal.modul.LessonFile;
import org.example.studentportal.modul.Student;
import org.example.studentportal.repository.LessonFileRepository;
import org.example.studentportal.repository.LessonRepository;
import org.example.studentportal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final LessonFileRepository lessonFileRepository;
    private final StudentRepository studentRepository;

    @Value("${upload.path}")
    private String uploadPath;



    // ✅ Метод для загрузки файла (Админ загружает)
    @SneakyThrows
    public String uploadFile(Long lessonId, MultipartFile file) throws IOException {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Урок не найден"));

        // Создаем папку, если её нет
        Files.createDirectories(Paths.get(uploadPath));

        // Добавляем таймштамп, чтобы избежать дубликатов
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = uploadPath + File.separator + fileName;

        // Сохраняем файл на сервере
        file.transferTo(new File(filePath));

        // Создаем запись в БД
        LessonFile lessonFile = new LessonFile();
        lessonFile.setLesson(lesson);
        lessonFile.setFileName(fileName);
        lessonFile.setFileUrl(filePath);
        lessonFileRepository.save(lessonFile);

        return filePath;
    }

    public List<String> getLessonFiles(Long lessonId){
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lessonfile is not found"));

        List<LessonFile> files = lessonFileRepository.findByLesson(lesson);
        return files.stream()
                .map(LessonFile::getFileUrl)
                .collect(Collectors.toList());
    }

    public List<Lesson> getStudentLessons(Long studentId){
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student is not found"));
        if(student.getGroup() == null){
            throw new RuntimeException("Student group is not found");
        }
        return student.getGroup().getLessons();
    }



}

package org.example.studentportal.controllers;

import lombok.RequiredArgsConstructor;
import org.example.studentportal.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    // ✅ Загрузка файла (только админ)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload-file/{lessonId}")
    public ResponseEntity<String> uploadFile(@PathVariable Long lessonId, @RequestParam("file") MultipartFile file) throws IOException {
        String lesson = lessonService.uploadFile(lessonId, file);
        return ResponseEntity.ok(lesson);
    }

    // ✅ Получение списка загруженных файлов (доступно всем студентам)
    @GetMapping("/{lessonId}/files")
    public ResponseEntity<List<String>> getLessonFiles(@PathVariable Long lessonId) {
        List<String> files = lessonService.getLessonFiles(lessonId);
        return ResponseEntity.ok(files);
    }
}

package org.example.studentportal.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.studentportal.modul.Lesson;
import org.example.studentportal.modul.Message;
import org.example.studentportal.modul.Payment;
import org.example.studentportal.modul.Student;

import org.example.studentportal.repository.LessonRepository;
import org.example.studentportal.repository.StudentRepository;
import org.example.studentportal.service.LessonService;
import org.example.studentportal.service.MessageService;

import org.example.studentportal.service.PaymentService;
import org.example.studentportal.service.StudentService;
import org.example.studentportal.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
@Tag(name = "Student Controller", description = "API для студентов")
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final LessonService lessonService;
    private final PaymentService paymentService;
    private final MessageService messageService;
    private final SecurityUtil securityUtil; // Добавляем SecurityUtil через Dependency Injection
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;

    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<Student> getStudentProfile() {
        Long studentId = securityUtil.getCurrentUserId(); // Используем инъекцию
        Student student = studentService.getStudentProfile(studentId);
        return ResponseEntity.ok(student);
    }
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @PutMapping("/profile")
    public ResponseEntity<Student> updateStudentProfile(@RequestBody Student updatedStudent) {
        Long studentId = securityUtil.getCurrentUserId();
        Student student = studentService.updateStudentProfile(studentId, updatedStudent);
        return ResponseEntity.ok(student);
    }
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @GetMapping("/lessons")
    public ResponseEntity<List<Lesson>> getLessons() {
        Long studentId = securityUtil.getCurrentUserId(); // Получаем ID студента через SecurityUtil
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Ошибка: студент не найден!"));

        if (student.getGroup() == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList()); // Если у студента нет группы, возвращаем пустой список
        }

        List<Lesson> lessons = lessonRepository.findByGroupId(student.getGroup().getId());
        return ResponseEntity.ok(lessons);
    }
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getStudentPayments() {
        Long studentId = securityUtil.getCurrentUserId();
        List<Payment> payments = paymentService.getStudentPayments(studentId);
        return ResponseEntity.ok(payments);
    }
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @PostMapping("/messages")
    public ResponseEntity<Message> sendMessageToAdmin(@RequestBody String content) {
        Long studentId = securityUtil.getCurrentUserId();
        Message message = messageService.sendMessageToAdmin(studentId, content);
        return ResponseEntity.ok(message);
    }
}

package org.example.studentportal.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.studentportal.modul.Lesson;
import org.example.studentportal.modul.Message;
import org.example.studentportal.modul.Payment;
import org.example.studentportal.modul.Student;

import org.example.studentportal.service.LessonService;
import org.example.studentportal.service.MessageService;

import org.example.studentportal.service.PaymentService;
import org.example.studentportal.service.StudentService;
import org.example.studentportal.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/profile")
    public ResponseEntity<Student> getStudentProfile() {
        Long studentId = securityUtil.getCurrentUserId(); // Используем инъекцию
        Student student = studentService.getStudentProfile(studentId);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/profile")
    public ResponseEntity<Student> updateStudentProfile(@RequestBody Student updatedStudent) {
        Long studentId = securityUtil.getCurrentUserId();
        Student student = studentService.updateStudentProfile(studentId, updatedStudent);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/lessons")
    public ResponseEntity<List<Lesson>> getStudentLessons() {
        Long studentId = securityUtil.getCurrentUserId();
        List<Lesson> lessons = lessonService.getStudentLessons(studentId);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getStudentPayments() {
        Long studentId = securityUtil.getCurrentUserId();
        List<Payment> payments = paymentService.getStudentPayments(studentId);
        return ResponseEntity.ok(payments);
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> sendMessageToAdmin(@RequestBody String content) {
        Long studentId = securityUtil.getCurrentUserId();
        Message message = messageService.sendMessageToAdmin(studentId, content);
        return ResponseEntity.ok(message);
    }
}
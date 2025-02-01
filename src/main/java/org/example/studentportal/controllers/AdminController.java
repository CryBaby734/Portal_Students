package org.example.studentportal.controllers;
import org.example.studentportal.modul.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.studentportal.modul.Student;

import org.example.studentportal.service.AdminService;
import org.example.studentportal.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Admin Controller",  description = "API для Админов")
@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final NewsService newsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = adminService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        adminService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateStudent(@PathVariable Long id) {
        adminService.deactivateStudent(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessagesFromStudents(){
        List<Message> messages = adminService.getAllMessagesFromStudents();
        return ResponseEntity.ok(messages);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/messages/reply/{studentId}")
    public ResponseEntity<Message> replyToStudent(@PathVariable Long studentId, @RequestBody String content){
        Message message = adminService.replyToStudent(studentId, content);
        return ResponseEntity.ok(message);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/news")
    public ResponseEntity<News> createNews(@RequestBody News news) {
        News createdNews = newsService.createNews(news);
        return ResponseEntity.ok(createdNews);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/news/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News updatedNews) {
        News news = newsService.updateNews(id, updatedNews);
        return ResponseEntity.ok(news);
    }

    @DeleteMapping("/news/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/students/assign-role/{id}")
    public ResponseEntity<?> assignRoleToStudent(@PathVariable Long id, @RequestBody Role role) {
        try {
            adminService.assignRoleToStudent(id, role);
            return ResponseEntity.ok("Роль " + role + " успешно назначена студенту с ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/email")
    public ResponseEntity<Optional<Admin>> findAdminsByEmail(@RequestBody String email) {
        Optional<Admin> admin = adminService.findAdminByEmail(email);
        return ResponseEntity.ok(admin);
    }

}
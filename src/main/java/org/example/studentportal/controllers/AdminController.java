package org.example.studentportal.controllers;
import org.example.studentportal.dto.LessonRequest;
import org.example.studentportal.dto.StudentGroupRequest;
import org.example.studentportal.modul.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.studentportal.service.AdminService;
import org.example.studentportal.service.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Admin Controller", description = "API для Админов")
@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final NewsService newsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(adminService.getAllStudents());
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
        return ResponseEntity.ok(adminService.getAllMessagesFromStudents());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/messages/reply/{studentId}")
    public ResponseEntity<Message> replyToStudent(@PathVariable Long studentId, @RequestBody String content){
        return ResponseEntity.ok(adminService.replyToStudent(studentId, content));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/news")
    public ResponseEntity<News> createNews(@RequestBody News news) {
        return ResponseEntity.ok(newsService.createNews(news));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/news/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News updatedNews) {
        return ResponseEntity.ok(newsService.updateNews(id, updatedNews));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/news/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/students/assign-role/{id}")
    public ResponseEntity<String> assignRoleToStudent(@PathVariable Long id, @RequestParam Role role) {
        boolean success = adminService.assignRoleToStudent(id, role);
        return success ? ResponseEntity.ok("Роль успешно назначена студенту с ID: " + id)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Студент с ID " + id + " не найден.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/email")
    public ResponseEntity<Optional<Admin>> findAdminsByEmail(@RequestBody String email) {
        Optional<Admin> admin = adminService.findAdminByEmail(email);
        return ResponseEntity.ok(admin);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-group")
    public ResponseEntity<StudentGroup> createGroup(@RequestBody StudentGroup studentGroup) {
        return ResponseEntity.ok(adminService.createGroup(studentGroup));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-lesson")
    public ResponseEntity<String> createLesson(@RequestBody LessonRequest lessonRequest) {
        adminService.createLesson(lessonRequest);
        return ResponseEntity.ok("Урок успешно создан!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-student-to-group")
    public ResponseEntity<Void> addStudentToGroup(@RequestBody StudentGroupRequest request){
        adminService.addStudentToGroup(request.getStudentEmail(), request.getGroupId());
        return ResponseEntity.noContent().build();
    }
}

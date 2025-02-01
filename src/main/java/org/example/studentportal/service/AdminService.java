package org.example.studentportal.service;

import org.example.studentportal.modul.Admin;
import org.example.studentportal.modul.Message;
import org.example.studentportal.modul.Role;
import org.example.studentportal.modul.Student;
import org.example.studentportal.repository.AdminRepository;
import org.example.studentportal.repository.MessageRepository;
import org.example.studentportal.repository.StudentRepository;
import org.example.studentportal.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final MessageRepository messageRepository;
    private final SecurityUtil securityUtil;

    public AdminService(AdminRepository adminRepository,
                        StudentRepository studentRepository,
                        MessageRepository messageRepository,
                        SecurityUtil securityUtil) {
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
        this.messageRepository = messageRepository;
        this.securityUtil = securityUtil;
    }

    // Получение всех сообщений от студентов
    public List<Message> getAllMessagesFromStudents() {
        return messageRepository.findAll();
    }

    // Отправка сообщения студенту
    @Transactional
    public Message replyToStudent(Long studentId, String content) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Студент с ID " + studentId + " не найден."));

        Message replyMessage = Message.builder()
                .senderId(securityUtil.getCurrentUserId()) // ID администратора
                .recipientId(student.getId()) // ID студента
                .content(content)
                .timestamp(LocalDateTime.now()) // Время отправки
                .build();

        return messageRepository.save(replyMessage);
    }

    // Получение всех студентов
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Удаление студента
    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // Деактивация студента
    @Transactional
    public void deactivateStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Студент с ID " + id + " не найден."));
        student.setActive(false);
        studentRepository.save(student);
    }

    // Назначение роли студенту
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void assignRoleToStudent(Long studentId, Role role) {
     Student student = studentRepository.findById(studentId)
             .orElseThrow(() -> new RuntimeException("Student is not found"));
     student.setRole(role);
     studentRepository.save(student);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Optional<Admin> findAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}

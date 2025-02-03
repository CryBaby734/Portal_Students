package org.example.studentportal.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.studentportal.dto.LessonRequest;
import org.example.studentportal.modul.*;
import org.example.studentportal.repository.*;
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
    private final StudentGroupRepository studentGroupRepository;
    private final LessonRepository lessonRepository;



    public AdminService(AdminRepository adminRepository,
                        StudentRepository studentRepository,
                        MessageRepository messageRepository,
                        SecurityUtil securityUtil, StudentGroupRepository studentGroupRepository, LessonRepository lessonRepository, LessonFileRepository lessonFileRepository) {
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
        this.messageRepository = messageRepository;
        this.securityUtil = securityUtil;
        this.studentGroupRepository = studentGroupRepository;
        this.lessonRepository = lessonRepository;

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
    public boolean assignRoleToStudent(Long studentId, Role role) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Студент с ID " + studentId + " не найден."));

        if (student.getRole() == role) {
            throw new IllegalStateException("Студент уже имеет роль " + role);
        }

        student.setRole(role);
        studentRepository.save(student);
        return true; // ✅ Вернем true, если операция прошла успешно
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Optional<Admin> findAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public StudentGroup createGroup(StudentGroup studentGroup){
        if(studentGroupRepository.existsByName(studentGroup.getName())){
            throw new RuntimeException("Group such name is not exist!");
        }
        return studentGroupRepository.save(studentGroup);
    }

    @Transactional
    public void createLesson(LessonRequest lessonRequest) {
        // Проверяем, существует ли группа
        StudentGroup group = studentGroupRepository.findById(lessonRequest.getGroupId())
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));

        // Создаем новый урок и привязываем его к группе
        Lesson lesson = Lesson.builder()
                .group(group)
                .subject(lessonRequest.getSubject())
                .teacher(lessonRequest.getTeacher())
                .schedule(lessonRequest.getSchedule())
                .build();

        // Сохраняем урок
        lessonRepository.save(lesson);
    }

    public void addStudentToGroup(String studentEmail, Long groupId) {
        Student student = studentRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student is not found"));

        StudentGroup group = studentGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group is not found"));

        student.setGroup(group);
        studentRepository.save(student);
    }


}

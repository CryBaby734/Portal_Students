package org.example.studentportal.service;


import lombok.RequiredArgsConstructor;
import org.example.studentportal.modul.Admin;
import org.example.studentportal.modul.Message;
import org.example.studentportal.modul.Student;
import org.example.studentportal.repository.AdminRepository;
import org.example.studentportal.repository.MessageRepository;
import org.example.studentportal.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    public Message sendMessageToAdmin(Long studentId, String content) {
        // Проверяем существование студента
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Студент с ID " + studentId + " не найден."));

        // Получаем первого доступного администратора
        Admin admin = adminRepository.findFirstBy()
                .orElseThrow(() -> new RuntimeException("Администратор не найден."));

        // Создаем сообщение
        Message message = new Message();
        message.setSenderId(student.getId());
        message.setRecipientId(admin.getId());
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        // Сохраняем сообщение
        return messageRepository.save(message);
    }
}

package org.example.studentportal.util;


import org.example.studentportal.modul.Admin;
import org.example.studentportal.modul.Student;
import org.example.studentportal.repository.AdminRepository;
import org.example.studentportal.repository.StudentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    public SecurityUtil(StudentRepository studentRepository, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Получаем email пользователя из токена

        // Сначала ищем среди студентов
        Student student = studentRepository.findByEmail(email).orElse(null);
        if (student != null) {
            return student.getId();
        }

        // Если студент не найден, ищем среди администраторов
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь с email " + email + " не найден"));
        return admin.getId();
    }
}

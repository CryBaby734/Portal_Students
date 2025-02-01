package org.example.studentportal.config;

import org.example.studentportal.modul.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.example.studentportal.modul.Student;
import org.example.studentportal.repository.StudentRepository;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!studentRepository.existsByEmail("admin@example.com")) {
            Student admin = Student.builder()
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123")) // ✅ Хешируем пароль
                    .firstName("Admin") // ✅ Добавляем имя
                    .lastName("User") // ✅ Добавляем фамилию
                    .role(Role.ADMIN) // ✅ Используем Enum
                    .active(true)
                    .build();

            studentRepository.save(admin);
            System.out.println("✅ Администратор добавлен!");
        }
    }
}

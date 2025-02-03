package org.example.studentportal.config;

import org.example.studentportal.modul.Admin;
import org.example.studentportal.modul.Student;
import org.example.studentportal.repository.AdminRepository;
import org.example.studentportal.repository.StudentRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository; // Добавляем репозиторий админов

    public CustomUserDetailsService(StudentRepository studentRepository, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Проверяем сначала студента
        Student student = studentRepository.findByEmail(email).orElse(null);
        if (student != null) {
            return User.builder()
                    .username(student.getEmail())
                    .password(student.getPassword())
                    .roles(student.getRole().name()) // ✅ Преобразуем Enum в String
                    .build();
        }

        // Если студент не найден, ищем администратора
        Admin admin = adminRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("Пользователь с email " + email + " не найден"));

        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword())
                .roles(admin.getRole().name()) // ✅ Преобразуем Enum в String
                .build();
    }
}

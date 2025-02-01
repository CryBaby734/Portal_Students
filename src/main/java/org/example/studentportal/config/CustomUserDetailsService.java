package org.example.studentportal.config;

import org.example.studentportal.modul.Student;
import org.example.studentportal.repository.StudentRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    public CustomUserDetailsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // ✅ Теперь ищем пользователя (студента или админа) только в StudentRepository
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с email " + email + " не найден"));

        return User.builder()
                .username(student.getEmail())
                .password(student.getPassword()) // Пароль уже зашифрован
                .roles(student.getRole().name()) // ✅ Преобразуем Enum в String
                .build();
    }
}

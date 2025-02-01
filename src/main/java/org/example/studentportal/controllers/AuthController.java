package org.example.studentportal.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.studentportal.dto.RegistrationRequest;
import org.example.studentportal.modul.Admin;
import org.example.studentportal.modul.Role;
import org.example.studentportal.modul.Student;
import org.example.studentportal.repository.StudentRepository;
import org.example.studentportal.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth Controller", description = "API для Авторизации")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 📌 Авторизация (логин)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            // 🔍 Ищем пользователя в базе
            Student student = studentRepository.findByEmail(authRequest.getEmail()).orElse(null);

            if (student == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь не найден");
            }

            // ✅ Генерируем токен с ролями
            String token = jwtUtil.generateToken(student.getEmail(), student.getRole());

            return ResponseEntity.ok(new AuthResponse(token, student.getRole().name()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
        }
    }

    // 📌 Регистрация студента
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {
        try {
            // Проверяем, существует ли студент с таким email
            if (studentRepository.existsByEmail(registrationRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Студент с таким email уже существует");
            }

            // ✅ Создание нового студента с Role.STUDENT
            Student newStudent = Student.builder()
                    .email(registrationRequest.getEmail())
                    .password(passwordEncoder.encode(registrationRequest.getPassword()))
                    .firstName(registrationRequest.getFirstName())
                    .lastName(registrationRequest.getLastName())
                    .role(Role.STUDENT) // ✅ Теперь используем Enum Role
                    .active(true) // Активен после регистрации
                    .build();

            studentRepository.save(newStudent);

            return ResponseEntity.ok("Регистрация успешна");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка регистрации");
        }
    }

    // DTO для авторизации (логина)
    public static class AuthRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // DTO для ответа с токеном
    public static class AuthResponse {
        private final String token;
        private final String role; // ✅ Теперь Enum Role

        public AuthResponse(String token, String role) {
            this.token = token;
            this.role = role;
        }

        public String getToken() {
            return token;
        }

        public String getRole() {
            return role;
        }
    }
}

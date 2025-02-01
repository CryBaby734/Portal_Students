package org.example.studentportal.repository;

import org.example.studentportal.modul.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email); // Для поиска пользователей по email
    boolean existsByEmail(String email); // Проверка существования пользователя
}

package org.example.studentportal.repository;

import org.example.studentportal.modul.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findFirstBy(); // Находит первого администратора
    Optional<Admin> findByEmail(String email); // Исправленный метод
}
package org.example.studentportal.service;

import org.example.studentportal.modul.Student;
import org.example.studentportal.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Получение профиля студента
    public Student getStudentProfile(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student with ID " + id + " not found"));
    }

    // Обновление профиля студента
    public Student updateStudentProfile(Long id, Student updatedStudent) {
        Student student = getStudentProfile(id);
        student.setFirstName(updatedStudent.getFirstName());
        student.setLastName(updatedStudent.getLastName());
        student.setEmail(updatedStudent.getEmail());
        return studentRepository.save(student);
    }
}

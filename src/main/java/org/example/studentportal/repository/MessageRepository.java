package org.example.studentportal.repository;

import org.example.studentportal.modul.Message;
import org.example.studentportal.modul.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByStudent(Student student);
}

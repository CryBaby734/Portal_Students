package org.example.studentportal.modul;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long senderId; // ID отправителя (студент или админ)

    @Column(nullable = false)
    private Long recipientId; // ID получателя (студент или админ)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // Содержание сообщения

    @Column(nullable = false)
    private LocalDateTime timestamp; // Время отправки

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = true) // Поле может быть null
    private Student student; // Связь с таблицей студентов
}

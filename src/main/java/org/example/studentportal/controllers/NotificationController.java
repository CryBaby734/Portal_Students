package org.example.studentportal.controllers;

import org.example.studentportal.dto.NotificationDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Разрешаем запросы с фронта
public class NotificationController {

    @MessageMapping("/sendNotification") // Фронт отправляет на /app/sendNotification
    @SendTo("/topic/notifications") // Все подписанные клиенты получают на /topic/notifications
    public NotificationDTO sendNotification(NotificationDTO notification) {
        return notification; // Отправляем обратно всем клиентам
    }
}

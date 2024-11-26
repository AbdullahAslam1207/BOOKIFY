package com.example.demo.oop.factories;

import com.example.demo.database.queries.NotificationQueries;
import com.example.demo.oop.models.Notification;

import java.util.List;

public class NotificationFactory {
    private final NotificationQueries notificationQueries;

    public NotificationFactory() {
        this.notificationQueries = new NotificationQueries();
    }

    public boolean sendNotification(int userId, String description, String type) {
        return notificationQueries.insertNotification(userId, description, type);
    }
    // Get notifications for a specific user
    public List<Notification> getNotificationsByUserId(int userId) {
        return notificationQueries.fetchNotificationsByUserId(userId);
    }
}

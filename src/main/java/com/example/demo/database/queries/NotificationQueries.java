package com.example.demo.database.queries;

import com.example.demo.database.DatabaseConnection;
import com.example.demo.oop.models.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NotificationQueries {
    private final DatabaseConnection dbConnection;

    public NotificationQueries() {
        this.dbConnection = new DatabaseConnection();
    }

    public boolean insertNotification(int userId, String description, String type) {
        String query = "INSERT INTO Notifications (user_id, description, date, time, type, status) " +
                "VALUES (?, ?, CURDATE(), CURTIME(), ?, 'unread')";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setString(2, description);
            statement.setString(3, type);

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // Fetch notifications by user ID
    public List<Notification> fetchNotificationsByUserId(int userId) {
        String query = "SELECT * FROM Notifications WHERE user_id = ? ORDER BY date DESC, time DESC";
        List<Notification> notifications = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    notifications.add(new Notification(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getString("description"),
                            resultSet.getDate("date"),
                            resultSet.getTime("time"),
                            resultSet.getString("type"),
                            resultSet.getString("status")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return notifications;
    }
}

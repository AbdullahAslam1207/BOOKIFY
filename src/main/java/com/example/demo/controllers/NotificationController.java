package com.example.demo.controllers;

import com.example.demo.oop.factories.NotificationFactory;
import com.example.demo.oop.models.Notification;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class NotificationController {

    @FXML
    private VBox notificationListContainer;

    private NotificationFactory notificationFactory;
    private int userId;

    public void initialize() {
        notificationFactory = new NotificationFactory();
    }

    public void setUserId(int userId) {
        this.userId = userId;
        loadNotifications();
    }

    private void loadNotifications() {
        List<Notification> notifications = notificationFactory.getNotificationsByUserId(userId);
        notificationListContainer.getChildren().clear();

        if (notifications != null && !notifications.isEmpty()) {
            for (Notification notification : notifications) {
                HBox notificationRow = new HBox(10);
                notificationRow.setStyle("-fx-padding: 10; -fx-background-color: #2d2d2d; -fx-border-color: gold; -fx-border-radius: 5; -fx-background-radius: 5;");

                Label descriptionLabel = new Label("Description: " + notification.getDescription());
                descriptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

                Label dateLabel = new Label("Date: " + notification.getDate());
                dateLabel.setStyle("-fx-text-fill: gold; -fx-font-size: 14px;");

                Label timeLabel = new Label("Time: " + notification.getTime());
                timeLabel.setStyle("-fx-text-fill: gold; -fx-font-size: 14px;");

                Label statusLabel = new Label("Status: " + notification.getStatus());
                statusLabel.setStyle("-fx-text-fill: " + ("unread".equals(notification.getStatus()) ? "red;" : "green;") + " -fx-font-size: 14px;");

                notificationRow.getChildren().addAll(descriptionLabel, dateLabel, timeLabel, statusLabel);
                notificationListContainer.getChildren().add(notificationRow);
            }
        } else {
            Label noNotificationsLabel = new Label("No notifications available.");
            noNotificationsLabel.setStyle("-fx-text-fill: gold; -fx-font-size: 14px;");
            notificationListContainer.getChildren().add(noNotificationsLabel);
        }
    }
}

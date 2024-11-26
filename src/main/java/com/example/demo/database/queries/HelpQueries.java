package com.example.demo.database.queries;

import com.example.demo.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelpQueries {

    private final DatabaseConnection dbConnection;

    public HelpQueries() {
        dbConnection = new DatabaseConnection();
    }

    public List<String> fetchChatHistoryByUserId(int userId) {
        List<String> chatHistory = new ArrayList<>();
        String query = "SELECT role, message, created_at FROM HelpIssues WHERE user_id = ? ORDER BY created_at";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String role = resultSet.getString("role");
                String message = resultSet.getString("message");
                String timestamp = resultSet.getString("created_at");
                chatHistory.add(role + ": " + message + " (" + timestamp + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatHistory;
    }

    public boolean addMessage(int userId, String message, String role) {
        String query = "INSERT INTO HelpIssues (user_id, message, role) VALUES (?, ?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setString(2, message);
            statement.setString(3, role);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> fetchPendingIssues() {
        List<String> issues = new ArrayList<>();
        String query = "SELECT DISTINCT user_id FROM HelpIssues WHERE role = 'customer'";
        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                issues.add("User ID: " + resultSet.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return issues;
    }
}

package com.example.demo.database.queries;


import com.example.demo.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsQueries {

    private final DatabaseConnection dbConnection;

    public AnalyticsQueries() {
        this.dbConnection = new DatabaseConnection();
    }

    public Map<String, Integer> fetchCompletedBookingsByService() {
        Map<String, Integer> data = new HashMap<>();
        String query = "SELECT s.name, COUNT(b.id) AS total_bookings " +
                "FROM Bookings b " +
                "JOIN Services s ON b.service_id = s.id " +
                "WHERE b.status = 'completed' " +
                "GROUP BY s.name";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                data.put(resultSet.getString("name"), resultSet.getInt("total_bookings"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public Map<String, Double> fetchRevenueByService() {
        Map<String, Double> data = new HashMap<>();
        String query = "SELECT s.name, SUM(b.price) AS revenue " +
                "FROM Bookings b " +
                "JOIN Services s ON b.service_id = s.id " +
                "WHERE b.status = 'completed' " +
                "GROUP BY s.name";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                data.put(resultSet.getString("name"), resultSet.getDouble("revenue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public Map<String, Integer> fetchUserSignupsOverTime() {
        Map<String, Integer> data = new HashMap<>();
        String query = "SELECT DATE(created_at) AS signup_date, COUNT(id) AS total_users " +
                "FROM Users " +
                "GROUP BY signup_date";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                data.put(resultSet.getString("signup_date"), resultSet.getInt("total_users"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}

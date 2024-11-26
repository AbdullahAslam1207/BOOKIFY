package com.example.demo.database.queries;

import com.example.demo.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ServiceQueries {
    private final DatabaseConnection dbConnection;

    public ServiceQueries() {
        this.dbConnection = new DatabaseConnection();
    }

    public int insertService(String name,  String serviceType, int serviceProviderId) {
        String query = "INSERT INTO Services (name,  type, owner_id) VALUES (?,  ?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.setString(2, serviceType);
            statement.setInt(3, serviceProviderId);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public Map<String, Integer> getServiceAnalytics(int ownerId) {
        String query = "SELECT s.name, COUNT(b.id) AS booking_count " +
                "FROM Services s " +
                "LEFT JOIN Bookings b ON s.id = b.service_id AND b.status = 'completed' " +
                "WHERE s.owner_id = ? " +
                "GROUP BY s.name";
        Map<String, Integer> analyticsData = new HashMap<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ownerId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                analyticsData.put(resultSet.getString("name"), resultSet.getInt("booking_count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return analyticsData;
    }

}

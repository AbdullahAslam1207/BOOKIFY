package com.example.demo.database.queries;

import com.example.demo.database.DatabaseConnection;
import com.example.demo.oop.models.Hotel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HotelQueries {
    private final DatabaseConnection dbConnection;

    public HotelQueries() {
        this.dbConnection = new DatabaseConnection();
    }

    public int insertHotelAndGetId(int serviceId, String name, String location, int totalRooms) {
        String query = "INSERT INTO Hotels (service_id, name, location, total_rooms) VALUES (?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, serviceId);
            statement.setString(2, name);
            statement.setString(3, location);
            statement.setInt(4, totalRooms);
            statement.executeUpdate();

            // Fetch the generated key
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated hotel ID
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if there was an error or no ID found
    }


    // Fetch the ID of the most recently added hotel
    public int getLastInsertedHotelId() {
        String query = "SELECT LAST_INSERT_ID() AS last_id";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("last_id"); // Return the last inserted hotel ID
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if there was an error or no ID found
    }
    public List<Hotel> fetchAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        String query = "SELECT * FROM Hotels";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                hotels.add(new Hotel(
                        resultSet.getInt("id"),
                        resultSet.getInt("service_id"),
                        resultSet.getString("name"),
                        resultSet.getString("location"),
                        resultSet.getInt("total_rooms")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotels;
    }
    public int fetchServiceIdByHotelId(int hotelId) {
        String query = "SELECT service_id FROM Hotels WHERE id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, hotelId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("service_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no serviceId found
    }



}

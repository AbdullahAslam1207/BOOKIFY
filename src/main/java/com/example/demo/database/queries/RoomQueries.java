package com.example.demo.database.queries;

import com.example.demo.database.DatabaseConnection;
import com.example.demo.oop.models.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoomQueries {
    private final DatabaseConnection dbConnection;

    public RoomQueries() {
        this.dbConnection = new DatabaseConnection();
    }

    public int insertRoom(int roomNumber, double price, int numberOfBeds, String type, String availability, int hotelId) {
        String query = "INSERT INTO Rooms (hotel_id, room_number, price, no_of_beds, type, availability) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, hotelId);
            statement.setInt(2, roomNumber);
            statement.setDouble(3, price);
            statement.setInt(4, numberOfBeds);
            statement.setString(5, type);
            statement.setString(6, availability);

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
    public List<Room> fetchAvailableRoomsByHotel(int hotelId, Date bookingDate) {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM Rooms WHERE hotel_id = ? AND availability = 'available'";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, hotelId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                rooms.add(new Room(
                        resultSet.getInt("id"),
                        resultSet.getInt("hotel_id"),
                        resultSet.getInt("room_number"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("no_of_beds"),
                        resultSet.getString("type"),
                        resultSet.getString("availability")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }
    public Room fetchRoomByIdAndHotelId(int roomId, int hotelId) {
        String query = "SELECT * FROM Rooms WHERE id = ? AND hotel_id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, roomId);
            statement.setInt(2, hotelId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Room(
                        resultSet.getInt("id"),
                        resultSet.getInt("hotel_id"),
                        resultSet.getInt("room_number"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("no_of_beds"),
                        resultSet.getString("type"),
                        resultSet.getString("availability")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if no room found
    }
    public boolean updateRoomAvailability(int roomId, int hotelId, String availability) {
        String query = "UPDATE Rooms SET availability = ? WHERE id = ? AND hotel_id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, availability);
            statement.setInt(2, roomId);
            statement.setInt(3, hotelId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateRoomAvailabilityAfterDeletion() {
        String query = "UPDATE Rooms r "
                + "LEFT JOIN RoomBookings rb ON r.id = rb.room_id "
                + "SET r.availability = 'available' "
                + "WHERE rb.room_id IS NULL";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.executeUpdate(); // Update room availability
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}

package com.example.demo.database.queries;

import com.example.demo.database.DatabaseConnection;
import com.example.demo.oop.models.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingQueries {
    private final DatabaseConnection dbConnection;

    public BookingQueries() {
        this.dbConnection = new DatabaseConnection();
    }

    public int insertBooking(int userId, int serviceId, java.sql.Date bookingDate, double price) {
        String query = "INSERT INTO Bookings (user_id, service_id, booking_date, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, userId);
            statement.setInt(2, serviceId);
            statement.setDate(3, bookingDate);
            statement.setDouble(4, price);

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

    public boolean insertRoomBooking(int bookingId, int roomId, int hotelId, int userId) {
        String query = "INSERT INTO RoomBookings (booking_id, room_id, hotel_id, user_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookingId);
            statement.setInt(2, roomId);
            statement.setInt(3, hotelId);
            statement.setInt(4, userId);

            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateBookingStatus(int bookingId, String status, double amount) {
        String query = "UPDATE Bookings SET status = ?, price = ? WHERE id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, status);
            statement.setDouble(2, amount);
            statement.setInt(3, bookingId);

            return statement.executeUpdate() > 0; // Return true if the update was successful
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Return false if there was an error
    }
    public boolean insertTableBooking(int bookingId, int tableId, int restaurantId, int userId) {
        String query = "INSERT INTO TableBookings (booking_id, table_id, restaurant_id, user_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookingId);
            statement.setInt(2, tableId);
            statement.setInt(3, restaurantId);
            statement.setInt(4, userId);

            statement.executeUpdate();
            return true; // Return true if insertion was successful
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Return false if an error occurred
    }
    public List<Booking> fetchCompletedBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String query = """
        SELECT 
            b.id AS booking_id, 
            b.service_id, 
            b.booking_date, 
            b.status, 
            b.price,
            s.name AS service_name 
        FROM 
            Bookings b 
        INNER JOIN 
            Services s 
        ON 
            b.service_id = s.id 
        WHERE 
            b.user_id = ? 
        AND 
            b.status = 'completed'
        """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Booking booking = new Booking(
                        resultSet.getInt("booking_id"),
                        userId,
                        resultSet.getInt("service_id"),
                        resultSet.getDate("booking_date"),
                        resultSet.getString("status"),
                        resultSet.getDouble("price")
                );
                //booking.setServiceName(resultSet.getString("service_name"));
                bookings.add(booking);
            }
        } catch ( SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    public boolean updateBookingDate(int bookingId, Date newDate) {
        String query = "UPDATE Bookings SET booking_date = ? WHERE id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDate(1, new java.sql.Date(newDate.getTime()));

            statement.setInt(2, bookingId);

            return statement.executeUpdate() > 0; // Return true if the booking date is updated
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteBooking(int bookingId) {
        String query = "DELETE FROM Bookings WHERE id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookingId);

            return statement.executeUpdate() > 0; // Return true if the booking is deleted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Booking> getCompletedRoomBookings(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.id, b.booking_date, b.status,b.price,b.service_id, rb.hotel_id, rb.room_id "
                + "FROM Bookings b "
                + "JOIN RoomBookings rb ON b.id = rb.booking_id "
                + "WHERE b.user_id = ? AND b.status = 'completed'";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Booking booking = new Booking(
                            resultSet.getInt("id"),
                            userId,
                            resultSet.getInt("service_id"), // Assuming hotel_id as service_id for Room
                            resultSet.getDate("booking_date"),
                            resultSet.getString("status"),
                            resultSet.getDouble("price")
                    );
                    System.out.println("we have a booking");
                    bookings.add(booking);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }
    public List<Booking> getCompletedTableBookings(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.id, b.booking_date, b.status,b.price,b.service_id, tb.restaurant_id, tb.table_id "
                + "FROM Bookings b "
                + "JOIN TableBookings tb ON b.id = tb.booking_id "
                + "WHERE b.user_id = ? AND b.status = 'completed'";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Booking booking = new Booking(
                            resultSet.getInt("id"),
                            userId,
                            resultSet.getInt("service_id"), // Assuming restaurant_id as service_id for Table
                            resultSet.getDate("booking_date"),
                            resultSet.getString("status"),
                            resultSet.getDouble("price") // Price is not retrieved here; modify query to include it if needed
                    );
                    bookings.add(booking);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }





}

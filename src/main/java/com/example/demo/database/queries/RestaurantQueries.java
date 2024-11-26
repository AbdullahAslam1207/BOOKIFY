package com.example.demo.database.queries;

import com.example.demo.database.DatabaseConnection;
import com.example.demo.oop.models.Restaurant;
import com.example.demo.oop.models.RestaurantTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantQueries {
    private final DatabaseConnection dbConnection;

    public RestaurantQueries() {
        this.dbConnection = new DatabaseConnection();
    }



    public int insertRestaurantAndGetId(String name,int serviceId, int totalTables) {
        String query = "INSERT INTO Restaurants (service_id,name, total_tables) VALUES (?,?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, serviceId);
            statement.setString(2, name);
            statement.setInt(3, totalTables);
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1); // Return generated restaurant ID
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int insertTable(int restaurantId, int tableNumber, int capacity, String availability) {
        String query = "INSERT INTO RestaurantTables (restaurant_id, table_number, capacity, availability) VALUES (?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, restaurantId);
            statement.setInt(2, tableNumber);
            statement.setInt(3, capacity);
            statement.setString(4, availability);
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1); // Return generated table ID
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public List<Restaurant> getAllRestaurants() {
        String query = "SELECT * FROM Restaurants";
        List<Restaurant> restaurants = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery())  {

            while (resultSet.next()) {
                //Restaurant restaurant = new Restaurant();
                restaurants.add( new Restaurant(
                    resultSet.getInt("id"),
                        resultSet.getInt("service_id"),
                        resultSet.getString("name"),


                    resultSet.getInt("total_tables")

                ));

            }

        } catch (SQLException e) {
            System.err.println("Error fetching all restaurants: " + e.getMessage());
        }

        return restaurants;
    }

    public List<RestaurantTable> getAvailableTables(int restaurantId) {
        List<RestaurantTable> tables = new ArrayList<>();
        String query = "SELECT * FROM RestaurantTables WHERE restaurant_id = ? AND availability = 'available'";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, restaurantId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    RestaurantTable table = new RestaurantTable(
                            resultSet.getInt("id"),
                            resultSet.getInt("restaurant_id"),
                            resultSet.getInt("table_number"),
                            resultSet.getInt("capacity"),
                            resultSet.getString("availability")
                    );
                    tables.add(table);
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching available tables for restaurant ID " + restaurantId + ": " + e.getMessage());
        }

        return tables;
    }


    public int fetchServiceIdByRestaurantId(int hotelId) {
        String query = "SELECT service_id FROM Restaurants  WHERE id = ?";
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
    public RestaurantTable fetchTableByIdAndRestaurantId(int tableId, int restaurantId) {
        String query = "SELECT * FROM RestaurantTables WHERE id = ? AND restaurant_id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, tableId);
            statement.setInt(2, restaurantId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new RestaurantTable(
                        resultSet.getInt("id"),
                        resultSet.getInt("restaurant_id"),
                        resultSet.getInt("table_number"),
                        resultSet.getInt("capacity"),
                        resultSet.getString("availability")
                );
            }
        } catch (Exception e) {
            System.err.println("Error fetching table by ID and Restaurant ID: " + e.getMessage());
        }
        return null; // Return null if no table found
    }
    public boolean updateTableAvailability(int tableId, int restaurantId, String availability) {
        String query = "UPDATE RestaurantTables SET availability = ? WHERE id = ? AND restaurant_id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, availability);
            statement.setInt(2, tableId);
            statement.setInt(3, restaurantId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if rows were updated
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Return false if an error occurred or no rows were updated
    }
    public void updateTableAvailabilityAfterDeletion() {
        String query = "UPDATE RestaurantTables t "
                + "LEFT JOIN TableBookings tb ON t.id = tb.table_id "
                + "SET t.availability = 'available' "
                + "WHERE tb.table_id IS NULL";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.executeUpdate(); // Update table availability
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}

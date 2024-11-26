package com.example.demo.database.queries;

import com.example.demo.database.DatabaseConnection;
import com.example.demo.oop.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserQueries {
    private final DatabaseConnection dbConnection;

    public UserQueries() {
        this.dbConnection = new DatabaseConnection();
    }

    // Method for authenticating users
    public ResultSet authenticateUser(String email, String password, String role) {
        String query = "SELECT * FROM Users WHERE email = ? AND password = ? AND role = ?";
        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, role);
            return statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to insert a new user
    public int insertUser(String name, String email, String password, String phoneNumber, String role) {
        String query = "INSERT INTO Users (name, email, password, phone_number, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, phoneNumber);
            statement.setString(5, role);
            statement.executeUpdate();

            // Get the generated user ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Return the new user ID
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Insert into role-specific table
    public boolean insertRoleSpecific(int userId, String role) {
        String query = "";
        switch (role.toLowerCase()) {
            case "customer":
                query = "INSERT INTO Customers (id) VALUES (?)";
                break;
            case "service_owner":
                query = "INSERT INTO ServiceOwners (user_id) VALUES (?)";
                break;
            case "admin":
                return false; // Admin cannot sign up
        }

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<User> getAllCustomers() {
        List<User> customers = new ArrayList<>();
        String query = """
            SELECT u.id, u.name, u.email, u.password, u.role, u.phone_number
            FROM Users u
            JOIN Customers c ON u.id = c.id
            WHERE u.role = 'customer'
            """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User customer = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("role"),
                        resultSet.getString("phone_number")
                );
                customers.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }

}

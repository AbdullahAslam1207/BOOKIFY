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
    public int insertPendingServiceOwner(String name, String email, String password, String phoneNumber) {
        String query = "INSERT INTO PendingServiceOwners (name, email, password, phone_number) VALUES (?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, phoneNumber);
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
    public List<User> getPendingServiceOwners() {
        String query = "SELECT * FROM PendingServiceOwners WHERE status = 'pending'";
        List<User> pendingOwners = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                pendingOwners.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        "service_owner", // Hardcoding role as service_owner
                        resultSet.getString("phone_number")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pendingOwners;
    }
    public boolean approveServiceOwner(int pendingId) {
        String getQuery = "SELECT * FROM PendingServiceOwners WHERE id = ?";
        String insertUserQuery = "INSERT INTO Users (name, email, password, phone_number, role) VALUES (?, ?, ?, ?, 'service_owner')";
        String insertServiceOwnerQuery = "INSERT INTO ServiceOwners (user_id) VALUES (?)";
        String updatePendingQuery = "UPDATE PendingServiceOwners SET status = 'approved' WHERE id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement getStatement = connection.prepareStatement(getQuery);
             PreparedStatement insertUserStatement = connection.prepareStatement(insertUserQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement insertServiceOwnerStatement = connection.prepareStatement(insertServiceOwnerQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updatePendingQuery)) {

            getStatement.setInt(1, pendingId);
            ResultSet resultSet = getStatement.executeQuery();

            if (resultSet.next()) {
                // Insert into Users
                insertUserStatement.setString(1, resultSet.getString("name"));
                insertUserStatement.setString(2, resultSet.getString("email"));
                insertUserStatement.setString(3, resultSet.getString("password"));
                insertUserStatement.setString(4, resultSet.getString("phone_number"));
                insertUserStatement.executeUpdate();

                ResultSet generatedKeys = insertUserStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    // Insert into ServiceOwners
                    insertServiceOwnerStatement.setInt(1, userId);
                    insertServiceOwnerStatement.executeUpdate();

                    // Update PendingServiceOwners
                    updateStatement.setInt(1, pendingId);
                    updateStatement.executeUpdate();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean disapproveServiceOwner(int pendingId) {
        String query = "UPDATE PendingServiceOwners SET status = 'disapproved' WHERE id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, pendingId);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public User fetchUserDetailsById(int userId) {
        String query = "SELECT id, name, email, phone_number FROM Users WHERE id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        null, // Password is not fetched for security
                        null, // Role is not required
                        resultSet.getString("phone_number")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUserDetails(int userId, String name, String email, String phone) {
        String query = "UPDATE Users SET name = ?, email = ?, phone_number = ? WHERE id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);
            statement.setInt(4, userId);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changePassword(int userId, String currentPassword, String newPassword) {
        String query = "UPDATE Users SET password = ? WHERE id = ? AND password = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newPassword);
            statement.setInt(2, userId);
            statement.setString(3, currentPassword);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }






}

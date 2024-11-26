package com.example.demo.database.queries;

import com.example.demo.database.DatabaseConnection;
import com.example.demo.oop.models.DiscountCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DiscountCodeQueries {
    private final DatabaseConnection dbConnection;

    public DiscountCodeQueries() {
        this.dbConnection = new DatabaseConnection();
    }

    public boolean insertDiscountCode(int userId, String code, int discountPercentage, java.sql.Date validity) {
        String query = "INSERT INTO DiscountCodes (user_id, code, discount_percentage, validity) VALUES (?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setString(2, code);
            statement.setInt(3, discountPercentage);
            statement.setDate(4, validity);

            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public  DiscountCode getDiscountCodeByUserAndCode(int userId, String code) {
        String query = "SELECT * FROM DiscountCodes WHERE user_id = ? AND code = ?  AND is_used = FALSE";
        //System.out.println("user id and code is + "+ userId+code);
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setString(2, code);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                //System.out.println(resultSet.getInt("user_id"));
                return new DiscountCode(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("code"),
                        resultSet.getInt("discount_percentage"),
                        resultSet.getDate("validity"),
                        resultSet.getBoolean("is_used")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean markCodeAsUsed(int id) {
        String query = "UPDATE DiscountCodes SET is_used = TRUE WHERE id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean createDiscountCode(int userId, String discountCode, int discount, String validity) {
        String query = "INSERT INTO DiscountCodes (user_id, code, discount_percentage, validity) VALUES (?, ?, ?, ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setString(2, discountCode);
            statement.setInt(3, discount);
            statement.setString(4, validity);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Returns true if at least one row was inserted
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Return false if an exception occurs
    }
}

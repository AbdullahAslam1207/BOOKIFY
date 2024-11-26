package com.example.demo.database.queries;

import com.example.demo.database.DatabaseConnection;
import com.example.demo.oop.models.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PaymentQueries {
    private final DatabaseConnection dbConnection;

    public PaymentQueries() {
        this.dbConnection = new DatabaseConnection();
    }

    public boolean insertPayment(int bookingId, int userId, double amount, String paymentMethod, String status) {
        String query = "INSERT INTO Payments (booking_id, user_id, amount, payment_method, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookingId);
            statement.setInt(2, userId);
            statement.setDouble(3, amount);
            statement.setString(4, paymentMethod);
            statement.setString(5, status);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean addPaymentForMembership(int membershipId, int userId, double amount, String paymentMethod, String status) {
        System.out.println("i am here with memid "+ membershipId);
        String query = "INSERT INTO Payments (membership_id, user_id, amount, payment_method, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, membershipId);
            statement.setInt(2, userId);
            statement.setDouble(3, amount);
            statement.setString(4, paymentMethod);
            statement.setString(5, status);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fetch all pending payments for a specific user
    public List<Payment> getPendingPayments(int userId) {
        String query = "SELECT * FROM Payments WHERE status = 'pending' AND user_id = ?";
        List<Payment> payments = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                payments.add(new Payment(
                        resultSet.getInt("id"),
                        resultSet.getInt("booking_id"),
                        resultSet.getInt("membership_id"),

                        resultSet.getInt("user_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("payment_method"),
                        resultSet.getTimestamp("payment_date"),
                        resultSet.getString("status")
                ));
            }
            System.out.println("userrrrr");
            System.out.println(userId);
            for (Payment payment : payments) {
                System.out.println("Payment Details: " + payment.getBookingId() + ", " + payment.getAmount() + ", " + payment.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return payments;
    }

    // Update payment status
    public boolean updatePaymentStatus(int bookingId, String status) {
        String query = "UPDATE Payments SET status = ? WHERE booking_id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, status);
            statement.setInt(2, bookingId);

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updatePaymentmemStatus(int memId, String status) {
        String query = "UPDATE Payments SET status = ? WHERE membership_id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, status);
            statement.setInt(2, memId);

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    // Update payment amount with a discount
    public boolean updatePaymentWithDiscount(int bookingId, double discountedAmount) {
        String query = "UPDATE Payments SET amount = ? WHERE booking_id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDouble(1, discountedAmount);
            statement.setInt(2, bookingId);

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public Payment getPaymentById(int bookingId1) {
        String query = "SELECT * FROM Payments WHERE booking_id = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookingId1);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int user_id=resultSet.getInt("user_id");
                    int bookingId = resultSet.getInt("booking_id");
                    double amount = resultSet.getDouble("amount");
                    String paymentMethod = resultSet.getString("payment_method");
                    Timestamp paymentDate = resultSet.getTimestamp("payment_date");
                    String status = resultSet.getString("status");

                    // Assuming the Payment class has a constructor for all these fields
                    return new Payment(id, bookingId,0,user_id ,amount, paymentMethod, paymentDate, status);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if no matching payment is found
    }

}

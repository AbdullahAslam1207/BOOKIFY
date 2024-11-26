package com.example.demo.database.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.demo.database.DatabaseConnection;
import com.example.demo.oop.models.Membership;


public class MembershipQueries {
    public boolean checkExistingMembership(int userId) {
        String query = "SELECT COUNT(*) FROM Memberships1 WHERE user_id = ? AND status = 'active'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true; // User already has an active membership
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addMembership(Membership membership) {
        String query = "INSERT INTO Memberships1 (user_id, type, discount_percentage, status, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, membership.getUserId());
            stmt.setString(2, membership.getType());
            stmt.setInt(3, membership.getDiscountPercentage());
            stmt.setString(4, "inactive"); // Status is initially inactive
            stmt.setDate(5, new java.sql.Date(membership.getStartDate().getTime()));
            stmt.setDate(6, new java.sql.Date(membership.getEndDate().getTime()));
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean activateMembership(int userId) {
        String query = "UPDATE Memberships1 SET status = 'active' WHERE id = ? AND status = 'inactive'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public Membership getMembershipByUserId(int userId) {
        String query = "SELECT * FROM Memberships1 WHERE user_id = ? AND status = 'active'";
        Membership membership = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                membership = new Membership();
                membership.setId(rs.getInt("id"));
                membership.setUserId(rs.getInt("user_id"));
                membership.setType(rs.getString("type"));
                membership.setDiscountPercentage(rs.getInt("discount_percentage"));
                membership.setStatus(rs.getString("status"));
                membership.setStartDate(rs.getDate("start_date"));
                membership.setEndDate(rs.getDate("end_date"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return membership;
    }

    public Membership getMembership(int userId) {
        String query = "SELECT * FROM Memberships1 WHERE user_id = ? AND status = 'inactive'";
        Membership membership = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                membership = new Membership();
                membership.setId(rs.getInt("id"));
                membership.setUserId(rs.getInt("user_id"));
                membership.setType(rs.getString("type"));
                membership.setDiscountPercentage(rs.getInt("discount_percentage"));
                membership.setStatus(rs.getString("status"));
                membership.setStartDate(rs.getDate("start_date"));
                membership.setEndDate(rs.getDate("end_date"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return membership;
    }
}

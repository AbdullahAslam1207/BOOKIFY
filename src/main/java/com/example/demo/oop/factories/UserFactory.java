package com.example.demo.oop.factories;

import com.example.demo.oop.models.User;
import com.example.demo.database.queries.UserQueries;

import java.sql.ResultSet;

public class UserFactory {
    private final UserQueries userQueries;

    public UserFactory() {
        this.userQueries = new UserQueries();
    }

    // Login Method
    public User loginUser(String email, String password, String role) {
        ResultSet resultSet = userQueries.authenticateUser(email, password, role);
        try {
            if (resultSet != null && resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Signup Method
    public boolean signUpUser(String name, String email, String password, String phoneNumber, String role) {
        // Step 1: Insert user into the Users table
        if (role.toLowerCase()=="service_owner"){
            PendingServiceOwnerFactory pp= new PendingServiceOwnerFactory();
            boolean id= pp.addPendingServiceOwner(name,email,password,phoneNumber);
            if (id){
                System.out.println("Inserted into check successfully");
                return  true;
            }
            else {
                System.out.println("Failed to insert into check");
                return false;
            }
        }
        else {


            int userId = userQueries.insertUser(name, email, password, phoneNumber, role);
            if (userId > 0) {
                // Step 2: Insert into role-specific table
                return userQueries.insertRoleSpecific(userId, role);
            }
            return false;
        }
    }
}

package com.example.demo.controllers;

import com.example.demo.database.queries.UserQueries;
import com.example.demo.oop.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MyAccount {

    @FXML
    private TextField nameField, emailField, phoneField, currentPasswordField, newPasswordField;

    @FXML
    private Button saveDetailsButton, changePasswordButton;

    private UserQueries userQueries;
    private int userId; // To store the logged-in user's ID

    public void initialize() {
        userQueries = new UserQueries();
        loadUserDetails();
    }

    public void setUserId(int userId) {
        this.userId = userId;
        loadUserDetails();
    }

    private void loadUserDetails() {
        User user = userQueries.fetchUserDetailsById(userId);
        if (user != null) {

            nameField.setText(user.getName());
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhoneNumber());
        }
    }

    @FXML
    private void handleSaveDetails() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        boolean isUpdated = userQueries.updateUserDetails(userId, name, email, phone);
        if (isUpdated) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Details updated successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update details. Please try again.");
        }
    }

    @FXML
    private void handleChangePassword() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        boolean isPasswordChanged = userQueries.changePassword(userId, currentPassword, newPassword);
        if (isPasswordChanged) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Incorrect current password or failed to change password.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

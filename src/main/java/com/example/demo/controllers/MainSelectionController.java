package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MainSelectionController {

    @FXML
    private Button adminButton, ownerButton, customerButton;

    @FXML
    private ImageView logoImageView;

    @FXML
    public void initialize() {
        // Load the logo image
        logoImageView.setImage(new Image(getClass().getResourceAsStream("/icons/bookify_logo-removebg-preview.png")));
    }

    private void openLoginSignup(String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/login-signup.fxml"));
            Parent root = loader.load();

            // Pass the selected role to LoginSignupController
            LoginSignupController controller = loader.getController();
            controller.setRole(role);

            // Open the LoginSignup page
            Stage stage = new Stage();
            stage.setTitle(role + " Login/Signup");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current window
            adminButton.getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void selectAdmin() {
        openLoginSignup("admin");
    }

    @FXML
    private void selectOwner() {
        openLoginSignup("service_owner");
    }

    @FXML
    private void selectCustomer() {
        openLoginSignup("customer");
    }
}

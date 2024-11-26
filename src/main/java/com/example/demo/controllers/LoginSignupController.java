package com.example.demo.controllers;

import com.example.demo.oop.factories.UserFactory;
import com.example.demo.oop.models.User;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginSignupController {

    @FXML
    private AnchorPane imagePane, formPane;

    @FXML
    private ImageView sideImage;

    @FXML
    private VBox loginForm, signupForm;

    @FXML
    private TextField usernameField, nameField, emailField, phoneNumberField;

    @FXML
    private PasswordField passwordField, signupPasswordField;

    @FXML
    private Button toggleButton;

    private boolean isSignup = false;

    @FXML
    private Label roleLabel;

    private String role;

    public void setRole(String role) {
        this.role = role;


        if (role.equals("Admin")) {
            // Admin can only login
            signupForm.setVisible(false);
            toggleButton.setVisible(false); // Hide toggle button for Admin
            roleLabel.setText(role + " Login");
        } else {
            // Service Owner and Customer can both login and signup
            signupForm.setVisible(false);
            toggleButton.setVisible(true);
            roleLabel.setText(role + " Login/Signup");
        }
    }

    @FXML
    public void initialize() {
        sideImage.setImage(new Image(getClass().getResourceAsStream("/icons/bookify_logo-removebg-preview.png")));
        signupForm.setVisible(false); // Ensure only the login form is visible at the start
    }

    @FXML
    private void switchToSignup() {
        if (!isSignup) {
            TranslateTransition imageTransition = new TranslateTransition(Duration.millis(400), imagePane);
            imageTransition.setToX(400);
            imageTransition.play();

            TranslateTransition formTransition = new TranslateTransition(Duration.millis(400), formPane);
            formTransition.setToX(-400);
            formTransition.play();

            loginForm.setVisible(false);
            signupForm.setVisible(true);

            toggleButton.setText("Login");
            isSignup = true;
        }
        else {
            switchToLogin();
        }
    }

    @FXML
    private void switchToLogin() {
        if (isSignup) {
            TranslateTransition imageTransition = new TranslateTransition(Duration.millis(400), imagePane);
            imageTransition.setToX(0);
            imageTransition.play();

            TranslateTransition formTransition = new TranslateTransition(Duration.millis(400), formPane);
            formTransition.setToX(0);
            formTransition.play();

            signupForm.setVisible(false);
            loginForm.setVisible(true);

            toggleButton.setText("Sign Up");
            isSignup = false;
        }
    }

    @FXML
    private void handleLogin() {
        UserFactory userFactory = new UserFactory();
        User user = userFactory.loginUser(usernameField.getText(), passwordField.getText(), role);

        if (user != null) {
            System.out.println(role + " logged in successfully!");
            int userId = user.getId();
            String userName = user.getName();

            loadDashboard(role, userName,userId);
            // Redirect to respective dashboard
        } else {
            System.out.println("Login failed! Check credentials.");
        }
    }


    @FXML
    private void handleSignup() {
        if (!role.equals("Admin")) {
            UserFactory userFactory = new UserFactory();
            boolean isCreated = userFactory.signUpUser(
                    nameField.getText(),
                    emailField.getText(),
                    signupPasswordField.getText(),
                    phoneNumberField.getText(),
                    role
            );

            if (isCreated) {
                System.out.println(role + " signed up successfully!");
            } else {
                System.out.println("Signup failed!");
            }
        } else {
            System.out.println("Admins cannot sign up through this form.");
        }
    }
    private void loadDashboard(String role, String name, int id) {
        try {
            FXMLLoader loader;
            Parent root;

            switch (role.toLowerCase()) {
                case "admin":
                    loader = new FXMLLoader(getClass().getResource("/com/example/demo/AdminDashboard.fxml"));
                    root = loader.load();
                    AdminDashboardController adminController = loader.getController();
                    adminController.setAdminDetails(name, id);
                    break;

                case "service_owner":
                    loader = new FXMLLoader(getClass().getResource("/com/example/demo/ServiceOwnerDashboard.fxml"));
                    root = loader.load();
                    ServiceOwnerDashboardController ownerController = loader.getController();
                    ownerController.setServiceOwnerDetails(name, id);
                    break;

                case "customer":
                    loader = new FXMLLoader(getClass().getResource("/com/example/demo/customerMain.fxml"));
                    root = loader.load();
                    customerMain customerController = loader.getController();
                    customerController.setCustomerDetails(name, id);
                    break;

                default:
                    throw new IllegalArgumentException("Invalid role: " + role);
            }

            // Set the new scene for the primary stage
            Stage primaryStage = (Stage) formPane.getScene().getWindow();
            primaryStage.setScene(new Scene(root, primaryStage.getWidth(), primaryStage.getHeight()));
            primaryStage.setTitle(role + " Dashboard"); // Update the title for clarity
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

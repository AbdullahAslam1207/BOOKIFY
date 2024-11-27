package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AdminDashboardController {
    @FXML
    private VBox sidebar;

    @FXML
    private Button collapseButton;


    @FXML
    private AnchorPane mainContent;

    @FXML
    private ImageView welcomeImage;

    private int adminId;
    private String adminname;

    private boolean isSidebarExpanded = true;

    public void initialize() {
        // Load the welcome image
        welcomeImage.setImage(new Image(getClass().getResourceAsStream("/icons/bookify_logo-removebg-preview.png")));
    }

    public void setAdminDetails(String name, int id) {
        this.adminId = id;
        this.adminname=name;
    }

    @FXML
    private void toggleSidebar() {
        if (isSidebarExpanded) {
            sidebar.setPrefWidth(50);
            collapseButton.setText("▶");
            isSidebarExpanded = false;
        } else {
            sidebar.setPrefWidth(200);
            collapseButton.setText("☰");
            isSidebarExpanded = true;
        }
    }

    @FXML
    private void loadViewBookingAdminPane() {
        loadPane("ViewBookingAdmin.fxml");
    }

    @FXML
    private void loadCustomerSupportAdminPane() {
        loadPane("CustomerSupportAdmin.fxml");
    }

    @FXML
    private void loadAuthorizeUserAdminPane() {
        loadPane("PendingOwners.fxml");
    }


    private void loadPane(String fxmlFile) {
        try {
            String path = "/com/example/demo/" + fxmlFile;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Node node = loader.load();
            mainContent.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleCustomerSupport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/CustomerSupportAdmin.fxml"));
            Node node = loader.load();

            // Pass userId to ViewBookingsController
            CustomerSupportAdminController controller = loader.getController();


            mainContent.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

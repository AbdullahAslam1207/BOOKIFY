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

public class ServiceOwnerDashboardController {

    @FXML
    private VBox sidebar;

    @FXML
    private Button collapseButton;


    @FXML
    private AnchorPane mainContent;

    @FXML
    private ImageView welcomeImage;

    private boolean isSidebarExpanded = true;
    private int ownerid;
    private  String ownername;

    public void initialize() {
        // Load the welcome image
        welcomeImage.setImage(new Image(getClass().getResourceAsStream("/icons/bookify_logo-removebg-preview.png")));
    }
    public void setServiceOwnerDetails(String name, int id) {
        this.ownerid= id;
        this.ownername=name;

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
    private void loadViewBookingsPane() {
        loadPane("ViewBookings.fxml");
    }

    @FXML
    private void loadCustomerSupportPane() {
        loadPane("CustomerSupport.fxml");
    }

    @FXML
    private void loadTargetedOffersPane() {
        loadPane("TargetedOffers.fxml");
    }



    @FXML
    private void loadAddServicePane() {
        loadPane("AddService.fxml");
    }


    private void loadPane(String fxmlFile) {
        try {
            String path = "/com/example/demo/" + fxmlFile;
            //System.out.println("Loading FXML file from: " + path);
            //String path="ServiceOwnerDashboard.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Node node = loader.load();

            if (fxmlFile=="AddService.fxml"){
                // Get the controller instance
                AddServiceController addservieController = loader.getController();

                // Pass the service provider ID to the controller
                addservieController.setServiceProviderId(ownerid);
            }

            mainContent.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loadServiceAnalyticsPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/ServiceAnalytics.fxml"));
            Node analyticsPane = loader.load();

            // Pass owner ID to the controller
            ServiceAnalyticsController analyticsController = loader.getController();
            analyticsController.setOwnerId(ownerid); // Assume `serviceOwnerId` is set during login

            // Load the pane into the main content area
            mainContent.getChildren().setAll(analyticsPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}

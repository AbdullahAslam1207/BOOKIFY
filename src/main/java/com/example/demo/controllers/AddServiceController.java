package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class AddServiceController {

    @FXML
    private Button cinemaButton, restaurantButton, hotelButton;

    @FXML
    private AnchorPane rootPane; // Add a root pane to replace the content dynamically
    private int serviceProviderId;

    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    @FXML
    private void handleCinemaButton() {
        System.out.println("Add Cinema button clicked!");
        // Navigate to the Add Cinema page when implemented
    }

    @FXML
    private void handleRestaurantButton() {
        System.out.println("Add Restaurant button clicked!");
        loadAddRestaurantPane();
        // Navigate to the Add Restaurant page when implemented
    }

    @FXML
    private void handleHotelButton() {
        System.out.println("Add Hotel button clicked!");
        loadAddHotelPane();
    }

    private void loadAddHotelPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/AddHotel.fxml"));
            Node addHotelPane = loader.load();

            // Get the controller instance
            AddHotelController addHotelController = loader.getController();

            // Pass the service provider ID to the controller
            addHotelController.setServiceProviderId(serviceProviderId);

            // Set the loaded pane into the root container
            rootPane.getChildren().setAll(addHotelPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadAddRestaurantPane() {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/demo/AddRestaurant.fxml");
            if (fxmlLocation == null) {
                System.out.println("FXML file not found!");
            } else {
                System.out.println("FXML file located at: " + fxmlLocation);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/AddRestaurant.fxml"));

            Node addRestaurantPane = loader.load();

            // Get the controller instance
            AddRestaurantController addRestaurantController = loader.getController();

            // Pass the service provider ID to the controller
            addRestaurantController.setServiceProviderId(serviceProviderId);

            // Set the loaded pane into the root container
            rootPane.getChildren().setAll(addRestaurantPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

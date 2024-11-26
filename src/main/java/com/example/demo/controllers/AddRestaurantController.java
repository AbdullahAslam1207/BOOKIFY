package com.example.demo.controllers;

import com.example.demo.oop.factories.RestaurantFactory;
import com.example.demo.oop.factories.RestaurantTableFactory;
import com.example.demo.oop.models.Restaurant;
import com.example.demo.oop.models.RestaurantTable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AddRestaurantController {

    @FXML
    private TextField restaurantNameField, totalTablesField;

    @FXML
    private VBox tableDetailsContainer;

    @FXML
    private Button submitRestaurantButton, addTableButton;

    private int serviceProviderId; // This will be set when the service provider logs in
    private RestaurantFactory restaurantFactory;
    private RestaurantTableFactory tableFactory;
    private int restaurantId; // ID of the restaurant created for this instance

    private int totalTablesToAdd;
    private int tablesAdded;

    public void initialize() {
        restaurantFactory = new RestaurantFactory();
        tableFactory = new RestaurantTableFactory();
        tableDetailsContainer.setVisible(false); // Initially hide table details section
        tablesAdded = 0;
    }

    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    @FXML
    private void handleAddRestaurant() {
        String restaurantName = restaurantNameField.getText();
        String totalTablesInput = totalTablesField.getText();

        if (restaurantName.isEmpty() || totalTablesInput.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fill in all fields.");
            return;
        }

        try {
            totalTablesToAdd = Integer.parseInt(totalTablesInput);
            if (totalTablesToAdd <= 0) {
                throw new NumberFormatException("Total tables must be greater than 0.");
            }

            // Create a restaurant instance
            Restaurant restaurant = restaurantFactory.createRestaurant(restaurantName, totalTablesToAdd, serviceProviderId);
            if (restaurant != null) {
                restaurantId = restaurant.getRestaurantId(); // Get the ID of the newly created restaurant
                showAlert(Alert.AlertType.INFORMATION, "Success", "Restaurant added successfully. Please add table details.");
                tableDetailsContainer.setVisible(true); // Show table details section
                submitRestaurantButton.setDisable(true); // Disable the "Add Restaurant" button
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add restaurant. Please try again.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Total tables must be a valid number.");
        }
    }

    @FXML
    private void handleAddTable() {
        if (tablesAdded >= totalTablesToAdd) {
            showAlert(Alert.AlertType.INFORMATION, "Tables Completed", "All tables have been added.");
            addTableButton.setDisable(true); // Disable the "Add Table" button
            return;
        }

        // Create fields for table details
        TextField tableNumberField = new TextField();
        tableNumberField.setPromptText("Table Number");

        TextField capacityField = new TextField();
        capacityField.setPromptText("Capacity");

        ComboBox<String> availabilityBox = new ComboBox<>();
        availabilityBox.getItems().addAll("Available", "Booked");
        availabilityBox.setPromptText("Availability");

        Button saveTableButton = new Button("Save Table");

        saveTableButton.setOnAction(event -> {
            try {
                int tableNumber = Integer.parseInt(tableNumberField.getText());
                int capacity = Integer.parseInt(capacityField.getText());
                String availability = availabilityBox.getValue();

                if (availability == null) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select availability.");
                    return;
                }

                // Add table to the database using the RestaurantTableFactory
                RestaurantTable table = tableFactory.createTable(restaurantId, tableNumber, capacity, availability);
                if (table != null) {
                    tablesAdded++;
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Table added successfully.");

                    // Disable the "Add Table" button when all tables are added
                    if (tablesAdded >= totalTablesToAdd) {
                        addTableButton.setDisable(true);
                        saveTableButton.setDisable(true);
                        showAlert(Alert.AlertType.INFORMATION, "Tables Completed", "All tables have been added.");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to add table. Please try again.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Table number and capacity must be valid numbers.");
            }
        });

        // Add table details dynamically to the tableDetailsContainer
        VBox tableDetails = new VBox(10, tableNumberField, capacityField, availabilityBox, saveTableButton);
        tableDetailsContainer.getChildren().add(tableDetails);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

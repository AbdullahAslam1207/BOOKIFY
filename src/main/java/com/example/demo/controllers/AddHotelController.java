package com.example.demo.controllers;

import com.example.demo.oop.factories.HotelFactory;
import com.example.demo.oop.factories.RoomFactory;
import com.example.demo.oop.factories.ServiceFactory;
import com.example.demo.oop.models.Hotel;
import com.example.demo.oop.models.Room;
import com.example.demo.oop.models.Service;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AddHotelController {

    @FXML
    private TextField hotelNameField, locationField, totalRoomsField;

    @FXML
    private VBox roomDetailsContainer;

    @FXML
    private Button submitHotelButton, addRoomsButton;

    private int serviceProviderId; // This will be set when the service provider logs in
    private HotelFactory hotelFactory;
    private RoomFactory roomFactory;
    private int hotelid;
    private ServiceFactory serviceFactory;

    private int serviceId; // ID of the service created for this hotel
    private int totalRoomsToAdd;
    static private int roomsAdded;

    public void initialize() {
        hotelFactory = new HotelFactory();
        roomFactory = new RoomFactory();
        serviceFactory = new ServiceFactory();
        roomDetailsContainer.setVisible(false); // Hide room details section initially
        roomsAdded = 0;
    }

    public void setServiceProviderId(int serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    @FXML
    private void handleAddHotel() {
        String hotelName = hotelNameField.getText();
        String location = locationField.getText();
        String totalRoomsInput = totalRoomsField.getText();

        if (hotelName.isEmpty() || location.isEmpty() || totalRoomsInput.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fill in all the fields.");
            return;
        }

        try {
            totalRoomsToAdd = Integer.parseInt(totalRoomsInput);
            if (totalRoomsToAdd <= 0) {
                throw new NumberFormatException("Total rooms must be greater than 0.");
            }

            // Step 1: Add service before adding the hotel
//            Service service = serviceFactory.createService(hotelName,"hotel", serviceProviderId);
//            if (service != null) {
//                serviceId = service.getId(); // Retrieve the ID of the created service
//            } else {
//                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add service. Please try again.");
//                return;
//            }

            Hotel hotel = hotelFactory.createHotel(hotelName, location, totalRoomsToAdd, serviceProviderId);
            this.hotelid=hotel.getHotelid();
            //System.out.println((this.hotelid));
            if (hotel != null) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Hotel added successfully. Please add room details.");
                roomDetailsContainer.setVisible(true); // Show room details section
                submitHotelButton.setDisable(true); // Disable the hotel submission button
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add hotel. Please try again.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Total rooms must be a valid number.");
        }
    }

    @FXML
    private void handleAddRoom() {
        // Check if the number of added rooms has reached the total
        if (roomsAdded >= totalRoomsToAdd) {
            showAlert(Alert.AlertType.INFORMATION, "Rooms Completed", "All rooms have been added.");
            addRoomsButton.setDisable(true); // Disable the "Add Rooms" button
            return;
        }

        // Create fields for room details
        TextField roomNumberField = new TextField();
        roomNumberField.setPromptText("Room Number");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        ComboBox<String> roomTypeBox = new ComboBox<>();
        roomTypeBox.getItems().addAll("Single", "Double", "Suite");
        roomTypeBox.setPromptText("Room Type");

        ComboBox<String> availabilityBox = new ComboBox<>();
        availabilityBox.getItems().addAll("Available", "Booked");
        availabilityBox.setPromptText("Availability");

        Button saveRoomButton = new Button("Save Room");

        // Define action for "Save Room" button
        saveRoomButton.setOnAction(event -> {
            try {
                // Validate room inputs
                int roomNumber = Integer.parseInt(roomNumberField.getText());
                double price = Double.parseDouble(priceField.getText());
                String roomType = roomTypeBox.getValue();
                String availability = availabilityBox.getValue();

                if (roomType == null || availability == null) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select room type and availability.");
                    return;
                }

                // Add the room to the database using the RoomFactory
                Room room = roomFactory.createRoom(roomNumber, price, 1, roomType, availability, hotelid);
                if (room != null) {
                    roomsAdded++;
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Room added successfully.");

                    // Disable "Save Room" button after adding the room


                    // If all rooms are added, disable "Add Rooms" button
                    if (roomsAdded >= totalRoomsToAdd) {
                        addRoomsButton.setDisable(true);
                        saveRoomButton.setDisable(true);
                        showAlert(Alert.AlertType.INFORMATION, "Rooms Completed", "All rooms have been added.");
                    }

                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to add room. Please try again.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Room number and price must be valid numbers.");
            }
        });

        // Add the room detail fields dynamically to the roomDetailsContainer
        VBox roomDetails = new VBox(10, roomNumberField, priceField, roomTypeBox, availabilityBox, saveRoomButton);
        roomDetailsContainer.getChildren().add(roomDetails);
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

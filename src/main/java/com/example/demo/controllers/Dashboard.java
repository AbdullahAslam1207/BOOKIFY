package com.example.demo.controllers;

import com.example.demo.oop.factories.*;
import com.example.demo.oop.models.Booking;
import com.example.demo.oop.models.Hotel;
import com.example.demo.oop.models.Restaurant;
import com.example.demo.oop.models.RestaurantTable;
import com.example.demo.oop.models.Room;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.util.List;

public class Dashboard {

    @FXML
    private TextField hotelIdField, roomIdField, priceField,tablePriceField;

    @FXML
    private DatePicker bookingDatePicker,bookDatePicker;

    @FXML
    private VBox hotelListContainer, roomListContainer;

    @FXML
    private Button showHotelsButton, confirmBookingButton;

    @FXML
    private VBox restaurantListContainer, tableListContainer;
    @FXML
    private TextField restaurantIdField, tableIdField;
    @FXML
    private Button showRestaurantsButton, confirmTableBookingButton;

    private HotelFactory hotelFactory;
    private RoomFactory roomFactory;
    private BookingFactory bookingFactory;
    private PaymentFactory paymentFactory;
    private RestaurantFactory restaurantFactory;
    private  RestaurantTableFactory restaurantTableFactory;
    private NotificationFactory notificationFactory;
    private int customerid;

    public void initialize() {
        hotelFactory = new HotelFactory();
        roomFactory = new RoomFactory();
        bookingFactory = new BookingFactory();
        paymentFactory = new PaymentFactory();
        restaurantFactory=new RestaurantFactory();
        restaurantTableFactory=new RestaurantTableFactory();
        notificationFactory = new NotificationFactory();
    }

    public void setuserId(int id) {
        this.customerid = id;
    }

    @FXML
    private void handleShowHotels() {
        List<Hotel> hotels = hotelFactory.getAllHotels();
        hotelListContainer.getChildren().clear();

        for (Hotel hotel : hotels) {
            Label hotelLabel = new Label("Hotel ID: " + hotel.getHotelid() + ", Name: " + hotel.getName());
            hotelListContainer.getChildren().add(hotelLabel);
        }
    }

    @FXML
    private void handleShowRestaurants() {
        List<Restaurant> restaurants = restaurantFactory.getAllRestaurants();
        restaurantListContainer.getChildren().clear();
        for (Restaurant restaurant : restaurants) {
            Label label = new Label("Restaurant ID: " + restaurant.getRestaurantId() + ", Name: " + restaurant.getName());
            label.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
            restaurantListContainer.getChildren().add(label);
        }
    }


    @FXML
    private void handleShowRooms() {
        String hotelIdInput = hotelIdField.getText();
        if (hotelIdInput.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "Please enter a Hotel ID.");
            return;
        }

        try {
            int hotelId = Integer.parseInt(hotelIdInput);
            Date bookingDate = Date.valueOf(bookingDatePicker.getValue());

            List<Room> rooms = roomFactory.getAvailableRoomsByHotel(hotelId, bookingDate);
            if (rooms.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Rooms", "No available rooms for the selected date.");
                return;
            }

            roomListContainer.getChildren().clear();
            for (Room room : rooms) {
                Label roomLabel = new Label("Room ID: " + room.getId() + ", Type: " + room.getType() + ", Price: " + room.getPrice());
                roomListContainer.getChildren().add(roomLabel);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input or date selection.");
        }
    }
    @FXML
    private void handleShowTables() {
        String restaurantIdInput = restaurantIdField.getText(); // Use a separate field for Restaurant ID if necessary
        if (restaurantIdInput.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "Please enter a Restaurant ID.");
            return;
        }

        try {
            int restaurantId = Integer.parseInt(restaurantIdInput);
            List<RestaurantTable> tables = restaurantTableFactory.getAvailableTables(restaurantId);

            if (tables.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Tables", "No available tables for the selected restaurant.");
                return;
            }

            restaurantListContainer.getChildren().clear();
            for (RestaurantTable table : tables) {
                Label tableLabel = new Label(
                        "Table ID: " + table.getTableId() +
                                ", Table Number: " + table.getTableNumber() +
                                ", Capacity: " + table.getCapacity() +
                                ", Availability: " + table.getAvailability()
                );
                tableLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                restaurantListContainer.getChildren().add(tableLabel);
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Restaurant ID must be a valid number.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching available tables.");
        }
    }


    @FXML
    private void handleConfirmBooking() {
        try {
            int hotelId = Integer.parseInt(hotelIdField.getText());
            int roomId = Integer.parseInt(roomIdField.getText());
            double price = Double.parseDouble(priceField.getText());
            Date bookingDate = Date.valueOf(bookingDatePicker.getValue());

            // Fetch serviceId from HotelFactory
            int serviceId = hotelFactory.getServiceIdByHotelId(hotelId);

            if (serviceId == -1) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid Hotel ID.");
                return;
            }

            // Fetch room details
            Room room = roomFactory.getRoomById(roomId, hotelId);
            if (room != null && "available".equals(room.getAvailability())) {
                // Create the booking
                Booking booking = bookingFactory.createBooking(customerid, serviceId, bookingDate, price, roomId, hotelId);
                if (booking != null) {
                    roomFactory.updateRoomAvailability(roomId, hotelId, "booked");

                    // Insert payment record into Payments table
                    boolean paymentCreated = paymentFactory.createPayment(booking.getId(),customerid, price,"cash" ,"pending");
                    if (paymentCreated) {
                        // Send Notification
                        notificationFactory.sendNotification(customerid, "Booking confirmed! Please proceed to payment.", "sms");
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Booking confirmed! Payment is pending.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Payment Error", "Failed to create payment record.");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to confirm booking.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "No Availability", "Room is not available on the selected date.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input.");
        }
    }




    @FXML
    private void handleConfirmTableBooking() {
        try {
            int restaurantId = Integer.parseInt(restaurantIdField.getText()); // Replace with a separate field for Restaurant ID if needed
            int tableId = Integer.parseInt(tableIdField.getText()); // Replace with a separate field for Table ID if needed
            double price = Double.parseDouble(tablePriceField.getText());
            Date bookingDate = Date.valueOf(bookDatePicker.getValue());

            // Fetch serviceId from RestaurantFactory
            int serviceId = restaurantFactory.getServiceIdByRestaurantId(restaurantId);

            if (serviceId == -1) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid Restaurant ID.");
                return;
            }
            System.out.println("aaaaaa");
            // Fetch table details
            RestaurantTable table = restaurantTableFactory.getTableById(tableId, restaurantId);
            if (table != null && "available".equals(table.getAvailability())) {
                // Create the booking
                Booking booking = bookingFactory.createTableBooking(customerid, serviceId, bookingDate, price, tableId, restaurantId);
                if (booking != null) {
                    // Update table availability
                    restaurantTableFactory.updateTableAvailability(tableId, restaurantId, "booked");

                    // Insert payment record into Payments table
                    boolean paymentCreated = paymentFactory.createPayment(booking.getId(), customerid, price, "cash", "pending");
                    if (paymentCreated) {
                        // Send Notification
                        notificationFactory.sendNotification(customerid, "Table booking confirmed! Please proceed to payment.", "sms");
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Table booking confirmed! Payment is pending.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Payment Error", "Failed to create payment record.");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to confirm table booking.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "No Availability", "Table is not available on the selected date.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for detailed error information
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

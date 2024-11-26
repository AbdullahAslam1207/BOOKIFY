package com.example.demo.controllers;

import com.example.demo.oop.models.Booking;
import com.example.demo.database.queries.BookingQueries;
import com.example.demo.database.queries.RoomQueries;
import com.example.demo.database.queries.RestaurantQueries;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.util.List;

public class ModifyBookingsController {

    @FXML
    private TableView<Booking> roomBookingsTable, tableBookingsTable;
    @FXML
    private TableColumn<Booking, Integer> roomBookingIdColumn, roomHotelColumn, roomNumberColumn;
    @FXML
    private TableColumn<Booking, Date> roomDateColumn;
    @FXML
    private TableColumn<Booking, String> roomStatusColumn;
    @FXML
    private TableColumn<Booking, Integer> tableBookingIdColumn, tableRestaurantColumn, tableNumberColumn;
    @FXML
    private TableColumn<Booking, Date> tableDateColumn;
    @FXML
    private TableColumn<Booking, String> tableStatusColumn;
    @FXML
    private TextField bookingIdField;
    @FXML
    private DatePicker newDatePicker;

    private BookingQueries bookingQueries;
    private RoomQueries roomQueries;
    private RestaurantQueries restaurantQueries;
    private int userId;

    public void initialize() {
        bookingQueries = new BookingQueries();
        roomQueries = new RoomQueries();
        restaurantQueries = new RestaurantQueries();

        // Setup columns
        roomBookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        roomHotelColumn.setCellValueFactory(new PropertyValueFactory<>("ServiceId"));
        //roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        roomStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableBookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableRestaurantColumn.setCellValueFactory(new PropertyValueFactory<>("ServiceId"));
        //tableNumberColumn.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        tableDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        tableStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


    }
    public void setUserId(int userId) {
        this.userId = userId;
        System.out.println("modigyyyyyyyyyyyy"+ this.userId);
        loadBookings();
    }

    private void loadBookings() {
        List<Booking> roomBookings = bookingQueries.getCompletedRoomBookings(userId);
        List<Booking> tableBookings = bookingQueries.getCompletedTableBookings(userId);
        roomBookingsTable.getItems().setAll(roomBookings);
        tableBookingsTable.getItems().setAll(tableBookings);
    }

    @FXML
    private void handleModifyDate() {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            Date newDate = Date.valueOf(newDatePicker.getValue());

            if (bookingQueries.updateBookingDate(bookingId, newDate)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Booking date updated successfully.");
                loadBookings();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update booking date.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input.");
        }
    }

    @FXML
    private void handleDeleteBooking() {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText());

            if (bookingQueries.deleteBooking(bookingId)) {
                roomQueries.updateRoomAvailabilityAfterDeletion();
                restaurantQueries.updateTableAvailabilityAfterDeletion();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Booking deleted successfully.");
                loadBookings();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete booking.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input.");
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

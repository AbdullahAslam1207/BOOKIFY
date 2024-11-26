package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ViewBookingAdminController {

    @FXML
    private Label pageTitle; // Title Label

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, String> serviceColumn;

    @FXML
    private TableColumn<Booking, String> customerColumn;

    @FXML
    private TableColumn<Booking, String> dateColumn;

    @FXML
    private TableColumn<Booking, String> statusColumn;

    public void initialize() {
        // Set the title text
        pageTitle.setText("View Confirmed or Canceled Bookings");

        // Initialize table columns
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Sample data including various statuses (Confirmed, Pending, Canceled)
        ObservableList<Booking> allBookings = FXCollections.observableArrayList(
                new Booking("Room Booking", "John Doe", "2024-11-20", "Confirmed"),
                new Booking("Table Reservation", "Jane Smith", "2024-11-22", "Pending"),  // This will be filtered out
                new Booking("Hall Booking", "Michael Johnson", "2024-12-01", "Canceled"),
                new Booking("Spa Appointment", "Emily Davis", "2024-11-25", "Confirmed"),
                new Booking("Car Rental", "Robert Brown", "2024-11-28", "Canceled")
        );

        // Filter to show only "Confirmed" or "Canceled" statuses
        ObservableList<Booking> filteredBookings = allBookings.filtered(
                booking -> booking.getStatus().equalsIgnoreCase("Confirmed") || booking.getStatus().equalsIgnoreCase("Canceled")
        );

        // Add the filtered bookings to the table
        bookingsTable.setItems(filteredBookings);
    }

    // Booking class for TableView data
    public static class Booking {
        private String service;
        private String customer;
        private String date;
        private String status;

        public Booking(String service, String customer, String date, String status) {
            this.service = service;
            this.customer = customer;
            this.date = date;
            this.status = status;
        }

        public String getService() { return service; }
        public String getCustomer() { return customer; }
        public String getDate() { return date; }
        public String getStatus() { return status; }
    }
}

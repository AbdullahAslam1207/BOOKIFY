package com.example.demo.controllers;

import com.example.demo.oop.models.Booking;
import com.example.demo.database.queries.BookingQueries;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ViewBookingsController {

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, Integer> idColumn;

    @FXML
    private TableColumn<Booking, Integer> useridColumn;

    @FXML
    private TableColumn<Booking, Integer> serviceidColumn;

    @FXML
    private TableColumn<Booking, String> dateColumn;

    @FXML
    private TableColumn<Booking, String> statusColumn;

    @FXML
    private TableColumn<Booking, Double> priceColumn;

    private BookingQueries bookingQueries;

    private int userId;

    public void initialize() {
        bookingQueries = new BookingQueries();

        // Bind columns to Booking properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        useridColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        serviceidColumn.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void setUserId(int userId) {
        this.userId = userId;
        loadCompletedBookings();
    }

    private void loadCompletedBookings() {
        List<Booking> completedBookings = bookingQueries.fetchCompletedBookingsByUserId(userId);
        bookingsTable.getItems().clear();
        bookingsTable.getItems().addAll(completedBookings);
    }
}

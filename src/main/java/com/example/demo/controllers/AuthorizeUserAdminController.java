package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuthorizeUserAdminController {

    @FXML
    private Label pageTitle; // Title Label

    @FXML
    private TableView<AuthorizationRequest> requestsTable;

    @FXML
    private TableColumn<AuthorizationRequest, String> nameColumn;

    @FXML
    private TableColumn<AuthorizationRequest, String> roleColumn;

    @FXML
    private TableColumn<AuthorizationRequest, String> requestDateColumn;

    @FXML
    private TableColumn<AuthorizationRequest, String> statusColumn;

    public void initialize() {
        // Set the title text
        pageTitle.setText("Authorization Requests");

        // Initialize table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        requestDateColumn.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Dummy data for testing authorization requests
        ObservableList<AuthorizationRequest> allRequests = FXCollections.observableArrayList(
                new AuthorizationRequest("Alice Johnson", "Service Owner", "2024-11-14", "Pending"),
                new AuthorizationRequest("John Doe", "Admin", "2024-11-15", "Approved"),
                new AuthorizationRequest("Jane Smith", "Carer", "2024-11-13", "Pending")
        );

        // Filter for "Pending" status requests
        ObservableList<AuthorizationRequest> pendingRequests = allRequests.filtered(
                request -> request.getStatus().equalsIgnoreCase("Pending")
        );

        // Show "Pending" requests if any, otherwise display "No Pending Requests"
        if (!pendingRequests.isEmpty()) {
            requestsTable.setItems(pendingRequests);
        } else {
            pageTitle.setText("No Pending Requests");
        }
    }

    // AuthorizationRequest class for TableView data
    public static class AuthorizationRequest {
        private String name;
        private String role;
        private String requestDate;
        private String status;

        public AuthorizationRequest(String name, String role, String requestDate, String status) {
            this.name = name;
            this.role = role;
            this.requestDate = requestDate;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        public String getRequestDate() {
            return requestDate;
        }

        public String getStatus() {
            return status;
        }
    }
}

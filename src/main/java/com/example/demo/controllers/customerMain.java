package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class customerMain {
    @FXML
    private VBox sidebar;

    @FXML
    private Button collapseButton;

    @FXML
    private AnchorPane mainContent;

    @FXML
    private ImageView welcomeImage;

    private boolean isSidebarExpanded = true;
    private int customerid;
    private String customername;

    public void initialize() {
        // Load the welcome image
        welcomeImage.setImage(new Image(getClass().getResourceAsStream("/icons/bookify_logo-removebg-preview.png")));
    }

    public void setCustomerDetails(String name, int id) {
        this.customerid= id;
        this.customername=name;

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
    private void loadBookingPane() {
        loadPane("Dashboard.fxml");
    }

    @FXML
    private void loadBookingHistoryPane() {
        loadPane("BookingHistory.fxml");
    }
    @FXML
    private void loadLogoutPane() {
        loadPane("TargetedOffers.fxml");
    }


    @FXML
    private void loadPaymentPane() {
        loadPane("Payment.fxml");
    }

    private void loadPane(String fxmlFile) {
        try {
            String path = "/com/example/demo/" + fxmlFile;
            System.out.println("Loading FXML file from: " + path);
            //String path="ServiceOwnerDashboard.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Node node = loader.load();
            if (fxmlFile=="Dashboard.fxml"){
                // Get the controller instance
                Dashboard addservieController = loader.getController();

                // Pass the service provider ID to the controller
                addservieController.setuserId(customerid);
            }
            else if (fxmlFile=="Payment.fxml"){
                PaymentController addservieController = loader.getController();

                // Pass the service provider ID to the controller
                addservieController.setuserId(customerid);

            }
            mainContent.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleNotifications() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/Notifications.fxml"));
            Node node = loader.load();

            // Get the NotificationController and pass the user ID
            NotificationController notificationController = loader.getController();
            notificationController.setUserId(customerid);

            // Replace the current view with the notifications view
            mainContent.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadMembershipPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/Membership.fxml"));
            Node node = loader.load();

            // Get the NotificationController and pass the user ID
            MembershipController notificationController = loader.getController();
            notificationController.setUserId(customerid);

            // Replace the current view with the notifications view
            mainContent.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleViewBookings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/ViewBookings.fxml"));
            Node node = loader.load();

            // Pass userId to ViewBookingsController
            ViewBookingsController controller = loader.getController();
            controller.setUserId(customerid);

            mainContent.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCustomerSupport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/CustomerSupport.fxml"));
            Node node = loader.load();

            // Pass userId to ViewBookingsController
            CustomerSupportController controller = loader.getController();
            controller.setUserId(customerid);

            mainContent.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleModify() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/ModifyBooking.fxml"));
            Node node = loader.load();

            // Pass userId to ViewBookingsController
            ModifyBookingsController controller = loader.getController();
            controller.setUserId(customerid);

            mainContent.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loadMyAccountPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/MyAccount.fxml"));
            Node node = loader.load();

            // Pass userId to ViewBookingsController
            MyAccount controller = loader.getController();
            controller.setUserId(customerid);

            mainContent.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





}

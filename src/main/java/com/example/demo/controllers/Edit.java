package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Edit {

    @FXML
    private AnchorPane mainContent;
    @FXML
    private void loadMyAccountPane() {
        loadPane("MyAccount.fxml");
    }

    private void loadPane(String fxmlFile) {
        try {
            String path = "/com/example/demo/" + fxmlFile;
            System.out.println("Loading FXML file from: " + path);
            //String path="ServiceOwnerDashboard.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Node node = loader.load();
            mainContent.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

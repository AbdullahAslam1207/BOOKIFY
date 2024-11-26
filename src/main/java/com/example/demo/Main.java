package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {


        try {
            Parent root = FXMLLoader.load(getClass().getResource("MainSelectionPage.fxml"));
            primaryStage.setTitle("Service Owner Dashboard");
            primaryStage.setScene(new Scene(root, 800, 600)); // Set preferred window size
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

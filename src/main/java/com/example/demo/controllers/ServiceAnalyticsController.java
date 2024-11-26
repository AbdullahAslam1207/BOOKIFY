package com.example.demo.controllers;

import com.example.demo.oop.factories.ServiceAnalyticsFactory;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.util.Map;

public class ServiceAnalyticsController {
    @FXML
    private BarChart<String, Integer> analyticsChart;

    private final ServiceAnalyticsFactory analyticsFactory;

    public ServiceAnalyticsController() {
        this.analyticsFactory = new ServiceAnalyticsFactory();
    }

    public void setOwnerId(int ownerId) {
        Map<String, Integer> analyticsData = analyticsFactory.getAnalyticsData(ownerId);
        populateChart(analyticsData);
    }

    private void populateChart(Map<String, Integer> analyticsData) {
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        analyticsData.forEach((serviceName, bookingCount) ->
                series.getData().add(new XYChart.Data<>(serviceName, bookingCount))
        );
        // Change the text color of the service name labels (X-axis ticks)
        analyticsChart.getXAxis().lookupAll(".axis-tick-mark-label").forEach(node ->
                node.setStyle("-fx-fill: white;") // Set text color to white
        );
        analyticsChart.getData().add(series);
    }
}

package com.example.demo.controllers;

import com.example.demo.oop.factories.AnalyticsFactory;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.util.Map;

public class ViewBookingAdminController {

    @FXML
    private BarChart<String, Number> bookingsBarChart;

    @FXML
    private PieChart revenuePieChart;

    @FXML
    private LineChart<String, Number> userActivityLineChart;

    private AnalyticsFactory analyticsFactory;

    public void initialize() {
        analyticsFactory = new AnalyticsFactory();

        // Load analytics
        loadCompletedBookingsChart();
        loadRevenuePieChart();
        loadUserActivityChart();
    }

    private void loadCompletedBookingsChart() {
        Map<String, Integer> completedBookings = analyticsFactory.getCompletedBookingsAnalytics();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Completed Bookings");

        for (Map.Entry<String, Integer> entry : completedBookings.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        bookingsBarChart.getData().add(series);
    }

    private void loadRevenuePieChart() {
        Map<String, Double> revenueData = analyticsFactory.getRevenueAnalytics();

        for (Map.Entry<String, Double> entry : revenueData.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            revenuePieChart.getData().add(slice);
        }
    }

    private void loadUserActivityChart() {
        Map<String, Integer> userActivity = analyticsFactory.getUserSignupsAnalytics();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("User Signups");

        for (Map.Entry<String, Integer> entry : userActivity.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        userActivityLineChart.getData().add(series);
    }
}

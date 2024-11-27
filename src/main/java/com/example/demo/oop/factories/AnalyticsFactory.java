package com.example.demo.oop.factories;

import com.example.demo.database.queries.AnalyticsQueries;

import java.util.Map;

public class AnalyticsFactory {

    private final AnalyticsQueries analyticsQueries;

    public AnalyticsFactory() {
        this.analyticsQueries = new AnalyticsQueries();
    }

    public Map<String, Integer> getCompletedBookingsAnalytics() {
        return analyticsQueries.fetchCompletedBookingsByService();
    }

    public Map<String, Double> getRevenueAnalytics() {
        return analyticsQueries.fetchRevenueByService();
    }

    public Map<String, Integer> getUserSignupsAnalytics() {
        return analyticsQueries.fetchUserSignupsOverTime();
    }
}

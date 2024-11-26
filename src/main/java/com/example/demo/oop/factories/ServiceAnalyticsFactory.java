package com.example.demo.oop.factories;

import com.example.demo.database.queries.ServiceQueries;
import java.util.Map;

public class ServiceAnalyticsFactory {
    private final ServiceQueries serviceQueries;

    public ServiceAnalyticsFactory() {
        this.serviceQueries = new ServiceQueries();
    }

    public Map<String, Integer> getAnalyticsData(int ownerId) {
        return serviceQueries.getServiceAnalytics(ownerId);
    }
}

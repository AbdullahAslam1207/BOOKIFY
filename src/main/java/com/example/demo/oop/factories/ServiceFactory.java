package com.example.demo.oop.factories;

import com.example.demo.oop.models.Service;
import com.example.demo.database.queries.ServiceQueries;

public class ServiceFactory {
    private final ServiceQueries serviceQueries;

    public ServiceFactory() {
        this.serviceQueries = new ServiceQueries();
    }

    // Method to create a new service
    public int createService(String name, String serviceType, int serviceProviderId) {
        return serviceQueries.insertService(name, serviceType, serviceProviderId);
    }
}

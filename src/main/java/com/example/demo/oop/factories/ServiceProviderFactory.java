package com.example.demo.oop.factories;

import com.example.demo.oop.models.ServiceProvider;
import com.example.demo.database.queries.ServiceProviderQueries;

public class ServiceProviderFactory {
    private final ServiceProviderQueries serviceProviderQueries;

    public ServiceProviderFactory() {
        this.serviceProviderQueries = new ServiceProviderQueries();
    }

    // Method to create a new ServiceProvider
    public ServiceProvider createServiceProvider(String name, String email, String phoneNumber) {
        int serviceProviderId = serviceProviderQueries.insertServiceProvider(name, email, phoneNumber);
        if (serviceProviderId > 0) {
            return new ServiceProvider(serviceProviderId, name, email, phoneNumber);
        }
        return null;
    }

    // Method to fetch an existing ServiceProvider
    public ServiceProvider getServiceProviderById(int id) {
        return serviceProviderQueries.getServiceProviderById(id);
    }
}

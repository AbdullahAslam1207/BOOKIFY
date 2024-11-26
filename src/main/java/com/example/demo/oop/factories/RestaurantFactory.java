package com.example.demo.oop.factories;

import com.example.demo.oop.models.Restaurant;
import com.example.demo.database.queries.RestaurantQueries;

import java.util.List;

public class RestaurantFactory extends ServiceFactory {
    private final RestaurantQueries restaurantQueries;

    public RestaurantFactory() {
        this.restaurantQueries = new RestaurantQueries();
    }

    // Method to create a new restaurant
    public Restaurant createRestaurant(String name, int totalTables, int serviceProviderId) {
        // Call the superclass method to create a service entry
        int serviceId = super.createService(name, "Restaurant", serviceProviderId);

        if (serviceId > 0) {
            // Insert the restaurant details and get the generated restaurant ID
            int restaurantId = restaurantQueries.insertRestaurantAndGetId(name,serviceId, totalTables);
            if (restaurantId > 0) {
                // Return the Restaurant object with all details
                return new Restaurant(restaurantId, name, "Restaurant", serviceProviderId, totalTables);
            }
        }
        return null; // Return null if there was an issue during creation
    }
    public List<Restaurant> getAllRestaurants() {
        return restaurantQueries.getAllRestaurants();
    }
    public int getServiceIdByRestaurantId(int resId) {
        return restaurantQueries.fetchServiceIdByRestaurantId(resId);
    }


}

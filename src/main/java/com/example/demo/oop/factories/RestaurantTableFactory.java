package com.example.demo.oop.factories;

import com.example.demo.oop.models.RestaurantTable;
import com.example.demo.database.queries.RestaurantQueries;

import java.util.List;

public class RestaurantTableFactory {
    private final RestaurantQueries restaurantQueries;

    public RestaurantTableFactory() {
        this.restaurantQueries = new RestaurantQueries();
    }

    public RestaurantTable createTable(int restaurantId, int tableNumber, int capacity, String availability) {
        int tableId = restaurantQueries.insertTable(restaurantId, tableNumber, capacity, availability);
        if (tableId > 0) {
            return new RestaurantTable(tableId, restaurantId, tableNumber, capacity, availability);
        }
        return null;
    }
    public List<RestaurantTable> getAvailableTables(int restaurantId) {
        return restaurantQueries.getAvailableTables(restaurantId);
    }
    public RestaurantTable getTableById(int tableId, int restaurantId) {
        return restaurantQueries.fetchTableByIdAndRestaurantId(tableId, restaurantId);
    }
    public boolean updateTableAvailability(int tableId, int restaurantId, String availability) {
        return restaurantQueries.updateTableAvailability(tableId, restaurantId, availability);
    }



}

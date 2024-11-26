package com.example.demo.oop.models;

import java.util.ArrayList;
import java.util.List;

public class Restaurant extends Service {
    private int restaurantId;
    private int totalTables;
    private List<RestaurantTable> tables; // List of associated tables

    public Restaurant(int id, String name, String type, int ownerId, int totalTables) {
        super(id, name);
        this.restaurantId = id;
        this.totalTables = totalTables;
        this.tables = new ArrayList<>(); // Initialize the list
    }
    public Restaurant(int restaurantId, int id, String name, int totalTables){
        super(id,name);
        this.restaurantId=restaurantId;
        this.totalTables=totalTables;
        this.tables=new ArrayList<>();
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    @Override
    public String getServiceType() {
        return "restaurant";
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getTotalTables() {
        return totalTables;
    }

    public void setTotalTables(int totalTables) {
        this.totalTables = totalTables;
    }

    public List<RestaurantTable> getTables() {
        return tables;
    }

    public void setTables(List<RestaurantTable> tables) {
        this.tables = tables;
    }

    // Add a table to the restaurant
    public void addTable(RestaurantTable table) {
        if (tables.size() < totalTables) {
            tables.add(table);
        } else {
            throw new IllegalStateException("Cannot add more tables than the total allocated for the restaurant.");
        }
    }
}

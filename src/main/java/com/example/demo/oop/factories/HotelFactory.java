package com.example.demo.oop.factories;

import com.example.demo.oop.models.Hotel;
import com.example.demo.database.queries.HotelQueries;

import java.util.List;

public class HotelFactory extends ServiceFactory {
    private final HotelQueries hotelQueries;

    public HotelFactory() {
        this.hotelQueries = new HotelQueries();
    }

    // Method to create a new hotel
    public Hotel createHotel(String name, String location, int totalRooms, int serviceProviderId) {
        int serviceId = super.createService(name,  "Hotel", serviceProviderId);

        System.out.println("i am here ");
        if (serviceId > 0) {
            int hotelid=hotelQueries.insertHotelAndGetId(serviceId,name,location, totalRooms);
            //int hotelid= hotelQueries.getLastInsertedHotelId();
            System.out.println((hotelid));
            return new Hotel(hotelid,serviceId, name, location, totalRooms);
        }
        return null;
    }
    public List<Hotel> getAllHotels() {
        return hotelQueries.fetchAllHotels();
    }
    public int getServiceIdByHotelId(int hotelId) {
        return hotelQueries.fetchServiceIdByHotelId(hotelId);
    }




}

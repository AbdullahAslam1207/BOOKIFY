package com.example.demo.oop.factories;

import com.example.demo.database.queries.DiscountCodeQueries;
import com.example.demo.oop.models.DiscountCode;

import java.sql.Date;

public class DiscountCodeFactory {
    private final DiscountCodeQueries discountCodeQueries;

    public DiscountCodeFactory() {
        this.discountCodeQueries = new DiscountCodeQueries();
    }

    public boolean generateDiscountCode(int userId, String code, int discountPercentage, Date validity) {
        return discountCodeQueries.insertDiscountCode(userId, code, discountPercentage, validity);
    }

    public DiscountCode validateDiscountCode(int userId, String code) {
        return discountCodeQueries.getDiscountCodeByUserAndCode(userId, code);
    }

    public boolean markCodeAsUsed(int id) {
        return discountCodeQueries.markCodeAsUsed(id);
    }
}

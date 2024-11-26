package com.example.demo.oop.factories;

import com.example.demo.database.queries.MembershipQueries;
import com.example.demo.oop.models.Membership;

import java.util.Calendar;
import java.util.Date;

public class MembershipFactory {
    private final MembershipQueries membershipQueries;

    public MembershipFactory() {
        this.membershipQueries = new MembershipQueries();
    }

    private PaymentFactory paymentFactory = new PaymentFactory();

    public boolean purchaseMembership(int userId, String membershipType, double price, String paymentMethod) {
        if (membershipQueries.checkExistingMembership(userId)) {
            System.out.println("User already has an active membership.");
            return false;
        }
        System.out.println("11111");
        Membership membership = new Membership();
        membership.setUserId(userId);
        membership.setType(membershipType);

        // Set discount percentage based on type
        switch (membershipType.toLowerCase()) {
            case "simple":
                membership.setDiscountPercentage(5);
                break;
            case "deluxe":
                membership.setDiscountPercentage(10);
                break;
            case "exclusive":
                membership.setDiscountPercentage(20);
                break;
            default:
                throw new IllegalArgumentException("Invalid membership type");
        }
        System.out.println("222");
        // Set membership validity for 1 year
        Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR, 1);
        Date endDate = calendar.getTime();

        membership.setStartDate(new java.sql.Date(startDate.getTime()));
        membership.setEndDate(new java.sql.Date(endDate.getTime()));


        System.out.println("333");

        // Add membership and process payment
        if (membershipQueries.addMembership(membership)) {
            System.out.println("444");
            membership=membershipQueries.getMembership(userId);
            System.out.println(membership.getId());
            boolean paymentId = paymentFactory.creatememPayment(membership.getId(),userId,price,"cash","pending");
            System.out.println("5555");
            if (paymentId){
                return true;
            }
            return false;
        }
        return false;
    }
    public boolean activateMembership(int membershipId) {
        return membershipQueries.activateMembership(membershipId);
    }



    public Membership getActiveMembership(int userId) {
        return membershipQueries.getMembershipByUserId(userId);
    }
}

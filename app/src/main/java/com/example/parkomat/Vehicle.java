package com.example.parkomat;

public class Vehicle {
    private String registrationNumber;
    private int hour;
    private int minute;
    private String date;
    private Double payment;

    public Vehicle(String registrationNumber, int hour, int minute, Double payment, String date) {
        this.registrationNumber = registrationNumber;
        this.hour = hour;
        this.minute = minute;
        this.payment = payment;
        this.date = date;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public Double getPayment() {
        return payment;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getDate() {
        return date;
    }



}

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

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setPayment(Double cena) {
        this.payment = cena;
    }

    public Double getPayment() {
        return payment;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}

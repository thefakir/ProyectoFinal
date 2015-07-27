package com.example.fidelbarcaya.easyparking.model;

/**
 * Created by fidel.barcaya on 4/2/2015.
 */
public class Parking {


    private String parkingName;
    private String openingTime;
    private String closingTime;
    private double rate;
    private int carSpacesTotal;
    private int motorBikeSpacesTotal;
    private int carSpacesAvailable ;
    private int motorBikeSpacesAvailable ;
    private Localization Local ;


    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getCarSpacesTotal() {
        return carSpacesTotal;
    }

    public void setCarSpacesTotal(int carSpacesTotal) {
        this.carSpacesTotal = carSpacesTotal;
    }

    public int getMotorBikeSpacesTotal() {
        return motorBikeSpacesTotal;
    }

    public void setMotorBikeSpacesTotal(int motorBikeSpacesTotal) {
        this.motorBikeSpacesTotal = motorBikeSpacesTotal;
    }

    public int getCarSpacesAvailable() {
        return carSpacesAvailable;
    }

    public void setCarSpacesAvailable(int carSpacesAvailable) {
        this.carSpacesAvailable = carSpacesAvailable;
    }

    public int getMotorBikeSpacesAvailable() {
        return motorBikeSpacesAvailable;
    }

    public void setMotorBikeSpacesAvailable(int motorBikeSpacesAvailable) {
        this.motorBikeSpacesAvailable = motorBikeSpacesAvailable;
    }

    public Localization getLocal() {
        return Local;
    }

    public void setLocal(Localization local) {
        Local = local;
    }
}

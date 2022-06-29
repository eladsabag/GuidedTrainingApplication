package com.example.guidedtrainingapplication.objects;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TrainingRecord {
    private String mode;
    private double latitude, longitude;
    private String date;

    public TrainingRecord() { }

    public TrainingRecord(String mode, double latitude, double longitude) {
        this.mode = mode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

    public String getMode() {
        return mode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDate() {
        return date;
    }

    public TrainingRecord setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public TrainingRecord setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public TrainingRecord setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public TrainingRecord setDate(String date) {
        this.date = date;
        return this;
    }
}

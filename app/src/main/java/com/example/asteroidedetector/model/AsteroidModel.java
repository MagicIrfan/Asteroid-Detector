package com.example.asteroidedetector.model;

public class AsteroidModel {

    private String name;
    private double magnitude;
    private double distance;

    public AsteroidModel(String name, double magnitude, double distance) {
        this.name = name;
        this.magnitude = magnitude;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

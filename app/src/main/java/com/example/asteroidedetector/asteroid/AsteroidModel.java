package com.example.asteroidedetector.asteroid;

public class AsteroidModel {

    private String name;
    private double magnitude;
    private int distance;

    public AsteroidModel(String name, double magnitude, int distance) {
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}

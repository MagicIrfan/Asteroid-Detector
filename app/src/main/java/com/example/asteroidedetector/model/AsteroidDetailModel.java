package com.example.asteroidedetector.model;

public class AsteroidDetailModel {
    private final String id;
    private String name;
    private double magnitude;
    private int distance;
    private int orbitalPeriod;

    public AsteroidDetailModel(String id, String name, double magnitude, int distance, int orbitalPeriod) {
        this.id = id;
        this.name = name;
        this.magnitude = magnitude;
        this.distance = distance;
        this.orbitalPeriod = orbitalPeriod;
    }

    public String getId() {
        return id;
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

    public int getOrbitalPeriod() {
        return orbitalPeriod;
    }
}

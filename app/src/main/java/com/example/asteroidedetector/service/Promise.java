package com.example.asteroidedetector.service;

public class Promise<T> {
    protected Callback<T> then;
    protected Callback<String> error;

    public Promise<T> then(Callback<T> then){
        this.then = then;
        return this;
    }

    public Promise<T> error(Callback<String> error){
        this.error = error;
        return this;
    }
}

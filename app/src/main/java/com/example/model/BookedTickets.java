package com.example.model;

import java.util.ArrayList;

public class BookedTickets {
    private long id;
    private String trainName;
    private ArrayList<String> seats;

    public BookedTickets(int id, String trainName, ArrayList<String> seats)
    {
        this.id = id;
        this.trainName = trainName;
        this.seats = seats;
    }

    public String getTrainName() {
        return trainName;
    }

    public ArrayList<String> getSeats() {
        return seats;
    }

    public long getId() {
        return id;
    }
}

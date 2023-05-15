package com.example.assignment2.Models;

public class Score
{
    private int score;
    //private Location location; //there appears to be an issue with json representation of a location type object, so using doubles of latitude and longitude instead.
    private double latitude;
    private double longitude;

    public Score() {}

    public int getScore() {
        return score;
    }

    public Score setScore(int score)
    {
        this.score = score;
        return this;
    }


    public double getLatitude()
    {
        return latitude;
    }

    public Score setLatitude(double latitude)
    {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public Score setLongitude(double longitude)
    {
        this.longitude = longitude;
        return this;
    }
}

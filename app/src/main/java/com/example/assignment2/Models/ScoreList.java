package com.example.assignment2.Models;

import java.util.ArrayList;

public class ScoreList
{

    private ArrayList<Score> scores = new ArrayList<>();

    public ScoreList() {    } //empty constuctors generally for firebase DB

    public ArrayList<Score> getScores()
    {
        return scores;
    }

}

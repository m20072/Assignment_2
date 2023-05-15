package com.example.assignment2.Logic;

import com.example.assignment2.Models.Obstacle;
import com.example.assignment2.Models.Player;
import com.example.assignment2.Models.Score;
import com.example.assignment2.Models.ScoreList;
import com.example.assignment2.R;
import com.example.assignment2.Utilities.MySP;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DataManager
{
    public static final int COLS = 5;
    public static final int ROWS = 5;
    public static final int TOP = 10;
    public static final int NUM_OF_OBSTACLES = COLS * ROWS;
    public static final int NUM_OF_PLAYERS = COLS;

    public static ArrayList<Player> getPlayers()
    {
        ArrayList<Player> Players = new ArrayList<>();
        for (int i = 0; i < NUM_OF_PLAYERS; i++)
        {
            Player q = new Player().setVisible(false);
            Players.add(q);
        }

        return Players;
    }

    public static ArrayList<Obstacle> getObstacles()
    {
        ArrayList<Obstacle> Obstacles = new ArrayList<>();
        for (int i = 0; i < NUM_OF_OBSTACLES; i++)
        {
            Obstacle q = new Obstacle().setVisible(false).setImageResource(R.drawable.ic_bitcoin); //setting all as image in index 0 in images array which is bitcoin NOT heart
            Obstacles.add(q);
        }

        return Obstacles;
    }

    public static ScoreList getScoreList()
    {
        ScoreList scoreList = new ScoreList();
        String topScoresJsonArrayListSP = MySP.getInstance().getString("TOP_10_SCORES","null"); //try null without ""

        if(topScoresJsonArrayListSP == "null")//if there is no saved top scores array list as json, then generate 10 scores of 0
        {
            for(int i = 0;i<TOP;i++)
            {
                Score s = new Score().setScore(0).setLatitude(0).setLongitude(0);
                scoreList.getScores().add(s);
            }
        }
        else//there is a saved top scores list object as json in shared preferences, so turn from json into ScoreList type class and put into scoreList
        {
            scoreList  = new Gson().fromJson(topScoresJsonArrayListSP, ScoreList.class);
        }
        return scoreList;
    }


    public static void saveToSP(ScoreList scoreList)
    {
        String scoreListJson = new Gson().toJson(scoreList);
        MySP.getInstance().putString("TOP_10_SCORES", scoreListJson);

    }

}

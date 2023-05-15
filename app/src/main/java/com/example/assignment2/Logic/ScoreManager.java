package com.example.assignment2.Logic;

import android.location.Location;

import com.example.assignment2.Models.Score;
import com.example.assignment2.Models.ScoreList;
public class ScoreManager
{
    private int score;
    public ScoreManager(int score)
    {
        this.score = score;
    }


    public void updateTopList(Location location)
    {
        ScoreList topScores = DataManager.getScoreList();
        Score myScore = new Score().setScore(this.score).setLatitude(location.getLatitude()).setLongitude(location.getLongitude()); //will also have .setLocation later
        int size = topScores.getScores().size();
        boolean changed = false;

        if(topScores.getScores().get(size-1).getScore() <= myScore.getScore()) //size-1 is lowest score while 0 is highest score (if my score bigger than lowest score, need change)
        {
            if(topScores.getScores().get(0).getScore() <= myScore.getScore()) //if my score higher than the highest or equal, add it.
            {
                topScores.getScores().remove(size-1);
                topScores.getScores().add(0, myScore);
                changed = true;
            }
            else
            {
                for (int i = size - 2; i >= 0; i--) //from lowest scores to highest scores
                {
                    if (topScores.getScores().get(i).getScore() > myScore.getScore())
                    {
                        topScores.getScores().add(i + 1, myScore); //shifts the element that was in that index previously to an index above (smaller value)
                        topScores.getScores().remove(topScores.getScores().size() - 1); //removes the lowest score, so now we add a new score in correct index in the next instruction
                                                                                            //not using shortcut for size cause due to adding an element the original size has changed.
                        changed = true;
                        break;
                    }
                }
            }
            if(changed)//if a new score was added to the arraylist of scores in the scoreList from the datamanager, we upload the updated ScoreList(topScores) to the shared-preferences
                DataManager.saveToSP(topScores);
        }
    }
}

package com.example.assignment2.Logic;


import static com.example.assignment2.Logic.DataManager.COLS;

import com.example.assignment2.Models.Obstacle;
import com.example.assignment2.Models.Player;
import com.example.assignment2.R;

import java.util.ArrayList;
import java.util.Random;

public class GameManager
{
    private final int life;
    private int wrong;
    private int score;
    private int visiblePlayerIndex;
    private static int random;
    private final static Random ran = new Random();
    private final ArrayList<Obstacle> obstacles;
    private final ArrayList<Player> players;


    public GameManager(int life)
    {
        this.life = life;
        this.wrong = 0;
        this.score = 0;
        this.visiblePlayerIndex = 1;
        random = 0;
        obstacles = DataManager.getObstacles();
        players = DataManager.getPlayers();
        getCurrentVisiblePlayer().setVisible(true);
    }


    public boolean spawnObstacle()
    {
        // 50% chance obstacle spawn
        return ran.nextInt(2) == 1;
    }

    public int randomObstacle()
    {
        random = ran.nextInt(COLS);//equal chance to spawn at each of the columns
        if(ran.nextInt(4) == 1) // 25% chance to switch image resource to heart
            obstacles.get(random).setImageResource(R.drawable.ic_icon_heart);
        else
            obstacles.get(random).setImageResource(R.drawable.ic_bitcoin);

        obstacles.get(random).setVisible(true);
        return random;//for UI to also set visible
    }

    public boolean isObstacleVisible(int obstacleIndex)
    {
        return obstacles.get(obstacleIndex).isVisible();
    }

    public int getObstacleImage(int obstacleIndex)
    {
        return obstacles.get(obstacleIndex).getImageResource();
    }

    public void setObstacleImage(int obstacleIndex, int drawable)
    {
        obstacles.get(obstacleIndex).setImageResource(drawable);
    }


    public int nextRowObstacleIndex(int oldObstacleIndex)
    {
        return oldObstacleIndex + COLS;
    }

    public int previousColObstacleIndex(int oldObstacleIndex)
    {
        return oldObstacleIndex - COLS;
    }

    public boolean isObstacleLastRow(int obstacleIndex)
    {
        return obstacleIndex >= obstacles.size() - COLS;
    }

    public boolean isObstacleFirstRow(int obstacleIndex)
    {
        return obstacleIndex < COLS;
    }

    public void setObstacleVisible(int obstacleIndex)
    {
        obstacles.get(obstacleIndex).setVisible(true);
    }

    public void updateImageResource(int obstacleIndex)
    {
        obstacles.get(obstacleIndex).setImageResource(obstacles.get(previousColObstacleIndex(obstacleIndex)).getImageResource());
    }

    public void setObstacleInvisible(int obstacleIndex)
    {
        obstacles.get(obstacleIndex).setVisible(false);
    }

    private Player getCurrentVisiblePlayer()
    {
        return players.get(visiblePlayerIndex);
    }

    public int getVisiblePlayerIndex()
    {
        return visiblePlayerIndex;
    }

    public void setVisiblePlayerIndex(int visiblePlayerIndex)//on button click
    {
        if (visiblePlayerIndex >= 0) this.visiblePlayerIndex = visiblePlayerIndex;
        else this.visiblePlayerIndex = players.size() - 1;

    }

    public boolean checkCollision(int obstacleIndex)
    {
        if (obstacleIndex - (obstacles.size() - COLS) == getVisiblePlayerIndex())
        {
            if(getObstacleImage(obstacleIndex) == R.drawable.ic_bitcoin)
            {
                if(this.wrong<3)
                {
                    this.wrong++;
                }
                return true;
            }
            else if(getObstacleImage(obstacleIndex) == R.drawable.ic_icon_heart)
            {
                if(this.wrong>0)
                    this.wrong--;

                return true;
            }
        }
        return false;
    }

    public void moveLeft()
    {
        getCurrentVisiblePlayer().setVisible(false);
        setVisiblePlayerIndex((getVisiblePlayerIndex() - 1));
        getCurrentVisiblePlayer().setVisible(true);
    }

    public void moveRight()
    {
        getCurrentVisiblePlayer().setVisible(false);
        setVisiblePlayerIndex((getVisiblePlayerIndex() + 1) % 5);
        getCurrentVisiblePlayer().setVisible(true);
    }

    public int getWrong()
    {
        return wrong;
    }

    public int getScore() {return score;}
    public void increaseScore() {this.score++;}

    public boolean isLose()
    {
        return life == wrong;
    } //not for first assignment
}
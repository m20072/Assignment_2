package com.example.assignment2.Models;

public class Obstacle
{
    private boolean Visible;
    private int imageResource;

    public Obstacle()
    {

    }

    public boolean isVisible()
    {
        return Visible;
    }

    public Obstacle setVisible(boolean visible)
    {
        Visible = visible;
        return this;
    }

    public int getImageResource() {
        return imageResource;
    }

    public Obstacle setImageResource(int imageResource)
    {
        this.imageResource = imageResource;
        return this;
    }

}

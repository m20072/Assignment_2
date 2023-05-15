package com.example.assignment2;

import android.app.Application;

import com.example.assignment2.Utilities.MySP;
import com.example.assignment2.Utilities.SignalGenerator;

public class App extends Application
{
    public void onCreate()
    {
        super.onCreate();
        MySP.init(this);
        SignalGenerator.init(this);
    }
}

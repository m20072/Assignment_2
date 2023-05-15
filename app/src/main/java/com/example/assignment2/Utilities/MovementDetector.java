package com.example.assignment2.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.assignment2.Interfaces.movementCallback;

public class MovementDetector
{
    private Sensor sensor;
    private SensorManager sensorManager;
    private movementCallback movementCallback;
    private SensorEventListener sensorEventListener;
    private long movementTimeStamp = 0;
    private long speedTimeStamp = 0;
    private boolean ignore1 = false;
    private boolean ignore2 = false;
    private final int DELAY = 1000;
    private final int CONSTANT = 300;
    private int movementDelay = 0;
    private int speedDelay = 0;

    public MovementDetector(Context context, movementCallback movementCallback)
    {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.movementCallback = movementCallback;
        initEventListener();
    }

    private void initEventListener()
    {
        sensorEventListener = new SensorEventListener()
        {
            @Override
            public void onSensorChanged(SensorEvent event)
            {
                float x = event.values[0]; //by default, event.values[0] is the x axis value
                float y = event.values[1];//by default, event.values[1] is the y axis value

                calculateSideMovement(x);
                calculateSpeed(y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy)
            {
                // pass
            }

        };
    }

    private void calculateSideMovement(float x)
    {
        if (System.currentTimeMillis() - movementTimeStamp > movementDelay)
        {
            movementTimeStamp = System.currentTimeMillis();
            if (x > 2)
            {
                movementCallback.left();
                movementDelay = CONSTANT;
            }

            if (x < -2)
            {
                movementCallback.right();
                movementDelay = CONSTANT;
            }

            if(x<2 && x>-2)
                movementDelay = CONSTANT;

        }
    }

    private void calculateSpeed(float y)
    {
        if (System.currentTimeMillis() - speedTimeStamp > speedDelay)
        {
            speedTimeStamp = System.currentTimeMillis();

            if(y>3.5 && ignore1 == false)
            {
                movementCallback.decrease();
                ignore1 = true;
                speedDelay = DELAY+CONSTANT;
            }

            if(y < -3.5 && ignore2 == false)
            {
                movementCallback.increase();
                ignore2 = true;
                speedDelay = DELAY-CONSTANT;
            }

            if(y<2.0 && y>-2 && (ignore1 == true || ignore2 == true))
            {
                movementCallback.normal();
                ignore1 = false;
                ignore2= false;
                speedDelay = DELAY;
            }
        }
    }
    public void start()
    {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop()
    {
        sensorManager.unregisterListener(sensorEventListener, sensor);
    }
}

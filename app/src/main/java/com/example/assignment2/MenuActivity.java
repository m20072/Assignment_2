package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MenuActivity extends AppCompatActivity
{
    private MaterialButton menu_MB_start;
    private SwitchMaterial menu_SWITCH_sensors;
    private SwitchMaterial menu_SWITCH_fast;
    private MaterialButton menu_MB_topscores;
    private boolean fast;
    private boolean sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        SwitchListeners();
        menu_MB_start.setOnClickListener(v -> startGameScreen(fast, sensors));
        menu_MB_topscores.setOnClickListener(v -> ScoreScreen());
    }

    private void startGameScreen(boolean fast, boolean sensors)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.KEY_FAST, fast);
        intent.putExtra(MainActivity.KEY_SENSORS, sensors);
        startActivity(intent);
        finish();
    }

    private void ScoreScreen()
    {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
        finish();
    }

    private void findViews()
    {
        menu_MB_start = findViewById(R.id.menu_MB_start);
        menu_SWITCH_fast = findViewById(R.id.menu_SWITCH_fast);
        menu_SWITCH_sensors = findViewById(R.id.menu_SWITCH_sensors);
        menu_MB_topscores = findViewById(R.id.menu_MB_topscores);
    }

    private void SwitchListeners()
    {
        menu_SWITCH_fast.setOnCheckedChangeListener((compoundButton, b) -> {if (menu_SWITCH_fast.isChecked()) fast = true; else fast = false;});
        menu_SWITCH_sensors.setOnCheckedChangeListener((compoundButton, b) -> {if(menu_SWITCH_sensors.isChecked()) sensors = true;else sensors = false;});
    }

}
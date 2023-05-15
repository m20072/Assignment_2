package com.example.assignment2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.assignment2.Fragments.ListFragment;
import com.example.assignment2.Interfaces.LocationCallback;
import com.example.assignment2.Logic.ScoreManager;
import com.example.assignment2.Utilities.SignalGenerator;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class ScoreActivity extends AppCompatActivity implements OnMapReadyCallback
{
    public static final String KEY_SCORE = "KEY_SCORE";
    private ScoreManager scoreManager;
    private ListFragment listFragment;
    private SupportMapFragment mapFragment;
    private GoogleMap myMap;
    private final int FINE_PERMISSION_CODE = 1;
    private double scoreLatitude;
    private double scoreLongitude;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private LocationCallback locationCallback = new LocationCallback()
    {

        @Override
        public void setLocation(double latitude, double longitude)
        {
            scoreLatitude = latitude;
            scoreLongitude = longitude;
            mapFragment.getMapAsync(ScoreActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Intent previousIntent = getIntent();
        int currentScore = previousIntent.getIntExtra(KEY_SCORE, 0); //score variable now has the score from the game.
        scoreManager = new ScoreManager(currentScore);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocAndUpdateList();//fragment inits and transactions are within this method because getting the location takes time and might get out of sync with the commands following the call of this method.
    }

    private void getLocAndUpdateList()
    {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>()
        {
            @Override
            public void onSuccess(Location location)
            {
                if(location !=null)
                {
                    initMapFragment();
                    scoreManager.updateTopList(location);
                    getSupportFragmentManager().beginTransaction().add(R.id.score_FRAME_map, mapFragment).commit();
                    initListFragment(); //now can init list cuz done updating the list and its now saved in the SP , so pulling from SP will pull the updated list.
                    beginTransactions();
                }
            }
        });
    }

    private void initListFragment()
    {
        listFragment = new ListFragment();
        listFragment.setCallBack_locationCallback(locationCallback);
    }

    private void initMapFragment()
    {
        mapFragment = new SupportMapFragment();
    }

    private void beginTransactions()
    {
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRAME_list, listFragment).commit();//we basically told the manager to start a fragment on the display/activity, a particular one called listFragment
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) //we get here at some point once we call getMapAsync.
    {
        myMap = googleMap;
        LatLng myLoc = new LatLng(scoreLatitude, scoreLongitude);
        myMap.addMarker(new MarkerOptions().position(myLoc).title("Score"));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc,15)); //better then .newLatLng(myLoc)
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getLocAndUpdateList();
            }
            else
                SignalGenerator.getInstance().toast("Can't get location.", Toast.LENGTH_SHORT);

        }
    }
}
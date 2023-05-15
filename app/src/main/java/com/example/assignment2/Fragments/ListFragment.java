package com.example.assignment2.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.Adapters.ScoreAdapter;
import com.example.assignment2.Interfaces.LocationCallback;
import com.example.assignment2.Interfaces.ScoreCallback;
import com.example.assignment2.Logic.DataManager;
import com.example.assignment2.Models.Score;
import com.example.assignment2.R;


public class ListFragment extends Fragment
{
    private RecyclerView main_LST_scores;
    private LocationCallback locationCallback;

    public void setCallBack_locationCallback(LocationCallback  locationCallback)
    {
        this.locationCallback = locationCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews(view);
        return view;
    }

    private void initViews(View view)
    {
        ScoreAdapter scoreAdapter = new ScoreAdapter(DataManager.getScoreList().getScores());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        main_LST_scores.setAdapter(scoreAdapter);
        main_LST_scores.setLayoutManager(linearLayoutManager);

        scoreAdapter.setScoreCallback(new ScoreCallback()
        {
            @Override
            public void itemClicked(Score score, int position)
            {
                locationCallback.setLocation(score.getLatitude(),score.getLongitude());
            }
        });
    }

    private void findViews(View view) {main_LST_scores = view.findViewById(R.id.main_LST_scores);}

}
package com.example.assignment2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.Interfaces.ScoreCallback;
import com.example.assignment2.Models.Score;
import com.example.assignment2.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>
{
    private ArrayList<Score> scores;
    private ScoreCallback scoreCallback;

    public ScoreAdapter(ArrayList<Score> scores) {this.scores = scores;}


    public void setScoreCallback(ScoreCallback scoreCallback)
    {
        this.scoreCallback = scoreCallback;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        ScoreViewHolder scoreViewHolder = new ScoreViewHolder(view);
        return scoreViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position)
    {
        Score score = getItem(position);
        holder.score_LBL_score.setText(score.getScore() + "");
    }

    @Override
    public int getItemCount() {
        return this.scores == null ? 0 : this.scores.size();
    }

    private Score getItem(int position)
    {
        return this.scores.get(position);
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder
    {
        private MaterialTextView score_LBL_score;

        public ScoreViewHolder(@NonNull View itemView)
        {
            super(itemView);
            score_LBL_score = itemView.findViewById(R.id.score_LBL_score);

            itemView.setOnClickListener(v -> {
                if (scoreCallback != null)
                    scoreCallback.itemClicked(getItem(getAdapterPosition()), getAdapterPosition()); //calling it here actually does something in listfragment.java inside itemclicked function implementation
            });
        }
    }

}

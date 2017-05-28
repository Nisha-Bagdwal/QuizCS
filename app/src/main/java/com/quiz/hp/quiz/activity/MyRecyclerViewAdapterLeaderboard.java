package com.quiz.hp.quiz.activity;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quiz.hp.quiz.R;

import java.util.ArrayList;

/**
 * Created by Hp on 5/4/2017.
 */

public class MyRecyclerViewAdapterLeaderboard extends RecyclerView.Adapter<MyRecyclerViewAdapterLeaderboard.DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<String> mNameSet;
    private ArrayList<String> mEmailSet;
    private ArrayList<String> mScoreSet;
    private ArrayList<String> mRankSet;
    private static MyRecyclerViewAdapterLeaderboard.MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvEmail,tvScore,tvRank;

        public DataObjectHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.leaderboardUsername);
            tvEmail = (TextView) itemView.findViewById(R.id.leaderboardEmail);
            tvScore = (TextView) itemView.findViewById(R.id.leaderboardScore);
            tvRank = (TextView) itemView.findViewById(R.id.leaderboardRank);

            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }

    public void setOnItemClickListener(MyRecyclerViewAdapterLeaderboard.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapterLeaderboard(ArrayList<String> myNameset,ArrayList<String> myEmailset,ArrayList<String> myScoreset,ArrayList<String> myRankset) {
        mNameSet = myNameset;
        mEmailSet=myEmailset;
        mScoreSet=myScoreset;
        mRankSet=myRankset;
    }

    @Override
    public MyRecyclerViewAdapterLeaderboard.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_leaderboard, parent, false);

        MyRecyclerViewAdapterLeaderboard.DataObjectHolder dataObjectHolder = new MyRecyclerViewAdapterLeaderboard.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapterLeaderboard.DataObjectHolder holder, int position) {
        holder.tvName.setText(mNameSet.get(position));
        holder.tvEmail.setText(mEmailSet.get(position));
        holder.tvScore.setText("Score: "+ mScoreSet.get(position));
        holder.tvRank.setText("Rank: "+ mRankSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mNameSet.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}

package com.quiz.hp.quiz.activity;

import android.content.Intent;
import android.os.Bundle;
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
 * Created by Hp on 5/3/2017.
 */

public class MyRecyclerViewAdapterMyScore extends RecyclerView.Adapter<MyRecyclerViewAdapterMyScore.DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<String> mDataset;
    private ArrayList<String> mScoreset;
    private static MyRecyclerViewAdapterMyScore.MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView tvsubject,tvscore;

        public DataObjectHolder(View itemView) {
            super(itemView);
            tvsubject = (TextView) itemView.findViewById(R.id.myScoreSubject);
            tvscore = (TextView) itemView.findViewById(R.id.myScoreScore);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }

    public void setOnItemClickListener(MyRecyclerViewAdapterMyScore.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapterMyScore(ArrayList<String> myDataset,ArrayList<String> myScoreset) {
        mDataset = myDataset;
        mScoreset=myScoreset;
    }

    @Override
    public MyRecyclerViewAdapterMyScore.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_myscores, parent, false);

        MyRecyclerViewAdapterMyScore.DataObjectHolder dataObjectHolder = new MyRecyclerViewAdapterMyScore.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapterMyScore.DataObjectHolder holder, int position) {
        holder.tvsubject.setText(mDataset.get(position));
        holder.tvscore.setText(mScoreset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}

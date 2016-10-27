package com.audreytroutt.androidbeginners.firstapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.audreytroutt.androidbeginners.firstapp.paintinglist.PaintingListAdapter;

public class PaintingGridActivity extends AppCompatActivity {

    private static int NUMBER_OF_COLUMNS = 2;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting_grid);

        mRecyclerView = (RecyclerView) findViewById(R.id.painting_grid_recycler_view);
        mLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PaintingListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(mAdapter.getItemViewType(0), 30);
    }
}
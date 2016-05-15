package com.audreytroutt.androidbeginners.firstapp;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.StringTokenizer;

public class PaintingDetailActivity extends AppCompatActivity {

    private int paintingId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting_detail);

        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            paintingId = extras.getInt("painting_id");
        }

        Resources res = getResources();
        String artist = res.obtainTypedArray(R.array.painting_artists).getString(paintingId);
        String title = res.obtainTypedArray(R.array.painting_titles).getString(paintingId);
        String desc = res.obtainTypedArray(R.array.painting_descriptions).getString(paintingId);
        int year = res.obtainTypedArray(R.array.painting_years).getInt(paintingId, 0);
        Drawable paintingDrawable = res.obtainTypedArray(R.array.paintings).getDrawable(paintingId);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(artist);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.WHITE);
        Picasso.with(this).load(res.obtainTypedArray(R.array.paintings).getResourceId(paintingId, 0)).placeholder(gradientDrawable).into((ImageView) findViewById(R.id.pd_image));

        TextView descView = (TextView) findViewById(R.id.pd_description);
        descView.setText(desc);

        TextView titleYearView = (TextView) findViewById(R.id.pd_title_year);
        titleYearView.setText(title + " (" + year + ")");
    }
}

package com.audreytroutt.androidbeginners.firstapp.paintinglist;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.audreytroutt.androidbeginners.firstapp.PaintingListActivity;
import com.audreytroutt.androidbeginners.firstapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by audrey on 5/15/16.
 */
public class PaintingListAdapter extends RecyclerView.Adapter<PaintingListAdapter.ViewHolder> {

    private final int screenHeight;
    private final int screenWidth;
    Activity context;

    public PaintingListAdapter(Activity context) {
        this.context = context;

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_painting, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Resources res = context.getResources();

        // Load images without crashing the app :)
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.WHITE);
        Picasso.with(context).load(res.obtainTypedArray(R.array.paintings).getResourceId(position, 0)).placeholder(gradientDrawable).into(vh.mPaintingImage);

        // Copy the text into the text views
        vh.mPaintingArtist.setText(res.obtainTypedArray(R.array.painting_artists).getString(position));
        vh.mPaintingTitle.setText(res.obtainTypedArray(R.array.painting_titles).getString(position) + " (" + res.obtainTypedArray(R.array.painting_years).getString(position) + ")");

        View container = vh.mListItem.findViewById(R.id.pl_container);

        //set height in proportion to screen size
        int proportionalHeight = containerHeight();
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, proportionalHeight); // (width, height)
        container.setLayoutParams(params);
    }

    private int containerHeight() {
        return (int) (screenHeight / 3.0);
    }

    @Override
    public int getItemCount() {
        return 25;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View mListItem;
        public ImageView mPaintingImage;
        public TextView mPaintingArtist;
        public TextView mPaintingTitle;

        public ViewHolder(View mListItem) {
            super(mListItem);
            this.mListItem = mListItem;
            mPaintingImage = (ImageView) mListItem.findViewById(R.id.painting_thumb);
            mPaintingArtist = (TextView) mListItem.findViewById(R.id.painting_artist);
            mPaintingTitle = (TextView) mListItem.findViewById(R.id.painting_title);
        }
    }
}

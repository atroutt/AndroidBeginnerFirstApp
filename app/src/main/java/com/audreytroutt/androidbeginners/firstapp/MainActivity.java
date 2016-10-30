package com.audreytroutt.androidbeginners.firstapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri androidBeginnerImageUri;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        if (haveAndroidBeginnerImageLocally()) {
            updateMainImageFromFile();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveAndroidBeginnerImageLocally()) {
                    shareAction();
                } else {
                    // create Intent to take a picture and return control to the calling application
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, getAndroidBeginnerImageUri()); // set the image file name that the camera will save to
                    MainActivity.this.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            // TODO Implement sign out
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // TODO create an intent for the MediaStore.ACTION_IMAGE_CAPTURE
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getAndroidBeginnerImageUri()); // set the image file name
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else if (id == R.id.nav_list) {
            Intent listIntent = new Intent(this, PaintingListActivity.class);
            startActivity(listIntent);
        } else if (id == R.id.nav_grid) {
            // TODO create an intent for the PaintingGridActivity
            Intent listIntent = new Intent(this, PaintingGridActivity.class);
            startActivity(listIntent);
        } else if (id == R.id.nav_web) {
            // TODO create an intent to open a url
            Uri webpage = Uri.parse("http://audreytroutt.com/android-beginners/");
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else if (id == R.id.nav_share) {
            // TODO create an intent to social share about this app
            shareAction();
        } else if (id == R.id.nav_send) {
            // TODO create an intent to send an email
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "gdiandroidbeginners@mailinator.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Testing out my Email Intent -- Success!");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareAction() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "I just made my first Android app!");
        shareIntent.setType("text/plain");
        if (haveAndroidBeginnerImageLocally()) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, getAndroidBeginnerImageUri());
            shareIntent.setType("*/*");
        }
        startActivity(shareIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // A picture was just taken, let's display that in our image view
            updateMainImageFromFile();
        }
    }

    // ----------------------------------
    // Image-related methods
    // ----------------------------------

    private Uri getAndroidBeginnerImageUri() {
        if (androidBeginnerImageUri == null) {
            androidBeginnerImageUri = Uri.fromFile(getAndroidBeginnerImageFile());
        }
        return androidBeginnerImageUri;
    }
    private boolean haveAndroidBeginnerImageLocally() {
        return new File(getAndroidBeginnerImageUri().getPath()).exists();
    }

    /** Create a File for saving an image or video */
    private File getAndroidBeginnerImageFile() {
        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(mediaStorageDir.getPath(), "androidBeginnerImage.jpg");
    }

    private void updateMainImageFromFile() {
        ImageView imageView = (ImageView)findViewById(R.id.camera_image);
        Bitmap bitmap = BitmapFactory.decodeFile(getAndroidBeginnerImageUri().getPath(), null);
        imageView.setImageBitmap(bitmap);

        // Show the picture label "Android Developer"
        findViewById(R.id.picture_label).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.welcome_message)).setText(R.string.main_screen_welcom_message_if_image_set);

        // Hide the instructions for taking a photo
        findViewById(R.id.initial_arrow_image).setVisibility(View.INVISIBLE);
        findViewById(R.id.initial_instructions).setVisibility(View.INVISIBLE);

        // Switch the icon on the FAB to share
        fab.setImageResource(R.drawable.ic_share);
    }
}

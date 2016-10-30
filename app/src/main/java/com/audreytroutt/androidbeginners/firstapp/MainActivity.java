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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawer;
    private Uri androidBeginnerImageUri;
    private FloatingActionButton fab;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up Google Auth and request basic user profile data and email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUserInfoInDrawer();
    }

    private void setUserInfoInDrawer() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView currentUserEmail = (TextView) headerView.findViewById(R.id.current_user_email);
            currentUserEmail.setText(user.getEmail());
            TextView currentUserName = (TextView) headerView.findViewById(R.id.current_user_name);
            currentUserName.setText(user.getDisplayName());
            ImageView currentUserImage = (ImageView) headerView.findViewById(R.id.current_user_photo);
            Picasso.with(this).load(user.getPhotoUrl()).into(currentUserImage);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
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
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                Intent loginScreenIntent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(loginScreenIntent);
                            }
                        }
                    });
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

        mDrawer.closeDrawer(GravityCompat.START);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult.getErrorMessage());
    }

}

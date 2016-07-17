package com.audreytroutt.androidbeginners.firstapp;

import android.app.Application;
import android.widget.Toast;

import java.util.Date;

public class MyFirstApplication extends Application {

    int toastLength = Toast.LENGTH_LONG;
    Date lastLaunchDate = new Date();

    @Override
    public void onCreate() {
        super.onCreate();
        // My Application was just created!
        showToast("I have been created!");
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

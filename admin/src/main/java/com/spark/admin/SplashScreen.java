package com.spark.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.FacebookSdk;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FacebookSdk.fullyInitialize();

        SharedPreferences settings = getSharedPreferences(LoginMain.PREFS_NAME, 0);
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        if (hasLoggedIn) {
            // User is already logged in, navigate to the Business activity
            startActivity(new Intent(SplashScreen.this, BottomNavigation.class));
        } else {
            // User is not logged in, navigate to the login activity
            startActivity(new Intent(SplashScreen.this, LoginMain.class));
        }

        // Finish the splash activity so the user cannot go back to it
        finish();
    }

}
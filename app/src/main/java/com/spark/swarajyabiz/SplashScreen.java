package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SplashScreen extends AppCompatActivity {

    DatabaseReference userRef;
    String userId;

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

        try {
            userRef = FirebaseDatabase.getInstance().getReference("Users");
            SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
             userId = sharedPreference.getString("mobilenumber", null);
            if (userId != null) {
                // userId = mAuth.getCurrentUser().getUid();
                System.out.println("dffvf  " +userId);
                userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String name = snapshot.child("name").getValue(String.class);
                            String activecount = snapshot.child("activeCount").getValue(String.class);

                            if (activecount == null) {
                                activecount = "0";
                            }

                            int count = Integer.parseInt(activecount) + 1;
                            System.out.println("wdevc " + count);

                            // Update the activeCount in the database
                            userRef.child(userId).child("activeCount").setValue(String.valueOf(count));

                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });

            } else {
                // Handle the case where the user ID is not available (e.g., not logged in or not registered)
            }
        }
        catch (Exception e){

        }



        // Finish the splash activity so the user cannot go back to it
        finish();
    }



}
package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.Adapters.FestivalDateAdapter;
import com.spark.swarajyabiz.Adapters.FestivalEventAdapter;
import com.spark.swarajyabiz.Adapters.FestivalMonthsAdapter;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;

public class Festivals_Event extends AppCompatActivity implements FestivalEventAdapter.OnClick {


    FestivalMonthsAdapter festivalMothsAdapter;
    DatabaseReference Festivalsref;
    String userId, month, date;
    RecyclerView recyclerView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festivals_event);


        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
            View decorsView = window.getDecorView();
            // Make the status bar icons dark
            decorsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        recyclerView = findViewById(R.id.festivalview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve keys from the intent extras
        ArrayList<String> keys = getIntent().getStringArrayListExtra("keys");
        TextView textView = findViewById(R.id.titletextthoughts);
        month = getIntent().getStringExtra("months");
        date = getIntent().getStringExtra("date");
        textView.setText(date);


        // Create and set adapter for RecyclerView
        FestivalEventAdapter adapter = new FestivalEventAdapter(this, keys, Festivals_Event.this);
        recyclerView.setAdapter(adapter);

        Festivalsref = FirebaseDatabase.getInstance().getReference("Festival");
    }

    @Override
    public void onClick(String event) {
        Festivalsref.child(month).child(date).child(event).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<String> imageUrls = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String imageUrl = snapshot.getValue(String.class);
                        System.out.println("rgfbx "+imageUrl);
                        imageUrls.add(imageUrl);
                    }

                    // Create an Intent to start BannerDetailsActivity
                    Intent intent = new Intent(Festivals_Event.this, Festival_Images.class);
                    // Pass the image URLs and other necessary data as extras to the intent
                    intent.putStringArrayListExtra("IMAGE_URL", imageUrls);
                    intent.putExtra("contactNumber",userId);
                    intent.putExtra("FESTIVAL_NAME", event);
                    intent.putExtra("date", date);
                    intent.putExtra("month", month);
                    startActivity(intent);
                } else {
                    // Handle case where no data exists under the selected event node
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }
}
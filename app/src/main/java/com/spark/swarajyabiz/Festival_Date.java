package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.spark.swarajyabiz.Adapters.FestivalMonthsAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;

public class Festival_Date extends AppCompatActivity implements FestivalDateAdapter.OnClick {

    FestivalMonthsAdapter festivalMothsAdapter;
    DatabaseReference Festivalsref;
    String userId,  months;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival_date);


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
        months = getIntent().getStringExtra("months");
        textView.setText(months);


        // Create and set adapter for RecyclerView
        FestivalDateAdapter adapter = new FestivalDateAdapter(this, keys, Festival_Date.this);
        recyclerView.setAdapter(adapter);

        Festivalsref = FirebaseDatabase.getInstance().getReference("Festival");
    }

    @Override
    public void onClick(String date) {
        Festivalsref.child(months).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<String> keys = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = snapshot.getKey();
                        keys.add(key);
                    }
                    // Create an Intent to start FestivalsDateActivity
                    Intent intent = new Intent(Festival_Date.this, Festivals_Event.class);
                    // Pass the keys as an extra to the intent
                    intent.putStringArrayListExtra("keys", keys);
                    intent.putExtra("date", date);
                    intent.putExtra("months", months);
                    startActivity(intent);
                } else {
                    // Handle case where no data exists under the selected month node
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }

}
package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.ui.FragmentHome;

import java.util.ArrayList;
import java.util.List;

public class ApplicationDetails extends AppCompatActivity {

    RecyclerView applicationdetails;
    ApplicationDetailsAdapter applicationDetailsAdapter;
    DatabaseReference databaseReference, shopRef, userRef, applicationRef;
    String userId, postcontactNumber, JobID;
    List<CandidateDetials> candidateDetialsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_details);

        applicationdetails = findViewById(R.id.ApplicationDetails);
        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("ApplicationProfile");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        shopRef = FirebaseDatabase.getInstance().getReference("Shop");
        applicationRef = FirebaseDatabase.getInstance().getReference("JobPosts");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.Background));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.Background));
            View decorsView = window.getDecorView();
            // Make the status bar icons dark
            decorsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

        postcontactNumber = getIntent().getStringExtra("postcontactNumber");
        JobID = getIntent().getStringExtra("jobID");

        candidateDetialsList = new ArrayList<>();
        applicationDetailsAdapter = new ApplicationDetailsAdapter(candidateDetialsList, this, sharedPreference);
        applicationdetails.setLayoutManager(new LinearLayoutManager(this));
        applicationdetails.setAdapter(applicationDetailsAdapter);

        retrieveapplicationdetails();
    }


    private void retrieveapplicationdetails(){
        applicationRef.child(postcontactNumber).child(JobID)
                .child("Applications").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                                String key = dataSnapshot.getKey();
                                String name = dataSnapshot.child("name").getValue(String.class);
                                String email = dataSnapshot.child("email").getValue(String.class);
                                String contactNumber = dataSnapshot.child("contact").getValue(String.class);
                                String qualification = dataSnapshot.child("qualification").getValue(String.class);
                                String stream = dataSnapshot.child("stream").getValue(String.class);
                                String skills = dataSnapshot.child("skills").getValue(String.class);
                                String appliedon = dataSnapshot.child("appliedon").getValue(String.class);
                                String address = dataSnapshot.child("address").getValue(String.class);

                                CandidateDetials candidateDetails = new CandidateDetials(name, contactNumber, email, qualification, stream, skills,address, appliedon);

                                // Add the CandidateDetails object to the list
                                candidateDetialsList.add(candidateDetails);
                            }
                        }
                        applicationDetailsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ApplicationDetails extends AppCompatActivity implements ApplicationDetailsAdapter.OnClickListener{

    RecyclerView applicationdetails;
    ApplicationDetailsAdapter applicationDetailsAdapter;
    DatabaseReference databaseReference, shopRef, userRef, applicationRef;
    String userId, postcontactNumber, JobID;
    List<CandidateDetials> candidateDetialsList;
    List<ChatJob> chatJobList;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_details);

        applicationdetails = findViewById(R.id.ApplicationDetails);
        back = findViewById(R.id.back);


        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("ApplicationProfile");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        shopRef = FirebaseDatabase.getInstance().getReference("Shop");
        applicationRef = FirebaseDatabase.getInstance().getReference("JobPosts");

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

        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

        postcontactNumber = getIntent().getStringExtra("postcontactNumber");
        JobID = getIntent().getStringExtra("jobID");

        candidateDetialsList = new ArrayList<>();
        applicationDetailsAdapter = new ApplicationDetailsAdapter(candidateDetialsList, this, sharedPreference, this);
        applicationdetails.setLayoutManager(new LinearLayoutManager(this));
        applicationdetails.setAdapter(applicationDetailsAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        retrieveapplicationdetails();
        retrieveJobPostDetails();
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

    private void retrieveJobPostDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("JobPosts");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    chatJobList = new ArrayList<>();
                    for (DataSnapshot jobPostsSnapshot : snapshot.getChildren()) {
                        String contactNumber = jobPostsSnapshot.getKey();
                        System.out.println("Contact Number: " + contactNumber);
                        String jobkey = jobPostsSnapshot.getKey();

                        for (DataSnapshot postSnapshot : jobPostsSnapshot.getChildren()) {
                            String postkey = postSnapshot.getKey();
                            System.out.println("rdvvfc " + postkey);

                           String jobTitle = postSnapshot.child("jobtitle").getValue(String.class);
                            System.out.println("Job Title: " + jobTitle);

                            String companyname = postSnapshot.child("companyname").getValue(String.class);
                            String joblocation = postSnapshot.child("joblocation").getValue(String.class);
                            String jobtype = postSnapshot.child("jobtype").getValue(String.class);
                            String description = postSnapshot.child("description").getValue(String.class);
                            String workplacetype = postSnapshot.child("workplacetype").getValue(String.class);
                            String currentdate = postSnapshot.child("currentdate").getValue(String.class);
                            String jobid = postSnapshot.child("jobID").getValue(String.class);
                            String experience = postSnapshot.child("experience").getValue(String.class);
                            String salary = postSnapshot.child("salary").getValue(String.class);
                            String skills = postSnapshot.child("skills").getValue(String.class);
                            String jobopenings = postSnapshot.child("jobopenings").getValue(String.class);


                            System.out.println("Company Name: " + companyname);
                            System.out.println("Job Location: " + joblocation);
                            System.out.println("Job Type: " + jobtype);
                            System.out.println("Description: " + description);
                            System.out.println("Workplace Type: " + postcontactNumber);

                            ChatJob chatJob = new ChatJob(companyname, jobTitle, postcontactNumber,userId, jobid);
                            chatJobList.add(chatJob);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    @Override
    public void onChatClick(int position, String candidateName, String UserContactNum) throws ExecutionException, InterruptedException {

        Intent intent = new Intent(this, JobChat.class);
        intent.putExtra("candidateName", candidateName);
        intent.putExtra("UserContactNum", UserContactNum);
        intent.putExtra("BusiContactNum", postcontactNumber);
        intent.putExtra("jobID", JobID);
        startActivity(intent);
    }
}
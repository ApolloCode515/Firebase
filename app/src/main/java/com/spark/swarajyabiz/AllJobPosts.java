package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.annotations.NonNull;

public class AllJobPosts extends AppCompatActivity implements JobPostAdapter.OnClickListener {

    RecyclerView alljobpostsrecyclerview;
    List<JobDetails> jobDetailsList;
    JobPostAdapter jobPostAdapter;
    ImageView back;
    String contactNumber;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_job_posts);

        alljobpostsrecyclerview = findViewById(R.id.alljobpostview);
        back = findViewById(R.id.back);

        contactNumber = getIntent().getStringExtra("contactNumber");
        System.out.println("sgdvcv " +contactNumber);





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
        String userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

        jobDetailsList = new ArrayList<>();
        jobPostAdapter = new JobPostAdapter(jobDetailsList, this, sharedPreference, this);
        alljobpostsrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        alljobpostsrecyclerview.setAdapter(jobPostAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("JobPosts").child(contactNumber);
        retrieveJobPostDetails();

        if (this instanceof AllJobPosts) {
            jobPostAdapter.setHomeFragment(true);
        }else {
            jobPostAdapter.setHomeFragment(false);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void retrieveJobPostDetails() {


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    jobDetailsList.clear();
                    for (DataSnapshot keySnapshot : snapshot.getChildren()) {
                        String postkey = keySnapshot.getKey();
                        System.out.println("sdfvcx " +postkey);

                            String jobTitle = keySnapshot.child("jobtitle").getValue(String.class);
                            System.out.println("Job Title: " + jobTitle);
                            String jobkey = keySnapshot.getKey();

                            String companyname = keySnapshot.child("companyname").getValue(String.class);
                            String joblocation = keySnapshot.child("joblocation").getValue(String.class);
                            String jobtype = keySnapshot.child("jobtype").getValue(String.class);
                            String description = keySnapshot.child("description").getValue(String.class);
                            String workplacetype = keySnapshot.child("workplacetype").getValue(String.class);
                            String currentdate = keySnapshot.child("currentdate").getValue(String.class);
                            String postcontactNumber = keySnapshot.child("contactNumber").getValue(String.class);
                            String jobid = keySnapshot.child("jobID").getValue(String.class);
                            String experience = keySnapshot.child("experience").getValue(String.class);
                            String salary = keySnapshot.child("salary").getValue(String.class);
                            String skills = keySnapshot.child("skills").getValue(String.class);
                            String jobopenings = keySnapshot.child("jobopenings").getValue(String.class);



                        System.out.println("Company Name: " + companyname);
                            System.out.println("Job Location: " + joblocation);
                            System.out.println("Job Type: " + jobtype);
                            System.out.println("Description: " + description);
                            System.out.println("Workplace Type: " + workplacetype);

                            JobDetails jobDetails = new JobDetails(jobTitle, companyname, workplacetype,
                                    joblocation, jobtype, description, currentdate, jobkey, postcontactNumber,
                                    experience, salary, skills,jobopenings, jobid);
                            jobDetailsList.add(jobDetails);

                    }

                    jobPostAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    @Override
    public void onItemClick(int position) throws ExecutionException, InterruptedException {
        // Assuming you have a list of JobDetails and you want to get the data for the clicked position
        JobDetails clickedJob = jobDetailsList.get(position);

        Intent intent = new Intent(this, ApplicationDetails.class);

        intent.putExtra("postcontactNumber", clickedJob.getContactNumber());
        intent.putExtra("jobID", clickedJob.getJobID());
        System.out.println("sdvcf " +clickedJob.getContactNumber());

        startActivity(intent);
    }

    @Override
    public void onchatClick(int position) {

    }
}
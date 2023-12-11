package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

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

        databaseReference = FirebaseDatabase.getInstance().getReference("Shop").child(contactNumber);
        retrieveJobPostDetails();

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
                        for (DataSnapshot jobPostsSnapshot : snapshot.child("JobPosts").getChildren()) {
                            String jobTitle = jobPostsSnapshot.child("jobtitle").getValue(String.class);
                            System.out.println("Job Title: " + jobTitle);
                            String jobkey = jobPostsSnapshot.getKey();

                            String companyname = jobPostsSnapshot.child("companyname").getValue(String.class);
                            String joblocation = jobPostsSnapshot.child("joblocation").getValue(String.class);
                            String jobtype = jobPostsSnapshot.child("jobtype").getValue(String.class);
                            String description = jobPostsSnapshot.child("description").getValue(String.class);
                            String workplacetype = jobPostsSnapshot.child("workplacetype").getValue(String.class);
                            String currentdate = jobPostsSnapshot.child("currentdate").getValue(String.class);

                            System.out.println("Company Name: " + companyname);
                            System.out.println("Job Location: " + joblocation);
                            System.out.println("Job Type: " + jobtype);
                            System.out.println("Description: " + description);
                            System.out.println("Workplace Type: " + workplacetype);

                            JobDetails jobDetails = new JobDetails(jobTitle, companyname, workplacetype, joblocation, jobtype, description,currentdate, jobkey);
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

    }
}
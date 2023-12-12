package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JobPostDetails extends AppCompatActivity {

    TextView jobtitle, companyname, joblocation, workplace, jobpostedtime, jobdescription, jobtype;
    Button applybtn;
    ImageView back;
    DatabaseReference databaseReference, userRef, shopRef, applicationRef;
    String userId, postcontactNumber, jobTitle, Companyname, JobID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post_details);

        jobtitle = findViewById(R.id.jobtitletextview);
        companyname = findViewById(R.id.jobcompanynametextview);
        jobtype = findViewById(R.id.jobtypetextview);
        joblocation = findViewById(R.id.locationtextview);
        workplace = findViewById(R.id.workplacetextview);
        jobpostedtime = findViewById(R.id.timetextview);
        jobdescription = findViewById(R.id.descriptiontextview);
        applybtn = findViewById(R.id.applybtn);
        back = findViewById(R.id.back);


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

        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("ApplicationProfile");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        shopRef = FirebaseDatabase.getInstance().getReference("Shop");
        applicationRef = FirebaseDatabase.getInstance().getReference("JobPosts");

        jobtitle.setText(getIntent().getStringExtra("jobtitle"));
        companyname.setText(getIntent().getStringExtra("companyname"));
        joblocation.setText(getIntent().getStringExtra("joblocation"));
        jobtype.setText(getIntent().getStringExtra("jobtype"));
        workplace.setText(getIntent().getStringExtra("workplacetype"));
        jobdescription.setText(getIntent().getStringExtra("description"));
        jobpostedtime.setText(getIntent().getStringExtra("currentdate"));
        postcontactNumber = getIntent().getStringExtra("postcontactNumber");
        jobTitle = getIntent().getStringExtra("jobtitle");
        Companyname = getIntent().getStringExtra("companyname");
        JobID = getIntent().getStringExtra("jobID");
        System.out.println("dvx er " +JobID);
        System.out.println("dfvcre " +getIntent().getStringExtra("jobtitle"));


        applicationRef.child(postcontactNumber).child(JobID)
                .child("Applications").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() || !snapshot.exists()) {
                    boolean hasApplied = false;

                    if (hasAppliedForJob(snapshot, userId)) {
                        hasApplied = true;
                    }

                    if (hasApplied) {
                        applybtn.setText("Applied");
                        int color = ContextCompat.getColor(JobPostDetails.this, R.color.whiteTextColor);
                        applybtn.setTextColor(color);
                        applybtn.setBackgroundResource(R.drawable.ic_login_bk);
//                        Toast.makeText(JobPostDetails.this, "Already apply this position", Toast.LENGTH_SHORT).show();

                    } else {
                        applybtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                storedetails();
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void storedetails(){
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String usercontact = snapshot.child("contact").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String qualification = snapshot.child("qualification").getValue(String.class);
                    String skills = snapshot.child("skills").getValue(String.class);
                    String stream = snapshot.child("stream").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);

                        String pushkey = databaseReference.push().getKey();

                        DatabaseReference profileRef =  applicationRef.child(postcontactNumber).child(JobID)
                                .child("Applications").child(userId);

                        profileRef.child("name").setValue(name);
                        profileRef.child("contact").setValue(usercontact);
                        profileRef.child("email").setValue(email);
                        profileRef.child("qualification").setValue(qualification);
                        profileRef.child("stream").setValue(stream);
                        profileRef.child("skills").setValue(skills);
                        profileRef.child("address").setValue(address);
                        profileRef.child("jobtitle").setValue(jobTitle);
                        profileRef.child("companyname").setValue(Companyname);

                        applybtn.setText("Applied");
                        int color = ContextCompat.getColor(JobPostDetails.this, R.color.whiteTextColor);
                        applybtn.setTextColor(color);
                        applybtn.setBackgroundResource(R.drawable.ic_login_bk);
                        applybtn.setEnabled(false); // Disable the apply button
                } else {
                    Intent intent = new Intent(getApplicationContext(), ApplicationProfile.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // Helper method to check if the user has already applied for the job
    private boolean hasAppliedForJob(DataSnapshot dataSnapshot, String contactNumber) {
            String applicationcontactNumber = dataSnapshot.getKey();
            System.out.println("dtgfsfgjnj " +applicationcontactNumber);

            if (userId.equals(applicationcontactNumber)) {
                return true; // User has already applied for this job
            }

        return false; // User has not applied for this job
    }
}
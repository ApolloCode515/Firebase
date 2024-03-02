package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JobPostDetails extends AppCompatActivity {

    TextView jobtitle, companyname, joblocation, workplace, jobpostedtime, jobdescription, jobtype, experience,
            salary, skills, jobopenings;
    Button applybtn;
    ImageView back;
    DatabaseReference databaseReference, userRef, shopRef, applicationRef, databaseRef;
    String userId, postcontactNumber, jobTitle, Companyname, JobID, usercontact, name;
    boolean hasApplied=false;
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
        experience = findViewById(R.id.experiencetextview);
        salary = findViewById(R.id.salarytextview);
        skills = findViewById(R.id.skillstextview);
        jobopenings = findViewById(R.id.openingstextview);
        applybtn = findViewById(R.id.applybtn);
        back = findViewById(R.id.back);


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

        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("ApplicationProfile");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        shopRef = FirebaseDatabase.getInstance().getReference("Shop");
        applicationRef = FirebaseDatabase.getInstance().getReference("JobPosts");
        databaseRef = FirebaseDatabase.getInstance().getReference("Shop");

        jobtitle.setText(getIntent().getStringExtra("jobtitle"));
        companyname.setText(getIntent().getStringExtra("companyname"));
        joblocation.setText(getIntent().getStringExtra("joblocation"));
        jobtype.setText(getIntent().getStringExtra("jobtype"));
        workplace.setText(getIntent().getStringExtra("workplacetype"));
        jobdescription.setText(getIntent().getStringExtra("description"));
        jobpostedtime.setText(getIntent().getStringExtra("currentdate"));
        experience.setText(getIntent().getStringExtra("experience"));
        salary.setText(getIntent().getStringExtra("salary"));
        skills.setText(getIntent().getStringExtra("skills"));
        jobopenings.setText(getIntent().getStringExtra("jobopenings"));
        postcontactNumber = getIntent().getStringExtra("postcontactNumber");
        jobTitle = getIntent().getStringExtra("jobtitle");
        Companyname = getIntent().getStringExtra("companyname");
        JobID = getIntent().getStringExtra("jobID");
        System.out.println("dvx er " +JobID);
        System.out.println("dfvcre " +getIntent().getStringExtra("jobtitle"));


        applicationRef.child(postcontactNumber).child(JobID)
                .child("Applications").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            // If "Applications" node exists, check if the user has applied
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String key = dataSnapshot.getKey();
                                System.out.println("wesdc " + key);

                                if (hasAppliedForJob(dataSnapshot, userId)) {
                                    hasApplied = true;
                                    break; // No need to continue checking if the user has already applied
                                }
                            }
                        }

                        if (hasApplied) {
                            // User has already applied
                            applybtn.setText("Applied");
                            int color = ContextCompat.getColor(JobPostDetails.this, R.color.whiteTextColor);
                            applybtn.setTextColor(color);
                            applybtn.setBackgroundResource(R.drawable.ic_login_bk);
                            applybtn.setEnabled(false);
//            Toast.makeText(JobPostDetails.this, "Already apply this position", Toast.LENGTH_SHORT).show();
                        } else {
                            // "Applications" node does not exist or user hasn't applied
                            hasApplied=false;
                            applybtn.setEnabled(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });

        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storedetails();
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        System.out.println("srgvcf " +currentDate);

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                     name = snapshot.child("candidateName").getValue(String.class);
                    usercontact = snapshot.child("candidateContactNumber").getValue(String.class);
                    String email = snapshot.child("candidateEmail").getValue(String.class);
                    String qualification = snapshot.child("candidateQualification").getValue(String.class);
                    String skills = snapshot.child("candidateSkills").getValue(String.class);
                    String stream = snapshot.child("candidateStream").getValue(String.class);
                    String address = snapshot.child("candidateAddress").getValue(String.class);


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
                        profileRef.child("appliedon").setValue(currentDate);
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

        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot currentUsersnapshot) {
                if (currentUsersnapshot.exists()){
                    String currentusercontactNum = currentUsersnapshot.child("contactNumber").getValue(String.class);
                    System.out.println("sfsgr " +currentusercontactNum);
                    String currentUserName = currentUsersnapshot.child("name").getValue(String.class);

                    // Get a reference to the "notification" node under "shopRef"
                    DatabaseReference notificationRef = databaseRef.child(postcontactNumber).child("notification");

                    // Generate a random key for the notification
                    String notificationKey = notificationRef.push().getKey();

                    // Create a message
                    String message = currentUserName + " Applied for " +jobTitle + " position.";
                    String order = jobTitle;

                    String details = JobID+"&&"+jobTitle+"&&"+name+"&&"+userId+"&&"+postcontactNumber;
                    // Create a map to store the message
                    Map<String, Object> notificationData = new HashMap<>();
                    notificationData.put("message", message);
                    notificationData.put("JobTitle", jobTitle);
                    notificationData.put("Jobkey",details);

                    // Store the message under the generated key
                    if (!TextUtils.isEmpty(notificationKey)) {
                        // Notification data setup and setting it to the database
                        notificationRef.child(notificationKey).setValue(notificationData);
                    }
                    DatabaseReference shopNotificationCountRef = databaseRef.child(postcontactNumber)
                            .child("notificationcount");
                    DatabaseReference NotificationCountRef = databaseRef.child(postcontactNumber).child("count")
                            .child("notificationcount");

                    // Read the current count and increment it by 1
                    shopNotificationCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot snapshot) {
                            int currentCount = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
                            int newCount = currentCount + 1;

                            // Update the notification count
                            shopNotificationCountRef.setValue(newCount);
                            NotificationCountRef.setValue(newCount);
                        }

                        @Override
                        public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError error) {
                            // Handle onCancelled event
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

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
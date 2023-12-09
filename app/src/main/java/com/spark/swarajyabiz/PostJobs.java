package com.spark.swarajyabiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.ui.FragmentFestivals;
import com.spark.swarajyabiz.ui.Fragment_Thoughts_Click_Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;

public class PostJobs extends AppCompatActivity implements  BusinessBannerAdapter.OnItemClickListener{


    BusinessBannerAdapter bannerAdapter;
    ImageView back;
    TextView allposttextview, workplaceerrortext, jobtypeerrortext;
    String shopname, shopcontactnumber, shopimage, shopownername, shopaddress;
    EditText jobtitleedittext, companynameedittext, workplaceedittext, locationedittext, jobtypeedittext,
            descriptionedittext;
    Button jobpostbtn;
    DatabaseReference databaseReference;
    RadioButton onsiteradiobutton, remoteradiobutton, halftimeradiobutton, fulltimeradiobutton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_jobs);

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


        jobtitleedittext = findViewById(R.id.titleedittext);
        companynameedittext = findViewById(R.id.companyedittext);
        workplaceedittext = findViewById(R.id.workplaceedittext);
        locationedittext = findViewById(R.id.locationedittext);
        jobtypeedittext = findViewById(R.id.jobtypeedittext);
        descriptionedittext = findViewById(R.id.descriptionedittext);
        jobpostbtn = findViewById(R.id.postjobbtn);
        allposttextview = findViewById(R.id.allpoststextview);
        onsiteradiobutton = findViewById(R.id.onsiteradiobutton);
        remoteradiobutton = findViewById(R.id.remoteradiobutton);
        halftimeradiobutton = findViewById(R.id.halftimeradiobutton);
        fulltimeradiobutton = findViewById(R.id.fulltimeradiobutton);
        workplaceerrortext = findViewById(R.id.workplaceerrortext);
        jobtypeerrortext = findViewById(R.id.jobtypeerrortext);
        back = findViewById(R.id.back);

        shopcontactnumber = getIntent().getStringExtra("contactNumber");
        shopname = getIntent().getStringExtra("shopName");
        shopimage = getIntent().getStringExtra("shopimage");
        shopownername = getIntent().getStringExtra("ownerName");
        shopaddress = getIntent().getStringExtra("shopaddress");

        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Shop");


        companynameedittext.setText(shopname);
        locationedittext.setText(shopaddress);
        if (onsiteradiobutton.isChecked() || remoteradiobutton.isChecked()) {
            workplaceerrortext.setVisibility(View.GONE);
        }
        if (halftimeradiobutton.isChecked() || fulltimeradiobutton.isChecked()) {
            jobtypeerrortext.setVisibility(View.GONE);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        System.out.println("srgvcf " +currentDate);

        jobpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jobtitle = jobtitleedittext.getText().toString();
                String companyname = companynameedittext.getText().toString();
                String workplace = workplaceedittext.getText().toString();
                String joblocation = locationedittext.getText().toString();
                String jobtype = jobtypeedittext.getText().toString();
                String jobdescription = descriptionedittext.getText().toString();

                String workplaceType = "";
                if (onsiteradiobutton.isChecked()) {
                    workplaceType = "Onsite";
                    workplaceerrortext.setVisibility(View.GONE);
                } else if (remoteradiobutton.isChecked()) {
                    workplaceType = "Remote";
                    workplaceerrortext.setVisibility(View.GONE);
                }

                // Check which RadioButton is selected for jobtype
                String jobType = "";
                if (halftimeradiobutton.isChecked()) {
                    jobType = "Halftime";
                    jobtypeerrortext.setVisibility(View.GONE);
                } else if (fulltimeradiobutton.isChecked()) {
                    jobType = "Fulltime";
                    jobtypeerrortext.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(jobtitle)) {
                    jobtitleedittext.setError("नोकरीेचे नाव टाईप करा");
                    return;
                }

                if (TextUtils.isEmpty(companyname)) {
                    companynameedittext.setError("व्यवसायाचे नाव टाईप करा");
                    return;
                }

                if (TextUtils.isEmpty(joblocation)) {
                    locationedittext.setError("नोकरी ठिकाण टाईप करा");
                    return;
                }


                if (TextUtils.isEmpty(jobdescription)) {
                    descriptionedittext.setError("नोकरीेचे वर्णन टाईप करा");
                    return;
                }

                if (workplaceType.isEmpty()) {
                    workplaceerrortext.setVisibility(View.VISIBLE);
                    workplaceerrortext.setText("* कामाच्या ठिकाणाचा प्रकार निवडा");
                    return;  // Exit the method without saving if validation fails
                }

                if (jobType.isEmpty()) {
                    jobtypeerrortext.setVisibility(View.VISIBLE);
                    jobtypeerrortext.setText("* नोकरीेचा प्रकार निवडा");
                    return;  // Exit the method without saving if validation fails
                }
                DatabaseReference jobRef = databaseReference.child(shopcontactnumber).child("JobPosts");

                String finalWorkplaceType = workplaceType;
                String finalJobType = jobType;

                // Generate a push key
                String pushKey = jobRef.push().getKey();

                jobRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        JobDetails jobDetails = snapshot.getValue(JobDetails.class);
                        if (jobDetails == null) {
                            JobDetails newjobInfo = new JobDetails();
                            newjobInfo.setJobtitle(jobtitle);
                            newjobInfo.setCompanyname(companyname);
                            newjobInfo.setWorkplacetype(workplace);
                            newjobInfo.setJoblocation(joblocation);
                            newjobInfo.setJobtype(jobtype);
                            newjobInfo.setDescription(jobdescription);
                            newjobInfo.setWorkplacetype(finalWorkplaceType);
                            newjobInfo.setJobtype(finalJobType);
                            newjobInfo.setCurrentdate(currentDate);

                            // Reference to "JobPosts" without the shopcontactnumber
                            DatabaseReference jobPostsRef = FirebaseDatabase.getInstance().getReference().child("JobPosts");

                            // Using the push key for both locations
                            DatabaseReference jobRefWithPushKey = jobRef.child(pushKey);
                            DatabaseReference jobPostsRefWithPushKey = jobPostsRef.child(pushKey);

                            // Save the new job details to Firebase under both locations
                            jobRefWithPushKey.setValue(newjobInfo);
                            jobPostsRefWithPushKey.setValue(newjobInfo);

                            // Clear EditText fields
                            clearEditTextFields();

                            // Show toast message
                            Toast.makeText(PostJobs.this, "Job details saved successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });

            }
        });

        // Set OnClickListener for allposttextview
        allposttextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AllJobPosts.class);
                startActivity(intent);
            }
        });

        // Initialize RecyclerView and adapter
        RecyclerView recyclerView = findViewById(R.id.postcatagoryview);
        bannerAdapter = new BusinessBannerAdapter(this, this, true);
        recyclerView.setAdapter(bannerAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Use a layout manager that fits your design

        // Fetch image URLs from Firebase and update the adapter
        DatabaseReference adref = FirebaseDatabase.getInstance().getReference("Business");
        adref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<String> keys = new ArrayList<>();
                    List<String> imageUrlsList = new ArrayList<>();

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        keys.add(key);

                        // Assuming that each key has a child node with image URLs
                        for (DataSnapshot imageSnapshot : childSnapshot.getChildren()) {
                            String imageUrl = imageSnapshot.getValue(String.class);
                            if (imageUrl != null) {
                                imageUrlsList.add(imageUrl);
                            }
                        }
                    }

                    // Update the adapter with the lists of keys and image URLs
                    bannerAdapter.setBusinessnametexts(keys);
                    bannerAdapter.setImageUrls(imageUrlsList);
                    bannerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Helper method to clear EditText fields
    private void clearEditTextFields() {
        // Replace these with your actual EditText references
        jobtitleedittext.setText("");
        workplaceedittext.setText("");
        jobtypeedittext.setText("");
        descriptionedittext.setText("");
        onsiteradiobutton.setChecked(false);
        remoteradiobutton.setChecked(false);
        halftimeradiobutton.setChecked(false);
        fulltimeradiobutton.setChecked(false);
    }

    private void loadYourFragment() {
        FragmentFestivals fragment = new FragmentFestivals();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onItemClick(int position, String imageUrl, String businessname) {
        // Handle item click, for example, start a new activity with the selected image
        Intent intent = new Intent(PostJobs.this, CreateBanner.class);
        intent.putExtra("IMAGE_URL", imageUrl);
        intent.putExtra("contactNumber",shopcontactnumber);
        intent.putExtra("shopName", shopname);
        intent.putExtra("shopimage", shopimage);
        intent.putExtra("ownerName", shopownername);
        intent.putExtra("shopaddress", shopaddress);
        startActivity(intent);
    }

}
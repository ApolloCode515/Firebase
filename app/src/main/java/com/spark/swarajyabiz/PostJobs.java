package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.ui.FragmentFestivals;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;

public class PostJobs extends AppCompatActivity {


    BusinessBannerAdapter bannerAdapter;
    ImageView back;
    TextView allposttextview, workplaceerrortext, jobtypeerrortext;
    String shopname, shopcontactnumber, shopimage, shopownername, shopaddress;
    EditText jobtitleedittext, companynameedittext, workplaceedittext, locationedittext, jobtypeedittext,
            descriptionedittext, experienceedittext, packageedittext, skillsedittext, jobopenings;
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
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
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
        experienceedittext = findViewById(R.id.experienceedittext);
        packageedittext = findViewById(R.id.packageedittext);
        skillsedittext = findViewById(R.id.skillsedittext);
        jobopenings = findViewById(R.id.openingedittext);
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
                String experience = experienceedittext.getText().toString();
                String salary = packageedittext.getText().toString();
                String skills = skillsedittext.getText().toString();
                String openings = jobopenings.getText().toString();

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

                if (TextUtils.isEmpty(experience)) {
                    experienceedittext.setError("आवश्यक अनुभव टाईप करा");
                    return;
                }

                if (TextUtils.isEmpty(salary)) {
                    packageedittext.setError("पगार टाईप करा");
                    return;
                }

                if (TextUtils.isEmpty(skills)) {
                    skillsedittext.setError("आवश्यक कौशल्ये टाईप करा");
                    return;
                }

                if (TextUtils.isEmpty(openings)) {
                    jobopenings.setError("आवश्यक जागा टाईप करा");
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
                // String pushKey = jobRef.push().getKey();

                DatabaseReference shopJobRef = FirebaseDatabase.getInstance().getReference("Shop").child(shopcontactnumber).child("JobPosts");
                DatabaseReference generalJobRef = FirebaseDatabase.getInstance().getReference().child("JobPosts").child(shopcontactnumber);

                String generatedKey;
                boolean keyExists;

                // Generate a unique key
                // Generate a unique key
                // Generate a unique key
                do {
                    generatedKey = generateKey(jobtitle, companyname, jobdescription, shopcontactnumber);
                    keyExists = checkIfKeyExists(generalJobRef, generatedKey);
                } while (keyExists);

                // Continue with the rest of the code using the generatedKey

                // Check if the job post with the generated key already exists
                if (!checkIfKeyExists(generalJobRef, generatedKey)) {
                    JobDetails newjobInfo = new JobDetails();
                    newjobInfo.setJobtitle(jobtitle);
                    newjobInfo.setCompanyname(companyname);
                    newjobInfo.setWorkplacetype(workplace);
                    newjobInfo.setJoblocation(joblocation);
                    newjobInfo.setJobtype(jobtype);
                    newjobInfo.setDescription(jobdescription);
                    newjobInfo.setExperience(experience);
                    newjobInfo.setSalary(salary);
                    newjobInfo.setSkills(skills);
                    newjobInfo.setJobopenings(openings);
                    newjobInfo.setWorkplacetype(finalWorkplaceType);
                    newjobInfo.setJobtype(finalJobType);
                    newjobInfo.setCurrentdate(currentDate);
                    newjobInfo.setContactNumber(shopcontactnumber);
                    newjobInfo.setJobID(generatedKey);

                    // Save the new job details to Firebase under general location
                    generalJobRef.child(generatedKey).setValue(newjobInfo);

                    DatabaseReference keywordRef =  generalJobRef.child(generatedKey).child("keywords");

                    // Store the candidate details under each keyword with keys 0, 1, 2, 3, etc.
                    keywordRef.child("0").setValue(jobtitle);
                    keywordRef.child("1").setValue(companyname);
                    keywordRef.child("2").setValue(finalWorkplaceType);
                    keywordRef.child("3").setValue(joblocation);
                    keywordRef.child("4").setValue(jobType);
                    keywordRef.child("5").setValue(jobdescription);
                    keywordRef.child("6").setValue(experience);
                    keywordRef.child("7").setValue(salary);
                    keywordRef.child("8").setValue(skills);


                    // Clear EditText fields
                    clearEditTextFields();
                    showImageSelectiondialog();
                    // Show toast message for successful save
                  //  Toast.makeText(PostJobs.this, "Job details saved successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    // Show toast message if the generated key already exists
                    Toast.makeText(PostJobs.this, "Job title already exists. Please choose a different title.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // Set OnClickListener for allposttextview
        allposttextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AllJobPosts.class);
                intent.putExtra("contactNumber", shopcontactnumber);
                startActivity(intent);
            }
        });

        // Initialize RecyclerView and adapter
//        RecyclerView recyclerView = findViewById(R.id.postcatagoryview);
//        bannerAdapter = new BusinessBannerAdapter(this, this, true);
//        recyclerView.setAdapter(bannerAdapter);
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
//        recyclerView.setLayoutManager(layoutManager);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));  // Use a layout manager that fits your design

        // Fetch image URLs from Firebase and update the adapter
        DatabaseReference adref = FirebaseDatabase.getInstance().getReference("Business");
//        adref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    List<String> keys = new ArrayList<>();
//                    List<String> imageUrlsList = new ArrayList<>();
//
//                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                        String key = childSnapshot.getKey();
//                        keys.add(key);
//
//                        // Assuming that each key has a child node with image URLs
//                        for (DataSnapshot imageSnapshot : childSnapshot.getChildren()) {
//                            String imageUrl = imageSnapshot.getValue(String.class);
//                            if (imageUrl != null) {
//                                imageUrlsList.add(imageUrl);
//                            }
//                        }
//                    }
//
//                    // Update the adapter with the lists of keys and image URLs
//                    bannerAdapter.setBusinessnametexts(keys);
//                    bannerAdapter.setImageUrls(imageUrlsList);
//                    bannerAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle onCancelled
//            }
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showImageSelectiondialog() {
        Dialog dialog1 = new Dialog(this);
        // Inflate the custom layout
        dialog1.setContentView(R.layout.progress_dialog);
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cancelButton = dialog1.findViewById(R.id.closeButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog1.show();
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
    }


    public static String generateKey(String jobtitle, String companyname, String description, String contactNumber) {
        try {
            // Extract the first four letters from jobtitle, companyname, description, and contactNumber
//            String jobTitlePrefix = jobtitle.substring(0, Math.min(jobtitle.length(), 4)).toUpperCase();
//            String companyNamePrefix = companyname.substring(0, Math.min(companyname.length(), 4)).toUpperCase();
//            String descriptionPrefix = description.substring(0, Math.min(description.length(), 4)).toUpperCase();
//            String contactNumberSuffix = contactNumber.substring(Math.max(contactNumber.length() - 4, 0));

            String jobTitlePrefix = jobtitle.toUpperCase();
            String companyNamePrefix = companyname.toUpperCase();
            String descriptionPrefix = description.toUpperCase();
            String contactNumberSuffix = contactNumber.substring(Math.max(contactNumber.length() , 0));

            // Concatenate the extracted prefixes/suffixes
            String concatenatedKey = jobTitlePrefix + companyNamePrefix + descriptionPrefix + contactNumberSuffix;

            // Apply MD5 hashing to create a unique identifier
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(concatenatedKey.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }

            // Extract the first 6 characters from the hash
            return sb.toString().substring(0, 10);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkIfKeyExists(DatabaseReference ref, String key) {
        final boolean[] exists = {false};
        ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exists[0] = snapshot.exists();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
        return exists[0];
    }


//    public static String generateKey(String jobtitle, String companyname, String description, String contactNumber) {
//        try {
//            // Extract the first four letters from jobtitle, companyname, and description
//            String jobTitlePrefix = jobtitle.substring(0, Math.min(jobtitle.length(), 4)).toLowerCase();
//            String companyNamePrefix = companyname.substring(0, Math.min(companyname.length(), 4)).toLowerCase();
//            String descriptionPrefix = description.substring(0, Math.min(description.length(), 4)).toLowerCase();
//
//            // Extract the last four letters from contactNumber
//            String contactNumberSuffix = contactNumber.substring(Math.max(contactNumber.length() - 4, 0));
//
//            // Concatenate the extracted prefixes/suffixes with dashes
//            String generatedKey = jobtitle + "-" + contactNumberSuffix;
//
//            return generatedKey;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    private static String getCurrentDate() {
        // Get the current date in the desired format (adjust the format as needed)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return sdf.format(new Date());
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
        experienceedittext.setText("");
        skillsedittext.setText("");
        packageedittext.setText("");
        jobopenings.setText("");
    }

    private void loadYourFragment() {
        FragmentFestivals fragment = new FragmentFestivals();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


//    @Override
//    public void onItemClick(int position, String imageUrl, String businessname) {
//        // Handle item click, for example, start a new activity with the selected image
//        Intent intent = new Intent(PostJobs.this, CreateBanner.class);
//        intent.putExtra("IMAGE_URL", imageUrl);
//        intent.putExtra("contactNumber",shopcontactnumber);
//        intent.putExtra("shopName", shopname);
//        intent.putExtra("shopimage", shopimage);
//        intent.putExtra("ownerName", shopownername);
//        intent.putExtra("shopaddress", shopaddress);
//        startActivity(intent);
//    }

}
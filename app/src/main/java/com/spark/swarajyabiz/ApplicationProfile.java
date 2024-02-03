package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ApplicationProfile extends AppCompatActivity {

    EditText nameedittext, contactedittext, emailedittext, qualtificationedittext, streamedittext, skillsedittext,
                addressedittext;
    ImageView back;
    Button savebtn;
    DatabaseReference databaseReference, userRef;
    String userId, username, address;
    List<CandidateDetials> candidateDetialsList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_profile);

        nameedittext = findViewById(R.id.nameedittext);
        contactedittext = findViewById(R.id.contactedittext);
        emailedittext = findViewById(R.id.emailedittext);
        qualtificationedittext = findViewById(R.id.qualificationedittext);
        streamedittext = findViewById(R.id.streamedittext);
        skillsedittext = findViewById(R.id.skillsedittext);
        addressedittext = findViewById(R.id.addressedittext);
        savebtn = findViewById(R.id.saveprofiebtn);
        back = findViewById(R.id.back);

        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("ApplicationProfile");
        userRef = FirebaseDatabase.getInstance().getReference("Users");

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

        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                     username = snapshot.child("name").getValue(String.class);
                     address = snapshot.child("district").getValue(String.class);
                     nameedittext.setText(username);
                     addressedittext.setText(address);
                     contactedittext.setText(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameedittext.getText().toString();
                String contact = contactedittext.getText().toString();
                String email = emailedittext.getText().toString();
                String qualification = qualtificationedittext.getText().toString();
                String stream = streamedittext.getText().toString();
                String skills = skillsedittext .getText().toString();
                String address = addressedittext.getText().toString();


                if (TextUtils.isEmpty(name)) {
                    nameedittext.setError("Enter your name");
                    return;
                }

                if (TextUtils.isEmpty(contact)) {
                    contactedittext.setError("Enter your name");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    emailedittext.setError("Enter your name");
                    return;
                }

                if (TextUtils.isEmpty(qualification)) {
                    qualtificationedittext.setError("Enter your name");
                    return;
                }

                if (TextUtils.isEmpty(skills)) {
                    skillsedittext.setError("Enter your name");
                    return;
                }

                if (TextUtils.isEmpty(address)) {
                    addressedittext.setError("Enter your name");
                    return;
                }

                String pushKey = databaseReference.push().getKey();
                DatabaseReference profileRef = databaseReference.child(contact);
                CandidateDetials candidateDetials = new CandidateDetials();

                candidateDetials.setCandidateName(name);
                candidateDetials.setCandidateEmail(email);
                candidateDetials.setCandidateContactNumber(contact);
                candidateDetials.setCandidateQualification(qualification);
                candidateDetials.setCandidateStream(stream);
                candidateDetials.setCandidateSkills(skills);
                candidateDetials.setCandidateAddress(address);

                profileRef.setValue(candidateDetials);
                // Extract keywords from the candidate details
                List<String> keywords = candidateDetials.getKeywords();
                DatabaseReference keywordRef = profileRef.child("keywords");

                // Store the candidate details under each keyword with keys 0, 1, 2, 3, etc.
                keywordRef.child("0").setValue(name);
                keywordRef.child("1").setValue(email);
                keywordRef.child("2").setValue(qualification);
                keywordRef.child("3").setValue(stream);
                keywordRef.child("4").setValue(skills);
                keywordRef.child("5").setValue(address);



//                profileRef.child("name").setValue(name);
//                profileRef.child("contact").setValue(contact);
//                profileRef.child("email").setValue(email);
//                profileRef.child("qualification").setValue(qualification);
//                profileRef.child("stream").setValue(stream);
//                profileRef.child("skills").setValue(skills);
//                profileRef.child("address").setValue(address);

                Toast.makeText(ApplicationProfile.this, "Profile Created Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
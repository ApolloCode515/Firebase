package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BusinessCard extends AppCompatActivity {

    Button webbtn, demobtn;
    String userId;
    Boolean premium;
    DatabaseReference usersRef, busicardRef;
    ImageView businesscardimg;
    ImageView back;
    AlertDialog dialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);

        webbtn = findViewById(R.id.webbtn);
        businesscardimg = findViewById(R.id.busicardimg);
        demobtn = findViewById(R.id.demobtn);
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

        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        busicardRef = FirebaseDatabase.getInstance().getReference("BusinessCard");
        busicardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String image = snapshot.child("01").getValue(String.class);
                    Glide.with(BusinessCard.this)
                            .load(image)
                            .into(businesscardimg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        webbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersRef.child("premium").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            premium = snapshot.child("premium").getValue(Boolean.class);
                            System.out.println("sdzcvf " +premium);

                            if (premium.equals(true)){
                                // Replace "https://www.example.com" with your desired URL
                                String url = "https://kamdhanda.online";

                                Intent intent = new Intent(BusinessCard.this, WebsiteView.class);
                                intent.putExtra("url", url);
                                startActivity(intent);
                            }
                            else{
                               showImageSelectionDialog();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                    }
                });
            }
        });

        demobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DemoBusinessCard.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showImageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("प्रीमियम");
        builder.setMessage("डिजिटल बिजनेस कार्ड ( मिनी वेबसाइट ) तयार करण्यासाठी प्रिमियम प्लॅन खरेदी करावा लागेल.\n");
        builder.setPositiveButton("क्लिक करा", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(BusinessCard.this, PremiumMembership.class);
                startActivity(intent);
            }
        });
//        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                // User clicked on Cancel, so just close the dialog
//                Intent intent = new Intent(getContext(), PremiumMembership.class); // Replace "PreviousActivity" with the appropriate activity class
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//
//            }
//        });



        dialog = builder.create();

        // Set the dialog to not be canceled on touch outside
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.setCancelable(false);

        // Show the dialog
        dialog.show();

    }

}
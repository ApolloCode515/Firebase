package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class NotificationPage extends AppCompatActivity {

    DatabaseReference databaseRef, userRef;
    String userId, contactNumber;
    RecyclerView notificationrecyclerView ;
    NotificationAdapter notificationAdapter;
    List<Notification> notificationList;
    TextView clearnotification;
    ImageView back;
    private static final String USER_ID_KEY = "userID";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_page);

        clearnotification = findViewById(R.id.clearnotification);
        back = findViewById(R.id.back);

        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference().child("Shop");
        userRef = database.getReference("Users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

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

        notificationrecyclerView = findViewById(R.id.notificationdetails);
        notificationrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notificationList, this);
        notificationrecyclerView.setAdapter(notificationAdapter);

        contactNumber = getIntent().getStringExtra("contactNumber");

        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                   System.out.println("sdvfv " +contactNumber);

                   DatabaseReference notificationRef = databaseRef.child(contactNumber).child("notification");

                   notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                           if (snapshot.exists()){
                               for (DataSnapshot notificationSnapshot : snapshot.getChildren()){
                                   String message = notificationSnapshot.child("message").getValue(String.class);
                                   System.out.println("sddvb " +message);
                                   Notification notification = notificationSnapshot.getValue(Notification.class);
                                   notificationList.add(notification);
                               }
                               notificationAdapter.notifyDataSetChanged();
                           }
                       }

                       @Override
                       public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                       }
                   });

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        clearnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationPage.this);
                builder.setTitle("History Clear");
                builder.setMessage("Are you sure you want to clear history?");
                builder.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle the delete action here
                        // You can put your Firebase database delete code here

                        // Example code to delete a node in Firebase Realtime Database
                        databaseRef.child(contactNumber).child("notification").removeValue();
                        databaseRef.child(contactNumber).child("notificationcount").setValue(0);
                        databaseRef.child(contactNumber).child("count").child("notificationcount").setValue(0);
                        Log.d("TAG", "onClick: " +contactNumber);
                        // Close the dialog
                        dialogInterface.dismiss();
                        recreate();
                        // Redirect to the business page
//                        Intent intent = new Intent(NotificationPage.this, Business.class);
//                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked on Cancel, so just close the dialog
                        dialogInterface.dismiss();
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
        finish();
    }
}
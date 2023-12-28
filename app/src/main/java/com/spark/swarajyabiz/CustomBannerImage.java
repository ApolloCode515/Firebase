package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomBannerImage extends AppCompatActivity implements CustomMatterAdapter.onClickListener{

    String userId, edittexthint;
    EditText customedittext;
    Button savebtn;
    DatabaseReference customBannerRef, customBusinessRef;
    RecyclerView customRecyclerview;
    List<String> matterList;
    CustomMatterAdapter adapter;
    ImageView back;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_banner_image);

        customedittext = findViewById(R.id.customedittext);
        savebtn = findViewById(R.id.savecustombtn);
        customRecyclerview = findViewById(R.id.custommatterview);
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
        customBannerRef = FirebaseDatabase.getInstance().getReference("Custom").child(userId).child("Banner");
        customBusinessRef = FirebaseDatabase.getInstance().getReference("Custom").child(userId).child("Business");

        edittexthint = getIntent().getStringExtra("hint");
        customedittext.setHint(edittexthint);

         matterList = new ArrayList<>();
         adapter = new CustomMatterAdapter(matterList, this);
        customRecyclerview.setLayoutManager(new LinearLayoutManager(this));
            customRecyclerview.setAdapter(adapter);

            if (edittexthint.equals("इथे तुमच्या इमेज वरील मॅटर टाइप करा")) {
                retrieveBannerFromFirebase();
            }else{
                retrieveBusinessFromFirebase();
            }



        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edittexthint.equals("इथे तुमच्या इमेज वरील मॅटर टाइप करा")) {
                    storecustominfo();
                }else{
                    storebusiInfo();
                }
            }
        });

       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });

    }

    private void retrieveBannerFromFirebase() {
        customBannerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Clear the existing data
                    matterList.clear();
                    // Iterate through the snapshot and add text data to the list
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String matter = dataSnapshot.getValue(String.class);
                        System.out.println("wsfcxc " + matter);
                        if (matter != null) {
                            matterList.add(matter);
                        }
                    }

                    // Update the adapter with the new data
                    adapter.setMatterList(matterList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void retrieveBusinessFromFirebase() {
        customBusinessRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Clear the existing data
                    matterList.clear();
                    // Iterate through the snapshot and add text data to the list
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String matter = dataSnapshot.getValue(String.class);
                        System.out.println("wsfcxc " + matter);
                        if (matter != null) {
                            matterList.add(matter);
                        }
                    }

                    // Update the adapter with the new data
                    adapter.setMatterList(matterList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void storebusiInfo() {
        customBusinessRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get the current maximum key
                long maxKey = -1;
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    long key = Long.parseLong(childSnapshot.getKey());
                    if (key > maxKey) {
                        maxKey = key;
                    }
                }
                long newKey = maxKey + 1;
                String customValue = customedittext.getText().toString();
                customBusinessRef.child(String.valueOf(newKey)).setValue(customValue);
                customedittext.setText("");
                Toast.makeText(CustomBannerImage.this, "Data stored successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }


    private void storecustominfo() {
        customBannerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get the current maximum key
                long maxKey = -1;
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    long key = Long.parseLong(childSnapshot.getKey());
                    if (key > maxKey) {
                        maxKey = key;
                    }
                }
                long newKey = maxKey + 1;
                String customValue = customedittext.getText().toString();
                customBannerRef.child(String.valueOf(newKey)).setValue(customValue);
                customedittext.setText("");
                Toast.makeText(CustomBannerImage.this, "Data stored successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    @Override
    public void onDeleteClick(int position) {
        if (edittexthint.equals("इथे तुमच्या इमेज वरील मॅटर टाइप करा")) {
            deletecustombanner(position);
        }else {
            deletecustombusiness(position);
        }
    }

    private void deletecustombanner(int position){
        customBannerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Find the key at the specified position
                long keyToDelete = -1;
                int currentPosition = 0;
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    if (currentPosition == position) {
                        keyToDelete = Long.parseLong(childSnapshot.getKey());
                        break;
                    }
                    currentPosition++;
                }

                // Delete the value using the key
                if (keyToDelete != -1) {
                    customBannerRef.child(String.valueOf(keyToDelete)).removeValue();
                    adapter.notifyDataSetChanged();
                    // Notify user or update UI as needed
                    Toast.makeText(CustomBannerImage.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

    }

    private void deletecustombusiness(int position){
            customBusinessRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Find the key at the specified position
                    long keyToDelete = -1;
                    int currentPosition = 0;
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        if (currentPosition == position) {
                            keyToDelete = Long.parseLong(childSnapshot.getKey());
                            break;
                        }
                        currentPosition++;
                    }

                    // Delete the value using the key
                    if (keyToDelete != -1) {
                        customBusinessRef.child(String.valueOf(keyToDelete)).removeValue();
                        adapter.notifyDataSetChanged();
                        // Notify user or update UI as needed
                        Toast.makeText(CustomBannerImage.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });
        }
}
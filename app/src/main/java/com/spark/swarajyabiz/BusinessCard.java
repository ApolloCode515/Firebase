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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.ui.Fragment_Banner;
import com.spark.swarajyabiz.ui.Fragment_Business_post;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BusinessCard extends AppCompatActivity implements BusinessCardBannerAdapter.OnItemClickListener {

    Button webbtn, demobtn;
    String userId;
    Boolean premium;
    DatabaseReference usersRef, busicardRef;
    ImageView businesscardimg;
    ImageView back;
    AlertDialog dialog;
    List<String> imageUrls;
    RecyclerView busimgRecyclerview;
    BusinessCardBannerAdapter businessBannerAdapter;
    private int currentPosition = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final int AUTO_SCROLL_DELAY = 5000; // Set your desired auto-scroll delay in milliseconds
    private LinearLayout dotsLayout;
    private int lastVisiblePosition = RecyclerView.NO_POSITION;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);

        webbtn = findViewById(R.id.webbtn);
        businesscardimg = findViewById(R.id.busicardimg);
        demobtn = findViewById(R.id.demobtn);
        back = findViewById(R.id.back);
        dotsLayout = findViewById(R.id.dotsLayout);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
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

        imageUrls = new ArrayList<>();
        busimgRecyclerview = findViewById(R.id.busiimageview);
        busimgRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        businessBannerAdapter = new BusinessCardBannerAdapter(this, sharedPreference, BusinessCard.this);
        busimgRecyclerview.setAdapter(businessBannerAdapter);

        retrieveBusinessCardBanner();
       // startAutoScroll();


        if (this instanceof BusinessCard) {
            businessBannerAdapter.setShopFragment(true);
        } else {
            businessBannerAdapter.setShopFragment(false);
        }

        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        busicardRef = FirebaseDatabase.getInstance().getReference("BusinessCard");
        busicardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String image = snapshot.child("0").getValue(String.class);
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
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void startAutoScroll() {
        // Create a runnable to scroll to the next item after a delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollNext();
                // Post the same runnable with a delay to create a loop
                handler.postDelayed(this, AUTO_SCROLL_DELAY);
            }
        }, AUTO_SCROLL_DELAY);

        // Add an OnScrollListener to the RecyclerView to detect when scrolling has finished
        busimgRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                updateDots();
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateDots();
                }
            }
        });

    }

    private void scrollNext() {
        // Check if there are items in the adapter
        if (businessBannerAdapter.getItemCount() > 0) {

            // Increment the current position and scroll to the next item
            currentPosition = (currentPosition + 1) % businessBannerAdapter.getItemCount();
            busimgRecyclerview.smoothScrollToPosition(currentPosition);
          // Update dots after scrolling

        }
    }

    @Override
    protected void onDestroy() {
        // Remove callbacks to prevent memory leaks
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void createDots(int numberOfDots) {
        dotsLayout.removeAllViews();
        for (int i = 0; i < numberOfDots; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_unselected));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dotsLayout.addView(dot);
        }
    }



    private void updateDots() {
        int totalDots = dotsLayout.getChildCount();
        int visiblePosition = ((LinearLayoutManager) busimgRecyclerview.getLayoutManager()).findFirstVisibleItemPosition();

        if (imageUrls != null && !imageUrls.isEmpty() && visiblePosition != RecyclerView.NO_POSITION && visiblePosition < imageUrls.size()) {
            if (lastVisiblePosition != RecyclerView.NO_POSITION && lastVisiblePosition >= 0 && lastVisiblePosition < totalDots) {
                ImageView previousDot = (ImageView) dotsLayout.getChildAt(lastVisiblePosition);
                if (previousDot != null) {
                    previousDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_unselected));
                }
            }

            if (visiblePosition >= 0 && visiblePosition < totalDots) {
                ImageView selectedDot = (ImageView) dotsLayout.getChildAt(visiblePosition);
                if (selectedDot != null) {
                    selectedDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_selected));
                }
                lastVisiblePosition = visiblePosition;
            }
        } else {
            // Debug logging to help identify the issue
            Log.d("DotsDebug", "imageUrls: " + imageUrls);
            Log.d("DotsDebug", "visiblePosition: " + visiblePosition);
            Log.d("DotsDebug", "lastVisiblePosition: " + lastVisiblePosition);
            Log.d("DotsDebug", "totalDots: " + totalDots);

            if (totalDots > 0) {
                if (lastVisiblePosition != RecyclerView.NO_POSITION && lastVisiblePosition >= 0 && lastVisiblePosition < totalDots) {
                    ImageView lastDot = (ImageView) dotsLayout.getChildAt(lastVisiblePosition);
                    if (lastDot != null) {
                        lastDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_unselected));
                    }
                    lastVisiblePosition = 0; // Set the last visible position to the first dot
                } else {
                    lastVisiblePosition = 0; // Set the last visible position to the first dot
                }
                ImageView selectedDot = (ImageView) dotsLayout.getChildAt(lastVisiblePosition);
                if (selectedDot != null) {
                    selectedDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_selected));
                }
            }
        }
    }

    private void retrieveBusinessCardBanner(){
        DatabaseReference investRef = FirebaseDatabase.getInstance().getReference("BusinessCard");
        System.out.println("dsjvnjv " +investRef);

        investRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                                String imageUrl = dataSnapshot.getValue(String.class);
                                if (imageUrl != null) {
                                    imageUrls.add(imageUrl);
                                    System.out.println("fgnfvb "+imageUrl);


                                }
                    }
                    businessBannerAdapter.setImageUrls(imageUrls);
                    businessBannerAdapter.notifyDataSetChanged();;
                    createDots(imageUrls.size());  // Call createDots here
                    updateDots();
                    startAutoScroll();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showImageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BusinessCard.this);

        // Inflate the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
        builder.setView(customLayout);

        // Find views in the custom layout
        ImageView alertImageView = customLayout.findViewById(R.id.alertImageView);
        TextView alertTitle = customLayout.findViewById(R.id.alertTitle);
        TextView alertMessage = customLayout.findViewById(R.id.alertMessage);
        Button positiveButton = customLayout.findViewById(R.id.positiveButton);

        // Customize the views as needed
        Glide.with(this).asGif().load(R.drawable.gif2).into(alertImageView); // Replace with your image resource
        alertTitle.setText("प्रीमियम");
        alertMessage.setText("डिजिटल बिजनेस कार्ड ( मिनी वेबसाइट ) तयार करण्यासाठी प्रिमियम प्लॅन खरेदी करावा लागेल.");

        // Set positive button click listener
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BusinessCard.this, PremiumMembership.class);
                startActivity(intent);
                dialog.dismiss(); // Dismiss the dialog after the button click
            }
        });

        // Create and show the dialog
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onItemClick(int position, Boolean premium, String imageUrl) throws ExecutionException, InterruptedException {

    }
}
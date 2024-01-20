package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ReviewInfo extends AppCompatActivity {

    TextView shopName;
    ImageView back;
    RecyclerView reviewRecyclerView;
    ReviewAdapter reviewAdapter;
    String userId, shopContactNumber;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_info);

        shopName = findViewById(R.id.shopNametextView);
        reviewRecyclerView = findViewById(R.id.reviewsview);
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
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

        shopContactNumber = getIntent().getStringExtra("shopContactNumber");
        String shopname = getIntent().getStringExtra("shopName");
        shopName.setText(shopname);

        retrieveReview();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void retrieveReview(){
        List<Review> reviewList = new ArrayList<>();
        DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("Shop").child(shopContactNumber).child("review");

        reviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot reviewSnapshot : snapshot.getChildren()){
                        String rating = reviewSnapshot.child("rating").getValue(String.class);
                        String reviews = reviewSnapshot.child("review").getValue(String.class);
                        String date = reviewSnapshot.child("date").getValue(String.class);
                        String userNo = reviewSnapshot.child("userNo").getValue(String.class);
                        String userName = reviewSnapshot.child("userName").getValue(String.class);
                        System.out.println("rfeththg"+userName);

                        Review review = new Review();
                        review.setRating(rating);
                        review.setReview(reviews);
                        review.setDate(date);
                        review.setUserNo(userNo);
                        review.setUserName(userName);
                        reviewList.add(review);
                    }

                    // Sort the reviewList based on date in descending order
                    Collections.sort(reviewList, new Comparator<Review>() {
                        @Override
                        public int compare(Review review1, Review review2) {
                            // Assuming date is in "dd/MM/yyyy" format
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                Date currentDate = new Date();  // Current date

                                Date date1 = sdf.parse(review1.getDate());
                                Date date2 = sdf.parse(review2.getDate());

                                // Compare the difference between current date and review date
                                long diff1 = Math.abs(currentDate.getTime() - date1.getTime());
                                long diff2 = Math.abs(currentDate.getTime() - date2.getTime());

                                return Long.compare(diff1, diff2);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });

                    reviewAdapter = new ReviewAdapter(reviewList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ReviewInfo.this);
                    reviewRecyclerView.setLayoutManager(layoutManager);
                    reviewRecyclerView.setAdapter(reviewAdapter);

                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });
    }

}
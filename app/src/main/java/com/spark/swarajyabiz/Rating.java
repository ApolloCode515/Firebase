package com.spark.swarajyabiz;


import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class Rating extends AppCompatActivity {
    private List<Integer> ratings = new ArrayList<>();
    private int[] ratingCounts = new int[5];
    private int totalRating = 0;
    TextView giverating;
    private int userCount = 0;

    private TextView totalRatingTextView;
    private TextView averageRatingTextView;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopdetails);

        // Initialize the progress bars
        ProgressBar progressBar1 = findViewById(R.id.progress_bar_1);
        ProgressBar progressBar2 = findViewById(R.id.progress_bar_2);
        ProgressBar progressBar3 = findViewById(R.id.progress_bar_3);
        ProgressBar progressBar4 = findViewById(R.id.progress_bar_4);
        ProgressBar progressBar5 = findViewById(R.id.progress_bar_5);
        giverating = findViewById(R.id.giverating);

        RatingBar ratingBar = findViewById(R.id.rating_bar);

        // Initialize the Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("rating");


        // Retrieve ratings from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear previous ratings
                ratings.clear();
                for (int i = 0; i < ratingCounts.length; i++) {
                    ratingCounts[i] = 0;
                }
                totalRating = 0;
                userCount = 0;

                // Retrieve new ratings
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String ratingKey = snapshot.getKey();
                    int ratingCount = snapshot.getValue(Integer.class);
                    int ratingValue = Integer.parseInt(ratingKey.substring(ratingKey.lastIndexOf("_") + 1));
                    ratingCounts[ratingValue - 1] = ratingCount;
                    totalRating += ratingCount * ratingValue;
                    userCount += ratingCount;
                    for (int i = 0; i < ratingCount; i++) {
                        ratings.add(ratingValue);
                    }
                }

                // Update the UI with retrieved ratings
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Rating.this, "Failed to retrieve ratings", Toast.LENGTH_SHORT).show();
            }
        });
        giverating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingBar.setVisibility(View.VISIBLE);
            }
        });

        // Set the OnRatingBarChangeListener for the rating bar
        // RatingBar ratingBar = findViewById(R.id.rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int roundedRating = Math.round(rating);

                // Disable the RatingBar to prevent further rating changes
                ratingBar.setIsIndicator(false);


                // Add the rating to the list
                ratings.add(roundedRating);

                // Update the rating counts
                ratingCounts[roundedRating - 1]++;

                // Update the total rating
                totalRating += roundedRating;

                // Update the user count
                userCount++;

                // Update the progress bars
                int maxProgress = userCount;
                progressBar1.setMax(maxProgress);
                progressBar2.setMax(maxProgress);
                progressBar3.setMax(maxProgress);
                progressBar4.setMax(maxProgress);
                progressBar5.setMax(maxProgress);
                progressBar1.setProgress(ratingCounts[0]);
                progressBar2.setProgress(ratingCounts[1]);
                progressBar3.setProgress(ratingCounts[2]);
                progressBar4.setProgress(ratingCounts[3]);
                progressBar5.setProgress(ratingCounts[4]);

                // Update the total rating TextView
                TextView totalRatingTextView = findViewById(R.id.total_rating_text_view);
                totalRatingTextView.setText("" + userCount);

                // Calculate and update the average rating
                float averageRating = calculateAverageRating();
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                String averageRatingFormatted = decimalFormat.format(averageRating);
                TextView averageRatingTextView = findViewById(R.id.average_rating_text_view);
                RatingBar Avgrating_bar = findViewById(R.id.Avgrating_bar);
                averageRatingTextView.setText(averageRatingFormatted);
                Avgrating_bar.setRating(averageRating);

                // Store the ratings in Firebase Realtime Database
                storeRatingsInFirebase();
            }
        });


        // Initialize the total rating TextView
        //totalRatingTextView = findViewById(R.id.total_rating_text_view);

        // Initialize the average rating TextView
        // averageRatingTextView = findViewById(R.id.average_rating_text_view);
    }

    private float calculateAverageRating() {
        if (ratings.size() == 0) {
            return 0.0F;
        }

        int sum = 0;
        for (int rating : ratings) {
            sum += rating;
        }

        return (float) sum / ratings.size();
    }

    private void storeRatingsInFirebase() {
        Map<String, Integer> ratingMap = new HashMap<>();
        for (int i = 0; i < ratingCounts.length; i++) {
            ratingMap.put("rating_" + (i + 1), ratingCounts[i]);
        }

        // Store the ratings in the "ratings" node in Firebase Realtime Database
        databaseReference.setValue(ratingMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(Rating.this, "Ratings stored successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Rating.this, "Failed to store ratings", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateUI() {
        // Update the progress bars
        ProgressBar progressBar1 = findViewById(R.id.progress_bar_1);
        ProgressBar progressBar2 = findViewById(R.id.progress_bar_2);
        ProgressBar progressBar3 = findViewById(R.id.progress_bar_3);
        ProgressBar progressBar4 = findViewById(R.id.progress_bar_4);
        ProgressBar progressBar5 = findViewById(R.id.progress_bar_5);
        int maxProgress = userCount;
        progressBar1.setMax(maxProgress);
        progressBar2.setMax(maxProgress);
        progressBar3.setMax(maxProgress);
        progressBar4.setMax(maxProgress);
        progressBar5.setMax(maxProgress);
        progressBar1.setProgress(ratingCounts[0]);
        progressBar2.setProgress(ratingCounts[1]);
        progressBar3.setProgress(ratingCounts[2]);
        progressBar4.setProgress(ratingCounts[3]);
        progressBar5.setProgress(ratingCounts[4]);

        TextView totalRatingTextView = findViewById(R.id.total_rating_text_view);
        totalRatingTextView.setText("" + userCount);

        // Calculate and update the average rating
        float averageRating = calculateAverageRating();
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String averageRatingFormatted = decimalFormat.format(averageRating);
        TextView averageRatingTextView = findViewById(R.id.average_rating_text_view);
        RatingBar Avgrating_bar = findViewById(R.id.Avgrating_bar);
        averageRatingTextView.setText(averageRatingFormatted);
        Avgrating_bar.setRating(averageRating);
    }
}

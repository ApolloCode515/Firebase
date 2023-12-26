package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PremiumMembership extends AppCompatActivity {

    ViewPager2 viewPager2;
    List<String> imageUrls;
    TextView textView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_membership);

        viewPager2 = findViewById(R.id.viewpager2);
        textView = findViewById(R.id.pricetextview);
       List<SlideImage> slideImages = new ArrayList<>();
//        slideImages.add(new SlideImage(R.drawable.a));
//        slideImages.add(new SlideImage(R.drawable.aa));
//        slideImages.add(new SlideImage(R.drawable.b));
//        slideImages.add(new SlideImage(R.drawable.aaa));
//        slideImages.add(new SlideImage(R.drawable.frame5));
//        slideImages.add(new SlideImage(R.drawable.frame6));

        SliderAdapter sliderAdapter = new SliderAdapter(slideImages, viewPager2, this);
        viewPager2.setAdapter(sliderAdapter);


       // viewPager2.setAdapter(new SliderAdapter(slideImages, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);


        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1- Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // Ensure the position is within the bounds of the list
                if (position >= 0 && position < slideImages.size()) {
                    // Get the price for the current item and update the text view
                    String price = slideImages.get(position).getPrice();
                    textView.setText(price+ " Rs");
                }
            }
        });

        viewPager2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the currently selected position
                int position = viewPager2.getCurrentItem();
                // Ensure the position is within the bounds of the list
                if (position >= 0 && position < slideImages.size()) {
                    // Get the price for the current item
                    String price = slideImages.get(position).getPrice();
                    // Show a toast message with the price
                    Toast.makeText(PremiumMembership.this, "Price: " + price + " Rs", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DatabaseReference membershipRef = FirebaseDatabase.getInstance().getReference("PremiumMembership");

        membershipRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot packageSnapshot : snapshot.getChildren()) {
                        String packageName = packageSnapshot.getKey();
                        System.out.println("Package Name: " + packageName);
                        DatabaseReference packageRef = membershipRef.child(packageName);

                        packageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot packageDataSnapshot) {
                                if (packageDataSnapshot.exists()) {
                                    String imageUrl = packageDataSnapshot.child("image").getValue(String.class);
                                    String price = packageDataSnapshot.child("price").getValue(String.class);

                                    if (imageUrl != null && price != null) {
                                        SlideImage newSlideImage = new SlideImage(imageUrl, price);
                                        slideImages.add(newSlideImage);
                                        sliderAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle onCancelled
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });


    }

    private void updatePriceTextView(String price) {
        // Implement this method to update your text view with the given price
        // For example, if you have a TextView named priceTextView:
        // priceTextView.setText(price);
    }
}
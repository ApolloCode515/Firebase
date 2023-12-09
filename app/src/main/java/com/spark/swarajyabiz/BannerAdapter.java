package com.spark.swarajyabiz;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerviewHolder> {

    private Context context;
    private List<String> imageUrls;
    private List<String> businessnametexts;
    private OnItemClickListener onItemClickListener;
    private boolean showAllItems;
    private boolean isShopFragment;
    private static SharedPreferences sharedPreferences;
    Boolean premium;

    public BannerAdapter(Context context, SharedPreferences sharedPreferences, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.imageUrls = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
        this.sharedPreferences = sharedPreferences;
    }

    // Add this method to update the list of image URLs
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        notifyDataSetChanged();
    }


    public void setShopFragment(boolean isShopFragment) {
        this.isShopFragment = isShopFragment;
        notifyDataSetChanged();
    }

    // Interface for item click events
    public interface OnItemClickListener {
        void onItemClick(int position, Boolean premium, String imageUrl) throws ExecutionException, InterruptedException;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BannerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_lists, parent, false);
        return new BannerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerviewHolder holder, @SuppressLint("RecyclerView") int position) {
        String imageUrl = imageUrls.get(position);

        // Use your preferred image loading library or method to load the image into the ImageView
        // For example, if using Picasso:
        // Picasso.get().load(imageUrl).into(holder.bannerImageView);
        // Or, if using Glide:
        Glide.with(context).load(imageUrl).centerCrop().into(holder.bannerImageView);

        // Make sure to include the necessary dependencies for Picasso or Glide in your app's build.gradle.

        // Set a click listener for the item
        holder.bannerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the activity or fragment about the click event
                if (onItemClickListener != null) {
                    try {
                        onItemClickListener.onItemClick(position,premium, imageUrl);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        hidelogo(position, holder.premiumimageview, holder.freeimageview);
    }

    @Override
    public int getItemCount() {
            return imageUrls != null ? imageUrls.size() : 0;

    }

    public class BannerviewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImageView, premiumimageview, freeimageview;


        public BannerviewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImageView = itemView.findViewById(R.id.bannerimages);
            premiumimageview = itemView.findViewById(R.id.premiumimageview);
            freeimageview = itemView.findViewById(R.id.freeimageview);


        }
    }

    private void hidelogo(int position, ImageView premiumimageview, ImageView freeimageview) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Shop");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = sharedPreferences.getString("mobilenumber", null);
        if (userId != null) {
            // Use the userId as needed
            System.out.println("dffvf  " + userId);
        }
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("premium");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                     premium = snapshot.child("premium").getValue(Boolean.class);
                    System.out.println("jsdvjdsn " +premium);

                    if (premium.equals(true)){
                        premiumimageview.setVisibility(View.GONE);
                        freeimageview.setVisibility(View.GONE);
                    } else{
                        premiumimageview.setVisibility(View.VISIBLE);
                        if (position<2){
                            freeimageview.setVisibility(View.VISIBLE);
                            premiumimageview.setVisibility(View.GONE);
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });

    }

}

package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class BannerPostAdapter extends RecyclerView.Adapter<BannerPostAdapter.BannerViewHolder> {
    private List<Post> postList;
    private Context context;
    private List<Shop> filteredShopList;
    private ClickListener clickListener;
    public static boolean isLiked = false;
    static String shopcontactNumber;
    public static int likeCount = 0;
    int promotioncount;

    public BannerPostAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(int position);
    }

    // Create a method to update the filtered list
    public void setFilteredList(List<Shop> filteredList) {
        this.filteredShopList = new ArrayList<>(filteredList); // Create a new instance of filtered list to avoid reference issues
    }


    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_post_lists, parent, false);
        return new BannerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Post shop = postList.get(position);
        holder.textViewShopName.setText(shop.getShopname());
        System.out.println("fgvdvfd " +shop.getShopname());
        shopcontactNumber = shop.getShopcontactNumber();


        Glide.with(context)
                .load(shop.getShopimage()).centerCrop()
                .placeholder(R.drawable.logo)
                .into(holder.circleImageView);

        // Load only the first image from the list
        if (!shop.getShoppostimages().isEmpty()) {
            String firstImageUrl = shop.getShoppostimages().get(position);
            Glide.with(context)
                    .load(firstImageUrl)
                    .into(holder.bannerpostimageview);
        }



    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {

        public ImageView circleImageView, bannerpostimageview, likeimageview;
        public TextView textViewShopName;

        public BannerViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.shopimageview);
            bannerpostimageview = itemView.findViewById(R.id.bannerpostimageview);
            textViewShopName = itemView.findViewById(R.id.shopnametextview);
            likeimageview = itemView.findViewById(R.id.likeimageview);

            likeimageview.setOnClickListener(v -> {
                // Toggle the like state
                isLiked = !isLiked;
                // Update the count based on the like state
                updateLikeCount();
                // Update the color of the likeImageView based on the state
                updateLikeImageView();
            });
        }

        // Method to update the count based on the like state
        // Method to update the count based on the like state
        private void updateLikeCount() {
            DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference().child("Shop").child(shopcontactNumber);

            shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        long currentCount = dataSnapshot.child("count").getValue(Long.class);

                        if (isLiked) {
                            // Increment the count if liked
                            currentCount++;
                        } else {
                            // Decrement the count if disliked
                            currentCount = Math.max(0, currentCount - 1);
                        }

                        // Update the count in the database
                        shopRef.child("count").setValue(currentCount);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors if needed
                }
            });
        }


        // Method to update the color of likeImageView based on the state
        private void updateLikeImageView() {
            if (isLiked) {
            likeimageview.setBackgroundResource(R.drawable.ic_baseline_thumb_up_24);
            } else {
                likeimageview.setBackgroundResource(R.drawable.ic_baseline_thumb_up_off_alt_24);
            }
        }


//        private void showFullImage(View anchorView, String imageUrl) {
//            // Check if imageUrl is not empty or null
//            if (imageUrl != null && !imageUrl.isEmpty()) {
//                // Inflate the popup layout
//                View popupView = LayoutInflater.from(context).inflate(R.layout.activity_full_screen, null);
//
//                // Find the ImageView in the popup layout
//                ImageView imageView = popupView.findViewById(R.id.popup_image_view);
//                ImageView cancelimageView = popupView.findViewById(R.id.close_image_view);
//
//                // Load the image into the ImageView using Glide
//                Glide.with(context)
//                        .load(imageUrl)
//                        .into(imageView);
//
//                // Calculate the width and height of the popup window
//                int width = LinearLayout.LayoutParams.MATCH_PARENT;
//                int height = LinearLayout.LayoutParams.MATCH_PARENT;
//
//                // Create the PopupWindow and set its properties
//                PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
//                popupWindow.setOutsideTouchable(true);
//                popupWindow.setFocusable(true);
//
//                // Show the popup window at the center of the anchor view
//                popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
//
//
//                cancelimageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Close the popup window when cancelimageview is clicked
//                        if (popupWindow != null && popupWindow.isShowing()) {
//                            popupWindow.dismiss();
//                        }
//                    }
//                });
//
//
//            } else {
//                // Show a toast message if imageUrl is empty or null
//                Toast.makeText(context, "Image not available", Toast.LENGTH_SHORT).show();
//            }
//        }

    }
}

package com.spark.swarajyabiz;


import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ThoughtsAdapter extends RecyclerView.Adapter<ThoughtsAdapter.BannerViewHolder> {

    private Context context;
    private List<String> imageUrls;
    private List<String> thoughtsnametexts;
    private OnThoughtClickListener onThoughtClickListener;
    private boolean showAllItems;
    private boolean isShopFragment;

    public ThoughtsAdapter(Context context, OnThoughtClickListener onThoughtClickListener, boolean showAllItems) {
        this.context = context;
        this.imageUrls = new ArrayList<>();
        this.onThoughtClickListener = onThoughtClickListener;
        this.showAllItems = showAllItems;
    }

    // Add this method to update the list of image URLs
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        notifyDataSetChanged();
    }

    public void setthoughtsnametexts(List<String> thoughtsnametexts) {
        this.thoughtsnametexts = thoughtsnametexts;
        notifyDataSetChanged();
    }

    public void setShopFragment(boolean isShopFragment) {
        this.isShopFragment = isShopFragment;
        notifyDataSetChanged();
    }

    // Interface for item click events
    public interface OnThoughtClickListener {
        void onThoughtClick(int position, String imageUrl, String thoughtsname) throws ExecutionException, InterruptedException;
    }

    public void setOnItemClickListener(OnThoughtClickListener onThoughtClickListener) {
        this.onThoughtClickListener = onThoughtClickListener;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.business_banner_list, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String imageUrl = imageUrls.get(position);

        // Check if thoughtsnametexts is not null and position is within bounds
        if (thoughtsnametexts != null && position >= 0 && position < thoughtsnametexts.size()) {
            String thoughtsname = thoughtsnametexts.get(position);
            // Use your preferred image loading library or method to load the image into the ImageView
            // For example, if using Picasso:
            // Picasso.get().load(imageUrl).into(holder.bannerImageView);
            // Or, if using Glide:
          //  Glide.with(context).load(imageUrl).centerCrop().into(holder.bannerImageView);

            Glide.with(context)
                    .load(imageUrl)
                    .transform(new CenterCrop(), new TopCropTransformation())
                    .into(holder.bannerImageView);


            holder.businesstextView.setText(thoughtsname);

            Log.d("BannerAdapter", "Position: " + position);
            Log.d("BannerAdapter", "Image URL: " + imageUrl);
            Log.d("BannerAdapter", "Business Name: " + thoughtsname);

            // Apply different sizes only for ShopFragment
            if (isShopFragment) {
                // Set ImageView size to 100dp
                ViewGroup.LayoutParams layoutParams = holder.cardView.getLayoutParams();
                layoutParams.width = 275;
                layoutParams.height = 298;
                holder.cardView.setLayoutParams(layoutParams);

                // Set TextView size to 10sp
                ViewGroup.LayoutParams layoutParam = holder.businesstextView.getLayoutParams();
                layoutParam.width = 275;
                holder.businesstextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                holder.favimageview.setVisibility(View.GONE);
            }
            holder.favimageview.setVisibility(View.GONE);
            // Set a click listener for the item
            holder.bannerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Notify the activity or fragment about the click event
                    if (onThoughtClickListener != null) {
                        try {
                            onThoughtClickListener.onThoughtClick(position, imageUrl, thoughtsname);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (showAllItems) {
            return imageUrls != null ? imageUrls.size() : 0;
        } else {
            // Display only the first 5 items for the ShopFragment
            return Math.min(imageUrls.size(), 5);
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImageView, favimageview;
        TextView businesstextView;
        CardView cardView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImageView = itemView.findViewById(R.id.businessimages);
            businesstextView= itemView.findViewById(R.id.businesstextviews);
            cardView = itemView.findViewById(R.id.businessimagecard);
            favimageview = itemView.findViewById(R.id.favimageview);
        }
    }
}


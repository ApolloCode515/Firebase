package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class BusinessBannerAdapter extends RecyclerView.Adapter<BusinessBannerAdapter.BannerViewHolder> {

    private Context context;
    private List<String> imageUrls;
    private List<String> businessnametexts;
    private OnItemClickListener onItemClickListener;
    private boolean showAllItems;
    private boolean isShopFragment;
    private List<RelativeLayout> relativeLayouts = new ArrayList<>();


    public BusinessBannerAdapter(Context context, OnItemClickListener onItemClickListener, boolean showAllItems) {
        this.context = context;
        this.imageUrls = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
        this.showAllItems = showAllItems;
    }

    // Add this method to update the list of image URLs
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        notifyDataSetChanged();
    }

    public void setBusinessnametexts(List<String> businessnametexts) {
        this.businessnametexts = businessnametexts;
        notifyDataSetChanged();
    }

    public void setShopFragment(boolean isShopFragment) {
        this.isShopFragment = isShopFragment;
        notifyDataSetChanged();
    }

    public void shuffleRelativeLayouts() {
        Collections.shuffle(relativeLayouts);
        notifyDataSetChanged();
    }


    // Interface for item click events
    public interface OnItemClickListener {
        void onItemClick(int position, String imageUrl, String businessname) throws ExecutionException, InterruptedException;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.business_banner_list, parent, false);
        RelativeLayout relativeLayout = view.findViewById(R.id.relativelayouts);
        relativeLayouts.add(relativeLayout);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String imageUrl = imageUrls.get(position);

        if (businessnametexts != null && position >= 0 && position < businessnametexts.size()) {
            String businessname = businessnametexts.get(position);
            // Use your preferred image loading library or method to load the image into the ImageView
            // For example, if using Picasso:
            // Picasso.get().load(imageUrl).into(holder.bannerImageView);
            // Or, if using Glide:
            Glide.with(context)
                    .load(imageUrl).centerCrop()
                    .into(holder.bannerImageView);

            holder.businesstextView.setText(businessname);

            Log.d("BannerAdapter", "Position: " + position);
            Log.d("BannerAdapter", "Image URL: " + imageUrl);
            Log.d("BannerAdapter", "Business Name: " + businessname);

            // Apply different sizes only for ShopFragment
            if (isShopFragment) {
                // Set ImageView size to 100dp
                ViewGroup.LayoutParams layoutParams = holder.cardView.getLayoutParams();
                layoutParams.width = 275;
                layoutParams.height = 300;
                holder.cardView.setLayoutParams(layoutParams);

                // Set TextView size to 10sp
                ViewGroup.LayoutParams layoutParam = holder.businesstextView.getLayoutParams();
                layoutParam.width = 275;
                holder.businesstextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);


            }

            // Make sure to include the necessary dependencies for Picasso or Glide in your app's build.gradle.

            // Set a click listener for the item
            holder.bannerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Notify the activity or fragment about the click event
                    if (onItemClickListener != null) {
                        try {
                            onItemClickListener.onItemClick(position, imageUrl, businessname);
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
        ImageView bannerImageView;
        TextView businesstextView;
        CardView cardView;
        RelativeLayout relativeLayout;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImageView = itemView.findViewById(R.id.businessimages);
            businesstextView= itemView.findViewById(R.id.businesstextviews);
            cardView = itemView.findViewById(R.id.businessimagecard);
            relativeLayout = itemView.findViewById(R.id.relativelayouts);
        }
    }
}

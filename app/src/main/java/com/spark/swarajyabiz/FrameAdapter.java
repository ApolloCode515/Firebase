package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.BannerViewHolder>{

    private Context context;
    private List<String> imageUrls;
    private OnFrameClickListener onFrameClickListener;
    private int selectedImagePosition = -1; // Initialize with an invalid position

    FrameAdapter(Context context, OnFrameClickListener onFrameClickListener){
        this.context = context;
        this.imageUrls = new ArrayList<>();
        this.onFrameClickListener = onFrameClickListener;
    }

    // Add this method to update the list of image URLs
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        notifyDataSetChanged();
    }

    public void setSelectedImagePosition(int position) {
        selectedImagePosition = position;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.frame_lists, parent, false);
        return new BannerViewHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String imageUrl = imageUrls.get(position);
        System.out.println("cxzjnzz " +imageUrls.toString());
        // Use Glide to load the image into a Drawable
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        // Set the loaded image as the background of the view
                        holder.framedesign.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle cleanup or placeholder if needed
                    }
                });



        // Set the click listener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the activity or fragment about the click event
                if (onFrameClickListener != null) {
                    try {
                        onFrameClickListener.onFrameClick(position, imageUrl);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Check if the current item is selected
        if (position == selectedImagePosition) {
            // Update the border or any other visual indication for the selected item
            GradientDrawable border = new GradientDrawable();
//        imageView.setImageResource(R.drawable.ic_baseline_blue_check_24);
            border.setColor(R.color.transparant); // Set transparent background
            border.setStroke(5, Color.BLACK); // Set border color and width
            holder.framedesign.setImageDrawable(border);

        } else {
                    GradientDrawable border = new GradientDrawable();
                    border.setStroke(4, Color.GRAY);
                    holder.framedesign.setImageDrawable(border);

        }

    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }



    public interface OnFrameClickListener {
        void onFrameClick(int position, String imageUrl)
                throws ExecutionException, InterruptedException;
    }


    public class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView framedesign;
        TextView businesstextView, currentdate;
        CardView cardView, currentdatecardview;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            framedesign = itemView.findViewById(R.id.framedesign);

        }
    }
}

package com.spark.swarajyabiz.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spark.swarajyabiz.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FestivalImagesAdapter extends RecyclerView.Adapter<FestivalImagesAdapter.ImageViewHolder> {
    private List<String>    imageUrls;
    Context context;
    OnClick onClick;

    public FestivalImagesAdapter(Context context, List<String> imageUrls, OnClick onClick) {
        this.context=context;
        this.imageUrls = imageUrls;
        this.onClick = onClick;
    }

    public interface OnClick{
        void onClick(String imageUrl, int poition);
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_lists, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String imageUrl = imageUrls.get(position);
        // Load image into ImageView using your preferred image loading library (e.g., Picasso, Glide)
        // For simplicity, assuming you are using an ImageView with id "imageView" in the item layout
        // Replace "R.id.imageView" with the actual id of your ImageView
        Glide.with(context).load(imageUrl).into(holder.imageView);

        System.out.println("y5rhfbx "+imageUrl);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(imageUrl, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bannerimages);
        }
    }
}

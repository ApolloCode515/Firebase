package com.spark.swarajyabiz;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.slider.Slider;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.sliderViewHolder> {

    List<SlideImage> slideImages;
    ViewPager2 viewPager2;
    private Context context;

    public SliderAdapter(List<SlideImage> slideImages, ViewPager2 viewPager2, Context context) {
        this.slideImages = slideImages;
        this.viewPager2 = viewPager2;
        this.context = context;
    }

    @NonNull
    @Override
    public sliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new sliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull sliderViewHolder holder, int position) {
        String imageUrl = slideImages.get(position).getImageUrl();

        // Load image using Glide or any other image loading library
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.imageView);
        }

        // Set the price in the text view based on the current item position
        String price = slideImages.get(position).getPrice();
        setPriceTextView(price);
    }

    @Override
    public int getItemCount() {
        return slideImages.size();
    }

    class sliderViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView imageView;

        sliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageslide);
        }
    }

    // Add a method to update the text view in the adapter
    public void setPriceTextView(String price) {
        // Implement this method to update your text view with the given price
        // For example, if you have a TextView named priceTextView:
        // priceTextView.setText(price);
    }
}

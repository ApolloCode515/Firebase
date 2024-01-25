package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class IntrestAdapter extends RecyclerView.Adapter<IntrestAdapter.ViewHolder> {

    private Context context;
    private List<IntrestClass> intrestList;

    OnImgClickListener onImgClickListener;

    public IntrestAdapter(Context context, List<IntrestClass> intrestList, OnImgClickListener onImgClickListener) {
        this.context = context;
        this.intrestList = intrestList;
        this.onImgClickListener = onImgClickListener;
    }

    interface OnImgClickListener{
        void ImgClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.intrest_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        IntrestClass intrestItem = intrestList.get(position);

        // Load shop image using Glide or your preferred image loading library
        Glide.with(context)
                .load(intrestItem.getShopImage())
                .placeholder(R.drawable.bklogo1) // Placeholder image if needed
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, ShopDetails.class);
//                intent.putExtra("contactNumber", intrestItem.getShopContactNumber());
//                context.startActivity(intent);
                onImgClickListener.ImgClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return intrestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.shopImg); // Replace with the actual image view ID in your item layout
        }
    }
}

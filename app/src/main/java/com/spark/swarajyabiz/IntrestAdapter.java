package com.spark.swarajyabiz;

import android.content.Context;
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

    public IntrestAdapter(Context context, List<IntrestClass> intrestList) {
        this.context = context;
        this.intrestList = intrestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.intrest_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IntrestClass intrestItem = intrestList.get(position);

        // Load shop image using Glide or your preferred image loading library
        Glide.with(context)
                .load(intrestItem.getShopImage())
                .placeholder(R.drawable.bklogo1) // Placeholder image if needed
                .into(holder.imageView);
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

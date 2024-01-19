package com.spark.swarajyabiz;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;


public class BusiPostAdapter extends RecyclerView.Adapter<BusiPostAdapter.PostViewHolder> {

    private List<BusinessPost> businessPosts;
    private OnClickListener onClickListener;

    public BusiPostAdapter(List<BusinessPost> businessPosts, OnClickListener onClickListener) {
        this.businessPosts = businessPosts;
        this.onClickListener = onClickListener;
    }

    interface OnClickListener {
        void onClick(int position, View view, String postkey);
    }


    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productimagelist, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BusinessPost post = businessPosts.get(position);
        holder.bind(post);


    }


    @Override
    public int getItemCount() {
        return businessPosts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, shopimageview, deleteimageview;
        TextView postDesc, shopnametextview, bizaddress, clickCount, viewcount, pendingtext;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image_view);

        }

        public void bind(BusinessPost post) {
            // Load image using your preferred image loading library (e.g., Picasso, Glide)
            // Here, assuming you have a method loadImage using Picasso:
                Picasso.get().load(post.getPostImg()).into(imageView);

        }
    }
}

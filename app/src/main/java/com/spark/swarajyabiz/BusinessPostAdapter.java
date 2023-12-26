package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BusinessPostAdapter extends RecyclerView.Adapter<BusinessPostAdapter.PostViewHolder> {

    private List<BusinessPost> businessPosts;
    private OnClickListener onClickListener;

    public BusinessPostAdapter(List<BusinessPost> businessPosts, OnClickListener onClickListener) {
        this.businessPosts = businessPosts;
        this.onClickListener = onClickListener;
    }

    interface OnClickListener {
        void onClick(int position, View view, String postkey);
    }
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_business_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BusinessPost post = businessPosts.get(position);
        holder.bind(post);
        String  postkey = post.getPostkey();
        holder.deleteimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(position, holder.deleteimageview,postkey);
            }
        });

    }

    @Override
    public int getItemCount() {
        return businessPosts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, shopimageview, deleteimageview;
        TextView textView, shopnametextview;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.postimageview);
            textView = itemView.findViewById(R.id.postcaption);
            shopimageview = itemView.findViewById(R.id.shopimageview);
            shopnametextview = itemView.findViewById(R.id.shopnametextview);
            deleteimageview = itemView.findViewById(R.id.deleteimageview);
        }

        public void bind(BusinessPost post) {
            // Load image using your preferred image loading library (e.g., Picasso, Glide)
            // Here, assuming you have a method loadImage using Picasso:
            Picasso.get().load(post.getImageUrl()).into(imageView);
            Picasso.get().load(post.getShopImage()).into(shopimageview);
            textView.setText(post.getText());
            shopnametextview.setText(post.getShopName());

        }
    }
}

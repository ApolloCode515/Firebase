package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
        String  postkey = post.getPostID();
        holder.deleteimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(position, holder.deleteimageview,postkey);
            }
        });

        String postStatus = post.getStatus();
        if ("Posted".equalsIgnoreCase(postStatus)) {
            holder.pendingtext.setBackgroundResource(R.color.blue);
            holder.pendingtext.setText("Posted");
        } else if ("Rejected".equalsIgnoreCase(postStatus)) {
            holder.pendingtext.setBackgroundResource(R.color.close_red);
            holder.pendingtext.setText("Rejected");
        } else {
            // Handle other status conditions if needed
        }

        if(post.getPostType().equals("Image")) {

            holder.postDesc.setVisibility(View.GONE);

            holder.imageView.setVisibility(View.VISIBLE);

        }else if (post.getPostType().equals("Text")) {

            holder.postDesc.setVisibility(View.VISIBLE);

            holder.imageView.setVisibility(View.GONE);

            } else if (post.getPostType().equals("Both")) {
            holder.postDesc.setVisibility(View.VISIBLE);

            holder.imageView.setVisibility(View.VISIBLE);
            }
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
            imageView = itemView.findViewById(R.id.postImg);
            postDesc = itemView.findViewById(R.id.postde);
            shopimageview = itemView.findViewById(R.id.userImg);
            shopnametextview = itemView.findViewById(R.id.bizname);
            deleteimageview = itemView.findViewById(R.id.deleteimageview);
            bizaddress = itemView.findViewById(R.id.bizadd);
            clickCount = itemView.findViewById(R.id.clickcount);
            viewcount = itemView.findViewById(R.id.viewcount);
            pendingtext = itemView.findViewById(R.id.pendingtext);
        }

        public void bind(BusinessPost post) {
            // Load image using your preferred image loading library (e.g., Picasso, Glide)
            // Here, assuming you have a method loadImage using Picasso:
            Picasso.get().load(post.getPostImg()).into(imageView);
            Picasso.get().load(post.getBizImg()).into(shopimageview);
            postDesc.setText(post.getPostDesc());
            shopnametextview.setText(post.getPostUser());
            bizaddress.setText(post.getPostAdd());
            clickCount.setText(post.getClickCount());
            viewcount.setText(post.getViewCount());


        }
    }
}

package com.spark.swarajyabiz.Adapters;

import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.DataFetcher;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.spark.swarajyabiz.ItemDetails;
import com.spark.swarajyabiz.ModelClasses.OrderModel;
import com.spark.swarajyabiz.ModelClasses.PostModel;
import com.spark.swarajyabiz.R;

import java.util.List;

public class HomeMultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> itemList;
    private static final int POST_ITEM = 1;
    private static final int PRODUCT_ITEM = 2;
    private static OnViewDetailsClickListener onViewDetailsClickListener;

    public HomeMultiAdapter(List<Object> itemList, OnViewDetailsClickListener listener) {
        this.itemList = itemList;
        this.onViewDetailsClickListener = listener;
    }

    public void setOnViewDetailsClickListener() {

    }

    public interface OnViewDetailsClickListener {
        void onViewDetailsClick(int position);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType){
            case POST_ITEM:
                view=inflater.inflate(R.layout.postlayout,parent,false);
                return new PostItemViewHolder(view);
            case PRODUCT_ITEM:
                view = inflater.inflate(R.layout.productlist, parent, false);
                return new OrderItemViewHolder(view);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case POST_ITEM:
                ((PostItemViewHolder) holder).bind((PostModel) itemList.get(position));
                break;
            case PRODUCT_ITEM:
                ((OrderItemViewHolder) holder).bind((OrderModel) itemList.get(position));
                break;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object item=itemList.get(position);
        if(item instanceof PostModel){
            return POST_ITEM;
        }else if(item instanceof OrderModel){
            return PRODUCT_ITEM;
        }else {
            throw new IllegalArgumentException("Invalid data type at position " + position);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class PostItemViewHolder extends RecyclerView.ViewHolder {
        TextView uname,uadd,postDesc;
        de.hdodenhof.circleimageview.CircleImageView profImg;
        ImageView postImg;


        public PostItemViewHolder(@NonNull View itemView) {
            super(itemView);
            uname=itemView.findViewById(R.id.bizname);
            uadd=itemView.findViewById(R.id.bizadd);
            postDesc=itemView.findViewById(R.id.postde);
            profImg=itemView.findViewById(R.id.userImg);
            postImg=itemView.findViewById(R.id.postImg);
        }

          public void bind(PostModel postModel){
            uname.setText(postModel.getPostUser());
            uadd.setText(postModel.getUserAdd());
            postDesc.setText(postModel.getPostDesc());
            Glide.with(itemView.getContext()).load(postModel.getPostImg()).into(postImg);
            Glide.with(itemView.getContext()).load(postModel.getUserImg()).into(profImg);
            if(postModel.getPostType().equals("Image")){
                postDesc.setVisibility(View.GONE);
                postImg.setVisibility(View.VISIBLE);
            }else if (postModel.getPostType().equals("Text")){
                postDesc.setVisibility(View.VISIBLE);
                postImg.setVisibility(View.GONE);
            } else if (postModel.getPostType().equals("Both")){
                postDesc.setVisibility(View.VISIBLE);
                postImg.setVisibility(View.VISIBLE);
            }
        }

    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView prodName,rating,crossRate,showrate,offer;
        RatingBar rateStar;
        ImageView prodImg;
        ShimmerTextView proTag;
        CardView viewdetails;


        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            prodName=itemView.findViewById(R.id.proDesc);
            rating=itemView.findViewById(R.id.rating);
            crossRate=itemView.findViewById(R.id.crossRate);
            showrate=itemView.findViewById(R.id.showRate);
            offer=itemView.findViewById(R.id.offer);
            rateStar=itemView.findViewById(R.id.ratingBar);
            prodImg=itemView.findViewById(R.id.prodImg);
            proTag=itemView.findViewById(R.id.proTag);
            viewdetails = itemView.findViewById(R.id.detailsCard);

            viewdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onViewDetailsClickListener != null) {
                        onViewDetailsClickListener.onViewDetailsClick(getAdapterPosition());
                    }
                }
            });
        }

        // Interface to handle click events


        // Method to set the click listener


        public void bind(OrderModel orderModel){
            prodName.setText(orderModel.getProdName());
            rating.setText(orderModel.getRating());
            crossRate.setText(orderModel.getProprice());
            showrate.setText(orderModel.getProsell());
            offer.setText(orderModel.getOffer());

            Glide.with(itemView.getContext()).load(orderModel.getProImg()).into(prodImg);

            Shimmer shimmer = new Shimmer();
            shimmer.start(proTag);

//            float ratingValue = Float.parseFloat(orderModel.getRating());
//            rateStar.setRating(ratingValue);



        }
    }


}

package com.spark.swarajyabiz.Adapters;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.spark.swarajyabiz.ItemDetails;
import com.spark.swarajyabiz.JobDetails;
import com.spark.swarajyabiz.ModelClasses.OrderModel;
import com.spark.swarajyabiz.ModelClasses.PostModel;
import com.spark.swarajyabiz.R;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
        void onPostClick(int position);
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

    public void shuffleItems() {
        Collections.shuffle(itemList);
        notifyDataSetChanged();
    }

    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }
    public void setData(List<Object> newData) {
        this.itemList.clear();
        this.itemList.addAll(newData);
        notifyDataSetChanged();
    }


    public static class PostItemViewHolder extends RecyclerView.ViewHolder {
        TextView uname,uadd,postDesc, viewCount, clickcount;
        de.hdodenhof.circleimageview.CircleImageView profImg;
        ImageView postImg, verifyimg;
        CardView cardView;
        private PostModel postModel;
        ShimmerTextView proTextView;

        public PostItemViewHolder(@NonNull View itemView) {
            super(itemView);
            uname=itemView.findViewById(R.id.bizname);
            uadd=itemView.findViewById(R.id.bizadd);
            postDesc=itemView.findViewById(R.id.postde);
            profImg=itemView.findViewById(R.id.userImg);
            postImg=itemView.findViewById(R.id.postImg);
            cardView=itemView.findViewById(R.id.postcardview);
            verifyimg = itemView.findViewById(R.id.verifyimg);
            viewCount = itemView.findViewById(R.id.viewcount);
            clickcount = itemView.findViewById(R.id.clickcount);
            proTextView = itemView.findViewById(R.id.proTags);
        }

          public void bind(PostModel postModel){
              this.postModel = postModel;

            uname.setText(postModel.getPostUser());
            uadd.setText(postModel.getUserAdd());
            postDesc.setText(postModel.getPostDesc());
            int clickCount = Integer.parseInt(postModel.getPostclickCount());
              clickcount.setText(formatClickCount(clickCount) + " Clicks");

            int viewcount = Integer.parseInt(postModel.getPostvisibilityCount());
              viewCount.setText(formatViewCount(viewcount)+ " Views");

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

              String postcate = postModel.getPostCate();
              String[] parts = postcate.split("&&");
              proTextView.setText(parts[0]);
              proTextView.setTextColor(Color.parseColor("#FFFFFF"));
              proTextView.setReflectionColor(Color.parseColor("#9C27B0"));
              Shimmer shimmer = new Shimmer();
              shimmer.start(proTextView);


              cardView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      if (onViewDetailsClickListener != null) {
                          onViewDetailsClickListener.onPostClick(getAdapterPosition());

//                          String currentVisibilityCount = postModel.getPostvisibilityCount();
//                          int currentcount = Integer.parseInt(currentVisibilityCount);
//                          postModel.setPostvisibilityCount(String.valueOf(currentcount + 1));
//                          viewCount.setText(String.valueOf(postModel.getPostvisibilityCount()));

                      }
                  }
              });

              if (!postModel.isVisibilityCountUpdated()) {
                  // Increment the visibility count when the post is clicked
                  int currentcount = Integer.parseInt(postModel.getPostvisibilityCount());
                  postModel.setPostvisibilityCount(String.valueOf(currentcount + 1));
                //  viewCount.setText(postModel.getPostvisibilityCount());

                  // Update the flag to indicate that the visibility count has been updated
                  postModel.setVisibilityCountUpdated(true);
                  updatePostVisibilityCount(postModel.getPostId(), postModel.getPostcontactKey());
              }


              String status = postModel.getPostStatus();
            if (status.equals("Posted")){
                verifyimg.setVisibility(View.VISIBLE);
            }else {
                verifyimg.setVisibility(View.GONE);
            }


          }

        public static String formatViewCount(int viewCount) {
            if (viewCount < 1000) {
                // If the view count is less than 1000, simply return the original count
                return String.valueOf(viewCount);
            } else if (viewCount < 1000000) {
                // If the view count is between 1000 and 999,999, format it as "x.yk"
                return String.format(Locale.getDefault(), "%.1fk", viewCount / 1000.0);
            } else {
                // If the view count is a million or more, format it as "xm"
                return String.format(Locale.getDefault(), "%dm", viewCount / 1000000);
            }
        }

        public static String formatClickCount(int viewCount) {
            if (viewCount < 1000) {
                // If the view count is less than 1000, simply return the original count
                return String.valueOf(viewCount);
            } else if (viewCount < 1000000) {
                // If the view count is between 1000 and 999,999, format it as "x.yk"
                return String.format(Locale.getDefault(), "%.1fk", viewCount / 1000.0);
            } else {
                // If the view count is a million or more, format it as "xm"
                return String.format(Locale.getDefault(), "%dm", viewCount / 1000000);
            }
        }


        public void resetVisibilityCountFlag() {
            if (postModel != null) {
                postModel.resetVisibilityCountUpdated();
            }
        }

        private void updatePostVisibilityCount(String postId, String postContactNumber) {
            DatabaseReference postVisibilityRef = FirebaseDatabase.getInstance()
                    .getReference("BusinessPosts").child(postContactNumber)
                    .child(postId)
                    .child("visibilityCount");

            // Retrieve the current count from Firebase
            postVisibilityRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String currentCount = snapshot.getValue(String.class);

                    int currentcount = Integer.parseInt(currentCount);
                    postVisibilityRef.setValue(String.valueOf(currentcount + 1));
                    // Increment the count and update it in Firebase
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });
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
            crossRate.setText("₹ "+orderModel.getProprice()+".00");
            // Assuming orderModel.getProsell() returns a string like "₹ 76.00"

            // Assuming orderModel.getProsell() returns a string like "₹ 76.00"
            String proSellString = orderModel.getProsell();
            String numericPart = proSellString
                    .replaceAll("[^0-9.]+", "");  // Remove non-numeric characters except the dot
            numericPart = numericPart.replaceAll("\\.0*$", "");
            if (numericPart.endsWith(".")) {
                numericPart = numericPart.substring(0, numericPart.length() - 1);
            }

// Display the result, for example in a TextView
            showrate.setText("₹ " + numericPart);



            offer.setText("("+orderModel.getOffer()+" off)");
            crossRate.setPaintFlags(showrate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            Glide.with(itemView.getContext()).load(orderModel.getProImg()).into(prodImg);

            Shimmer shimmer = new Shimmer();
            shimmer.start(proTag);

//            float ratingValue = Float.parseFloat(orderModel.getRating());
//            rateStar.setRating(ratingValue);



        }
    }


}

package com.spark.swarajyabiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewList;

    // Constructor to initialize the adapter with a list of reviews
    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        // Bind data to the ViewHolder
        Review review = reviewList.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    // ViewHolder class for holding the views of each item
    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView ratingTextView, reviewTextView, dateTextView, userNoTextView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views from the item layout
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            reviewTextView = itemView.findViewById(R.id.reviewTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            userNoTextView = itemView.findViewById(R.id.userNoTextView);
        }

        // Bind data to the views
        public void bind(Review review) {
            ratingTextView.setText("Rating: " + review.getRating());
            reviewTextView.setText("Review: " + review.getReview());
            dateTextView.setText("Date: " + review.getDate());
            userNoTextView.setText("User Number: " + review.getUserNo());
        }
    }
}

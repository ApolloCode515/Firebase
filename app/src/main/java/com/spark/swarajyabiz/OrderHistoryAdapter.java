package com.spark.swarajyabiz;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private List<Order> orderList;


    // Constructor to initialize the adapter with the order data
    public OrderHistoryAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout here (e.g., R.layout.order_history_item)
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ordered_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to your ViewHolder's views here
        Order order = orderList.get(position);
        holder.itemNameTextView.setText(order.getItemName());
//        holder.quantityTextView.setText("Quantity: " + order.getQuantity());
        // Add more bindings for other views as needed

        // Load and display the image using Glide
        Glide.with(holder.itemView.getContext())
                .load(order.getFirstImageUrl())
                .into(holder.itemimage);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView quantityTextView;
        TextView intrestedTextView;
        TextView Textview;
        ImageView itemimage;

                // Add references to other views in your item layout

        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.ItemName);
            intrestedTextView = itemView.findViewById(R.id.intrestedtextview);
            quantityTextView = itemView.findViewById(R.id.quantityedittext);
            itemimage = itemView.findViewById(R.id.itemimage);
            //button1 = findViewById(R.id.button1);
        }
    }
}


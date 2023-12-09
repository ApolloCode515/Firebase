package com.spark.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
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

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder>{

    private List<Order> orderList;
    private OrderClickListener orderClickListener;
    Context context;

    // Constructor to initialize the adapter with the order data
    public MyOrderAdapter(List<Order> orderList, OrderClickListener orderClickListener, Context context) {
        this.orderList = orderList;
        this.orderClickListener = orderClickListener;
        this.context = context;
    }

    public interface OrderClickListener {
        void onRemoveClick(int position);
        void onMessageClick(int position);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout here (e.g., R.layout.order_history_item)
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myorder_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Bind data to your ViewHolder's views here
        Order order = orderList.get(position);
        holder.itemNameTextView.setText(order.getItemName());
        holder.quantityTextView.setText( order.getQuantity());
        holder.NameTextView.setText(order.getBuyerName());
        holder.dateTextView.setText(order.getDatetamp().toString());
        String imageUrl = order.getFirstImageUrl();

        holder.removeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderClickListener.onRemoveClick(position);
            }
        });

        holder.itemimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullImage(v, imageUrl);
            }
        });

        holder.messagesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderClickListener.onMessageClick(position);
            }
        });

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
        TextView NameTextView;
        TextView dateTextView;
        ImageView removeImageView;
        ImageView itemimage;
        Button messagesbtn;

        // Add references to other views in your item layout

        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.myorder_item_Name);
           NameTextView = itemView.findViewById(R.id.Myorder_name);
            quantityTextView = itemView.findViewById(R.id.myorder_Quantity_number);
            dateTextView = itemView.findViewById(R.id.myorder_textView_Date);
            itemimage = itemView.findViewById(R.id.myorder_itemimage);
            removeImageView = itemView.findViewById(R.id.remove);
            messagesbtn = itemView.findViewById(R.id.contact);
            //button1 = findViewById(R.id.button1);
        }
    }

    private void showFullImage(View anchorView, String imageUrl) {
        // Check if imageUrl is not empty or null
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Inflate the popup layout
            View popupView = LayoutInflater.from(context).inflate(R.layout.activity_full_screen, null);

            // Find the ImageView in the popup layout
            ImageView imageView = popupView.findViewById(R.id.popup_image_view);

            // Load the image into the ImageView using Glide
            Glide.with(context)
                    .load(imageUrl)
                    .into(imageView);

            // Calculate the width and height of the popup window
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;

            // Create the PopupWindow and set its properties
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

            // Show the popup window at the center of the anchor view
            popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
        } else {
            // Show a toast message if imageUrl is empty or null
            Toast.makeText(context, "Image not available", Toast.LENGTH_SHORT).show();
        }
    }


}


package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private List<orders> ordersList;
    private OrderClickListener orderClickListener;

    public OrdersAdapter(List<orders> ordersList, OrderClickListener orderClickListener) {
        this.ordersList = ordersList;
        this.orderClickListener = orderClickListener;
    }

    public interface OrderClickListener {
        void onRemoveClick(int position);
        void onContactClick(int position);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your layout for each item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list, parent, false);
        return new OrderViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        orders order = ordersList.get(position);

        holder.buyerNameTextView.setText(order.getBuyerName());
        holder.contactNumberTextView.setText(order.getBuyerContactNumber());
        holder.itemNamesTextView.setText(order.getItemName());
        holder.totalamttextview.setText(order.getTotalAmt());

        Log.d("TAG", "tdhgbvc " +order.getStatus());
//        if (order.getDatetamp().toString().trim() !=null) {
//            holder.dateTextView.setText(order.getDatetamp().toString());
//        }

        if (!("").equals(order.getDatetamp().toString().trim())) {
            holder.dateTextView.setText(order.getDatetamp().toString());
        }

        holder.timeTextView.setText(order.getQuantity());

        if (("Approved").equals(order.getStatus())) {
            holder.rejectCard.setVisibility(View.GONE);

            holder.approvebtntxt.setText("Go to Chat");

            holder.buyerNameTextView.setTextColor(Color.WHITE); // Set text color to white
            // Assuming 'holder.header' is your RelativeLayout
            holder.header.setBackgroundColor(Color.parseColor("#2F5107"));
        }

        holder.rejectCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderClickListener.onRemoveClick(position);
            }
        });

        holder.contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderClickListener.onContactClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView buyerNameTextView,approvebtntxt;
        TextView contactNumberTextView;
        TextView itemNamesTextView, totalamttextview;
        TextView dateTextView;
        TextView timeTextView;
        ImageView removeImageView;
        CardView contactButton, rejectCard;
        RelativeLayout header;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            buyerNameTextView = itemView.findViewById(R.id.buyer_name);
            contactNumberTextView = itemView.findViewById(R.id.contact_number);
            itemNamesTextView = itemView.findViewById(R.id.item_Name);
            dateTextView = itemView.findViewById(R.id.textView_Date);
            timeTextView = itemView.findViewById(R.id.TextView_Time);
            removeImageView = itemView.findViewById(R.id.remove);
            contactButton = itemView.findViewById(R.id.contact);
            totalamttextview = itemView.findViewById(R.id.myorder_Amt);
            rejectCard = itemView.findViewById(R.id.rejectcard);
            header = itemView.findViewById(R.id.header);
            approvebtntxt = itemView.findViewById(R.id.approveBtnTxt);
        }
    }
}


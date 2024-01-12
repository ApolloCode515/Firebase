package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.OrderViewHolder>{

    private List<orders> ordersList;
    private OrderClickListener orderClickListener;

    public HistoryAdapter(List<orders> ordersList, OrderClickListener orderClickListener) {
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

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        orders order = ordersList.get(position);

        holder.buyerNameTextView.setText(order.getBuyerName());
        holder.contactNumberTextView.setText(order.getBuyerContactNumber());
        holder.itemNamesTextView.setText(order.getItemName());
        Log.d("TAG", "onBindViewHolder: " +order.getTimestamp());
        holder.dateTextView.setText(order.getDatetamp().toString());
        holder.timeTextView.setText(order.getQuantity());
        holder.totalAmttextview.setText(order.getTotalAmt());

        holder.removeImageView.setOnClickListener(new View.OnClickListener() {
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
        TextView buyerNameTextView;
        TextView contactNumberTextView;
        TextView itemNamesTextView, totalAmttextview;
        TextView dateTextView;
        TextView timeTextView;
        ImageView removeImageView;
        CardView contactButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            buyerNameTextView = itemView.findViewById(R.id.buyer_name);
            contactNumberTextView = itemView.findViewById(R.id.contact_number);
            itemNamesTextView = itemView.findViewById(R.id.item_Name);
            dateTextView = itemView.findViewById(R.id.textView_Date);
            timeTextView = itemView.findViewById(R.id.TextView_Time);
            removeImageView = itemView.findViewById(R.id.remove);
            contactButton = itemView.findViewById(R.id.contact);
            totalAmttextview = itemView.findViewById(R.id.myorder_Amt);

        }
    }
}





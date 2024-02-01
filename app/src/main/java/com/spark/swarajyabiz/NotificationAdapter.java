package com.spark.swarajyabiz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> notificationList;
    private Context context;

    public NotificationAdapter(List<Notification> notificationList, Context context) {
        this.notificationList =notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.notificationTextView.setText(notification.getMessage());
      //  holder.calltextview.setText(notification.getContactNumber());


        System.out.println("dfbfh " +notification.getComm());
        if (notification.getKey() != null)
        {
            if (notification.getComm()!=null){
                holder.calltextview.setVisibility(View.GONE);
                holder.callimageview.setVisibility(View.GONE);
            }else {
                holder.calltextview.setVisibility(View.VISIBLE);
                holder.callimageview.setVisibility(View.VISIBLE);
                holder.callimageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phoneNumber = notification.getKey();
                        openDialerWithPhoneNumber(phoneNumber);
                    }
                });
            }


        }else {
            holder.callimageview.setVisibility(View.GONE);
            holder.calltextview.setVisibility(View.GONE);
        }

      //  System.out.println("rgfsxc "+notification.getKey());

        if (notification.getOrder() != null){
            holder.orderImageview.setVisibility(View.VISIBLE);
            holder.orderImageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String isOrder = notification.getOrder();
                    if (isOrder != null) {
                        // Condition is true, so navigate to the order list page
                        Intent intent = new Intent(context, OrderLists.class);
                        intent.putExtra("contactNumber", notification.getContactNumber());

                        context.startActivity(intent);
                    } else {
                        // Handle the case where getorder() is not true
                        // You can display a message or perform some other action
                    }
                }
            });
        } else {
            holder.orderImageview.setVisibility(View.GONE);
            holder.calltextview.setVisibility(View.GONE);

        }

        if (notification.getShopNumber() != null){
            holder.promoteImageView.setVisibility(View.VISIBLE);
            holder.promoteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String shopNumber = notification.getShopNumber();
                    if (shopNumber != null) {
                        // Condition is true, so navigate to the order list page
                        Intent intent = new Intent(context, MyShopPromoted.class);
                        intent.putExtra("contactNumber", shopNumber);
                        context.startActivity(intent);
                    } else {
                        // Handle the case where getorder() is not true
                        // You can display a message or perform some other action
                    }
                }
            });
        } else {
            holder.promoteImageView.setVisibility(View.GONE);
            holder.calltextview.setVisibility(View.GONE);

        }


        // Adjust the layout for right or left-aligned messages
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTextView, calltextview;
        ImageView callimageview, orderImageview, promoteImageView;
        CardView notificationcardview;



        public ViewHolder(View itemView) {
            super(itemView);
            notificationTextView = itemView.findViewById(R.id.notification);
            callimageview = itemView.findViewById(R.id.callImageView);
            calltextview = itemView.findViewById(R.id.phoneNumberTextView);
            notificationcardview = itemView.findViewById(R.id.notificationcardview);
            orderImageview = itemView.findViewById(R.id.orderImageView);
            promoteImageView = itemView.findViewById(R.id.promoteImageView);
        }

    }
    public void openDialerWithPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            // Handle the case where the dialer app is not available
           // Toast.makeText(NotificationAdapter.this, "Dialer app not found.", Toast.LENGTH_SHORT).show();
        }
    }

}


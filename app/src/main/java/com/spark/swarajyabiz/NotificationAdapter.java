package com.spark.swarajyabiz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> notificationList;
    private Context context;
    private static SharedPreferences sharedPreferences;
    Boolean premium;

    public NotificationAdapter(List<Notification> notificationList, Context context, SharedPreferences sharedPreferences, boolean premium) {
        this.notificationList =notificationList;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.premium = premium;
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

        String userId = sharedPreferences.getString("mobilenumber", null);
        if (userId != null) {
            // Use the userId as needed
            System.out.println("dffvf  " + userId);
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                     premium = snapshot.child("premium").getValue(Boolean.class);
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        System.out.println("dfbfh " +notification.getComm());
        if (notification.getKey() != null)
        {
            if (notification.getComm()!=null){
                holder.calltextview.setVisibility(View.GONE);
                holder.callimageview.setVisibility(View.GONE);
            }else {
                if (premium != null && premium) {
                    System.out.println("wsdv " +premium);
                    holder.calltextview.setVisibility(View.VISIBLE);
                    holder.callimageview.setVisibility(View.VISIBLE);
                    holder.callimageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String phoneNumber = notification.getKey();
                            openDialerWithPhoneNumber(phoneNumber);
                        }
                    });
                } else {
                    holder.callimageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          showImageSelectionDialog();
                        }
                    });
                }

            }


        }else {
            holder.callimageview.setVisibility(View.GONE);
            holder.calltextview.setVisibility(View.GONE);
        }

      //  System.out.println("rgfsxc "+notification.getKey());

        if(notification.getJobKey()!=null){
            String details = notification.getJobKey();
            String[] splitdetails = details.split("&&");
            System.out.println("sedvxc " + Arrays.toString(splitdetails));
            holder.notificationcardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JobChat.class);
                    intent.putExtra("candidateName", splitdetails[2]);
                    intent.putExtra("jobtitle", splitdetails[1]);
                    intent.putExtra("UserContactNum", splitdetails[3]);
                    intent.putExtra("BusiContactNum", splitdetails[4]);
                    intent.putExtra("jobID", splitdetails[0]);
                    context.startActivity(intent);

                }
            });
        }

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

    private void showImageSelectionDialog() {
        Dialog dialog = new Dialog(context);

        // Inflate the custom layout
        dialog.setContentView(R.layout.custom_alert_dialog);


        // Find views in the custom layout
        ImageView alertImageView = dialog.findViewById(R.id.alertImageView);
        TextView alertTitle = dialog.findViewById(R.id.alertTitle);
        TextView alertMessage = dialog.findViewById(R.id.alertMessage);
        Button positiveButton = dialog.findViewById(R.id.positiveButton);

        // Customize the views as needed
        Glide.with(context).asGif().load(R.drawable.gif3).into(alertImageView); // Replace with your image resource
        alertTitle.setText("प्रीमियम");
        alertMessage.setText("नंबर पाहण्यासाठी प्रीमियम प्लान असणे गरजेचे आहे.\n" +
                "प्रीमियम प्लॅनसाठी खाली क्लिक करा.\n");

        // Set positive button click listener
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PremiumMembership.class);
                context.startActivity(intent);
                // dialog.dismiss(); // Dismiss the dialog after the button click
            }
        });

        // Create and show the dialog
        dialog.show();
    }

}


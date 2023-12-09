package com.spark.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {
    private List<Shop> shopList;
    private Context context;
    private List<Shop> filteredShopList;
    private ClickListener clickListener;
    int promotioncount;

    public ShopAdapter(List<Shop> shopList, Context context, ClickListener clickListener) {
        this.shopList = shopList;
        this.context = context;
        this.filteredShopList = new ArrayList<>(shopList);
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(int position);
    }

    public void shuffleData() {
        Collections.shuffle(shopList);
        Collections.shuffle(filteredShopList);
        notifyDataSetChanged();
    }

    // Create a method to update the filtered list
    public void setFilteredList(List<Shop> filteredList) {
        this.filteredShopList = new ArrayList<>(filteredList); // Create a new instance of filtered list to avoid reference issues
    }

    public void addItem(Shop shop, String imageUrl) {
        shopList.add(shop);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop, parent, false);
        return new ShopViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Shop shop = shopList.get(position);
        holder.textViewName.setText(shop.getName());
        holder.textViewShopName.setText(shop.getShopName());
        holder.textViewAddress.setText(shop.getAddress());
        holder.textViewcontactNumber.setText(shop.getContactNumber());
        holder.textViewdistrict.setText(shop.getDistrict());
        holder.textViewtaluka.setText(shop.getTaluka());
        String imageUrl = shop.getUrl();
        holder.bind(shop, context);
       /* String imageurl = null;
        imageurl= shop.getUrl();
        Picasso.get().load(imageurl).into(holder.circleImageView);
*/


        Glide.with(holder.circleImageView.getContext())
                .load(shop.getUrl()).centerCrop()
                .placeholder(R.drawable.logo)
                .into(holder.circleImageView);



        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullImage(v,imageUrl);
            }
        });


        holder.promote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the promotion count from the shop
                int promotionCount = shop.getpromotionCount();

                if (promotionCount > 0) {
                    // Create an Intent to open the MyShopPromoted activity
                    String contactNumber = shop.getContactNumber();
                    Intent intent = new Intent(context, MyShopPromoted.class);
                    // Pass the contact number as an extra to the intent
                    intent.putExtra("contactNumber", contactNumber);
                    context.startActivity(intent);
                } else {
                    // Display a message or take appropriate action when there are no promotions
                    Toast.makeText(context, "No promotions for this shop", Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShopDetails.class);
                intent.putExtra("image", shop.getUrl());
                intent.putExtra("ShopName", shop.getShopName());
                intent.putExtra("Name", shop.getName());
                intent.putExtra("Address", shop.getAddress());
                intent.putExtra("contactNumber", shop.getContactNumber());
                intent.putExtra("PhoneNumber", shop.getPhoneNumber());
                intent.putExtra("District", shop.getDistrict());
                intent.putExtra("Taluka", shop.getTaluka());
                intent.putExtra("promotionCount", shop.getpromotionCount());


                IntentDataHolder.setSharedIntent(intent);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        DatabaseReference promtioncountRef = FirebaseDatabase.getInstance().getReference()
                .child("Shop").child(shop.getContactNumber()).child("count").child("promotionCount");
//        promtioncountRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                     promotioncount = snapshot.child("promotionCount").getValue(Integer.class);
//                }
//            }
//
//            @Override
//            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//            }
//        });
        // Assuming shop.getPromotedShopCount() returns the count as a String
        int promotedShopCount = shop.getpromotionCount();

        if (promotedShopCount > 0) {
            // Display the badge count for promoted shops
            holder.promote.setVisibility(View.VISIBLE);
            holder.promotecount.setText(String.valueOf(promotedShopCount));
//            holder.notificationBadge.setVisibility(View.VISIBLE);
//            holder.notificationBadge.setText(String.valueOf(promotedShopCount));
//
//            // Create a custom Drawable with a solid background color
//            GradientDrawable drawable = new GradientDrawable();
//            drawable.setShape(GradientDrawable.OVAL);
//            //  drawable.setColor(Color.GREEN); // Replace with your desired background color
//            int colorResource = R.color.colorAccent;
//            int color = ContextCompat.getColor(context, colorResource);
//            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
//            // Set the corner radius for rounded corners if needed
//            drawable.setCornerRadius(10); // Replace with your desired corner radius
//
//            // Set the background color of the badge
//            holder.notificationBadge.setBadgeBackgroundDrawable(drawable); // Replace with your custom background drawable
//
//            // Set the text color to black
//            holder.notificationBadge.setTextColor(Color.WHITE);
        } else {
            // Hide the badge if the count is zero or negative
            holder.promote.setVisibility(View.GONE);
            holder.notificationBadge.setVisibility(View.GONE);
        }


//        // Display the badge count for promoted shops
//          //  holder.promote.setVisibility(View.VISIBLE);
//            holder.notificationBadge.setText(shop.getPromotedShopCount());
//            // Create a custom Drawable with a solid background color
//            GradientDrawable drawable = new GradientDrawable();
//            drawable.setShape(GradientDrawable.OVAL);
//            drawable.setColor(Color.GREEN); // Replace with your desired background color
//
//            // Set the corner radius for rounded corners if needed
//            drawable.setCornerRadius(10); // Replace with your desired corner radius
//
//            // Set the background color of the badge
//            holder.notificationBadge.setBadgeBackgroundDrawable(drawable); // Replace with your custom background drawable
//            // Set the text color to black
//            holder.notificationBadge.setTextColor(Color.BLACK);


        // Check if the address is empty
        if (TextUtils.isEmpty(shop.getAddress())) {
            // If address is empty, hide the TextView
            holder.textViewAddress.setVisibility(View.GONE);
        } else {
            // If address has a value, show the TextView
            holder.textViewAddress.setVisibility(View.VISIBLE);
            // You can also set the address text here if needed
            holder.textViewAddress.setText(shop.getAddress());
        }

    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {

        public ImageView circleImageView;
        public TextView textViewShopName, promotecount;
        public TextView textViewAddress;
        public TextView textViewcontactNumber;
        public TextView textViewName;
        public TextView textViewdistrict, textViewtaluka;
        public RecyclerView viewdetails;
        public NotificationBadge notificationBadge;
        public CardView promote;


        public ShopViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profileimage);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewShopName = itemView.findViewById(R.id.textViewShopName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewcontactNumber = itemView.findViewById(R.id.textViewcontactNumber);
            textViewdistrict = itemView.findViewById(R.id.textViewdistrict);
            textViewtaluka = itemView.findViewById(R.id.textViewtaluka);
            viewdetails = itemView.findViewById(R.id.viewdetails);
            notificationBadge = itemView.findViewById(R.id.badge_count);
            promotecount = itemView.findViewById(R.id.promotecount);
            promote = itemView.findViewById(R.id.promotecountcard);


        }

        public void bind(Shop shop, Context context) {
            textViewcontactNumber.setText(shop.getContactNumber());
        }
    }


    private void showFullImage(View anchorView, String imageUrl) {
        // Check if imageUrl is not empty or null
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Inflate the popup layout
            View popupView = LayoutInflater.from(context).inflate(R.layout.activity_full_screen, null);

            // Find the ImageView in the popup layout
            ImageView imageView = popupView.findViewById(R.id.popup_image_view);
            ImageView cancelimageView = popupView.findViewById(R.id.close_image_view);

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


            cancelimageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close the popup window when cancelimageview is clicked
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });


        } else {
            // Show a toast message if imageUrl is empty or null
            Toast.makeText(context, "Image not available", Toast.LENGTH_SHORT).show();
        }
    }


}




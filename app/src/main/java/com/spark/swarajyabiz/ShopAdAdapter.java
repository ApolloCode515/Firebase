package com.spark.swarajyabiz;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

public class ShopAdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Object> items; // List of either Ad or Shop objects
    private List<Shop> filteredShopList;


    public ShopAdAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
        this.filteredShopList = new ArrayList<>();
       // insertAds();
    }
    public void updateData(List<Object> updatedItems) {
        this.items.clear();
        this.items.addAll(updatedItems);
        notifyDataSetChanged();
    }

    // Define view types for different data classes
    private static final int VIEW_TYPE_AD = 0;
    private static final int VIEW_TYPE_SHOP = 1;

    private void insertAds() {
        // Create a new list to hold combined data with ads
        List<Object> combinedItems = new ArrayList<>();

        int itemCount = items.size();
        int adPosition = 0; // Track the position to insert ads
        for (int i = 0; i < itemCount; i++) {
            combinedItems.add(items.get(i));

            // Insert an Ad after every 2nd shop
            if (i > 0 && (i + 1) % 2 == 0) {
                Ad adItem = new Ad(); // Create an Ad item (you might need to adjust this based on your Ad class)
                combinedItems.add(adPosition + 1, adItem);
                adPosition++; // Increment the ad position
            }
        }

        // Update the items list with the combined data
        items.clear();
        items.addAll(combinedItems);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_AD) {
            View view = inflater.inflate(R.layout.ads_list, parent, false);
            return new AdViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.list_item_shop, parent, false);
            return new ShopViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);

        if (holder instanceof AdViewHolder && item instanceof Ad) {
            Ad ad = (Ad) item;
            ((AdViewHolder) holder).bind(ad);
//            ((ShopViewHolder) holder).promotionCountText.setVisibility(View.GONE);

        } else if (holder instanceof ShopViewHolder && item instanceof Shop) {
            Shop shop = (Shop) item;
            ((ShopViewHolder) holder).bind(shop);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);

        if (item instanceof Ad) {
            return VIEW_TYPE_AD;
        } else if (item instanceof Shop) {
            return VIEW_TYPE_SHOP;
        }
        return super.getItemViewType(position);
    }

    public void setItems(List<Shop> items) {
        this.filteredShopList.clear();
        this.filteredShopList.addAll(items);
        notifyDataSetChanged();
    }



    public class ShopViewHolder extends RecyclerView.ViewHolder {
        public ImageView circleImageView;
        public TextView textViewShopName;
        public TextView textViewAddress;
        public TextView textViewContactNumber;
        public TextView textViewName;
        public TextView textViewDistrict, textViewTaluka;
        public NotificationBadge notificationBadge;
        public CardView promote;
        public TextView promotionCountText;

        public ShopViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profileimage);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewShopName = itemView.findViewById(R.id.textViewShopName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewContactNumber = itemView.findViewById(R.id.textViewcontactNumber);
            textViewDistrict = itemView.findViewById(R.id.textViewdistrict);
            textViewTaluka = itemView.findViewById(R.id.textViewtaluka);
            promote = itemView.findViewById(R.id.promote);
            notificationBadge = itemView.findViewById(R.id.badge_count);
          // promotionCountText = itemView.findViewById(R.id.promotionco);
        }

        public void bind(Shop shop) {
            textViewContactNumber.setText(shop.getContactNumber());
            textViewName.setText(shop.getName());
            textViewShopName.setText(shop.getShopName());
            textViewAddress.setText(shop.getAddress());
            textViewDistrict.setText(shop.getDistrict());
            textViewTaluka.setText(shop.getTaluka());
            System.out.println("fdfd " + shop.getShopName());
            String imageUrl = shop.getUrl();

            // Load the image using Glide
            Glide.with(circleImageView.getContext())
                    .load(imageUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(circleImageView);

            // Set click listeners
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFullImage(v, imageUrl);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShopDetails.class);
                    intent.putExtra("image", imageUrl);
                    intent.putExtra("ShopName", shop.getShopName());
                    intent.putExtra("Name", shop.getName());
                    intent.putExtra("Address", shop.getAddress());
                    intent.putExtra("contactNumber", shop.getContactNumber());
                    intent.putExtra("PhoneNumber", shop.getPhoneNumber());
                    intent.putExtra("District", shop.getDistrict());
                    intent.putExtra("Taluka", shop.getTaluka());
                    intent.putExtra("promotionCount", shop.getpromotionCount());
                    System.out.println("sfhbdf " +shop.getpromotionCount());
                    IntentDataHolder.setSharedIntent(intent);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            // Display the badge count for promoted shops
            int promotedShopCount = shop.getpromotionCount();
            System.out.println("sffe "+promotedShopCount);

                if (promotedShopCount > 0) {
                    // Display the badge count for promoted shops
                    notificationBadge.setVisibility(View.VISIBLE);
                    notificationBadge.setText(String.valueOf(promotedShopCount));

                    // Create a custom Drawable with a solid background color
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setShape(GradientDrawable.OVAL);
                    int colorResource = R.color.colorAccent;
                    int color = ContextCompat.getColor(context, colorResource);
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    drawable.setCornerRadius(10);

                    notificationBadge.setBadgeBackgroundDrawable(drawable);
                    notificationBadge.setTextColor(Color.WHITE);
                } else {
                    notificationBadge.setVisibility(View.GONE);
                }

            // Check if the address is empty
            if (TextUtils.isEmpty(shop.getAddress())) {
                textViewAddress.setVisibility(View.GONE);
            } else {
                textViewAddress.setVisibility(View.VISIBLE);
                textViewAddress.setText(shop.getAddress());
            }
        }
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView promotionCountText; // Add this field

        public AdViewHolder(View itemView) {
            super(itemView);
          imageView = itemView.findViewById(R.id.adimages);
                  promotionCountText = itemView.findViewById(R.id.promotionCountText); // Initialize promotionCountText for AdViewHolder
        }

        public void bind(Ad ad) {
            Glide.with(imageView.getContext())
                    .load(ad.getImageUrl())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);

//            if (promotionCountText != null) {
//                // For advertisements, hide the promotionCountText
//                promotionCountText.setVisibility(View.GONE);
//            }
        }
    }

    private void showFullImage(View anchorView, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            View popupView = LayoutInflater.from(context).inflate(R.layout.activity_full_screen, null);
            ImageView imageView = popupView.findViewById(R.id.popup_image_view);

            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);

            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;

            PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

            popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
        } else {
            Toast.makeText(context, "Image not available", Toast.LENGTH_SHORT).show();
        }
    }
}

//public class ShopAdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private List<ListItem> items;
//    private Context context;
//
//    public ShopAdAdapter(List<ListItem> items, Context context) {
//        this.items = items;
//        this.context = context;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return items.get(position).getType();
//    }
//
//    @androidx.annotation.NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
//        if (viewType == ListItem.SHOP_TYPE) {
//            View shopView = LayoutInflater.from(context).inflate(R.layout.list_item_shop, parent, false);
//            return new ShopAdapter.ShopViewHolder(shopView);
//        } else {
//            View adView = LayoutInflater.from(context).inflate(R.layout.ads_list, parent, false);
//            return new AdAdapter.AdViewHolder(adView);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        ListItem item = items.get(position);
//
//        if (holder instanceof ShopAdapter.ShopViewHolder) {
//            // Bind shop data to ShopViewHolder
//            Shop shop = ((ShopItem) item).getShop();
//            ((ShopAdapter.ShopViewHolder) holder).bind(shop, context);
//            // Bind shop data to your ShopViewHolder
//        } else if (holder instanceof AdAdapter.AdViewHolder) {
//            // Bind ad data to AdViewHolder
//            Ad ad = ((AdItem) item).getAd();
//            ((AdAdapter.AdViewHolder) holder).bind(ad);
//            // Bind ad data to your AdViewHolder
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//}

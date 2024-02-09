package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class BusinessBannerAdapter extends RecyclerView.Adapter<BusinessBannerAdapter.BannerViewHolder> {

    private Context context;
    private List<String> imageUrls;
    private List<String> businessnametexts;
    private OnItemClickListener onItemClickListener;
    private boolean showAllItems;
    private boolean isShopFragment;
    private List<RelativeLayout> relativeLayouts = new ArrayList<>();
    List<BusinessBanner> businessBannerList;
    private static SharedPreferences sharedPreferences;
    private List<BusinessBanner> favoriteBusinessList = new ArrayList<>();
    private List<BusinessBanner> nonFavoriteBusinessList = new ArrayList<>();


    public BusinessBannerAdapter(List<BusinessBanner> businessBannerList, SharedPreferences sharedPreferences, Context context, OnItemClickListener onItemClickListener, boolean showAllItems) {
       this.businessBannerList = businessBannerList;
        this.sharedPreferences = sharedPreferences;
        this.context = context;
        this.imageUrls = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
        this.showAllItems = showAllItems;
    }

    // Add this method to update the list of image URLs
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        notifyDataSetChanged();
    }

    public void setBusinessInfoList(List<BusinessBanner> businessBannerList) {
        this.businessBannerList = businessBannerList;
        notifyDataSetChanged();
    }

    public void setBusinessnametexts(List<String> businessnametexts) {
        this.businessnametexts = businessnametexts;
        notifyDataSetChanged();
    }

    public void setShopFragment(boolean isShopFragment) {
        this.isShopFragment = isShopFragment;
        notifyDataSetChanged();
    }

    public void shuffleRelativeLayouts() {
        Collections.shuffle(relativeLayouts);
        notifyDataSetChanged();
    }


    // Interface for item click events
    public interface OnItemClickListener {
        void onItemClick(int position, String imageUrl, String businessname) throws ExecutionException, InterruptedException;
        void onfavClick(int position, ImageView favimageview, String businessname);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.business_banner_list, parent, false);
        RelativeLayout relativeLayout = view.findViewById(R.id.relativelayouts);
        relativeLayouts.add(relativeLayout);
        return new BannerViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, @SuppressLint("RecyclerView") int position) {
      // String imageUrl = imageUrls.get(position);
        BusinessBanner businessBanner = businessBannerList.get(position);

        Glide.with(context)
                .load(businessBanner.getImageUrl()) // Load the image at position 0
                //.thumbnail(0.10f)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.bannerImageView);

        holder.businesstextView.setText(businessBanner.getBusinessName());
        checkAndSetFavImageFromDatabase(holder.favImageview, businessBanner.getBusinessName());

            if (isShopFragment) {
                // Set ImageView size to 100dp
                ViewGroup.LayoutParams layoutParams = holder.cardView.getLayoutParams();
                layoutParams.width = 275;
                layoutParams.height = 300;
                holder.cardView.setLayoutParams(layoutParams);

                // Set TextView size to 10sp
                ViewGroup.LayoutParams layoutParam = holder.businesstextView.getLayoutParams();
                layoutParam.width = 275;
                holder.businesstextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

                holder.favImageview.setVisibility(View.GONE);
            }



            holder.favImageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onfavClick(position, holder.favImageview, businessBanner.getBusinessName());

                }
            });

            // Make sure to include the necessary dependencies for Picasso or Glide in your app's build.gradle.

            // Set a click listener for the item
            holder.bannerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Notify the activity or fragment about the click event
                    if (onItemClickListener != null) {
                        try {
                            onItemClickListener.onItemClick(position, businessBanner.getImageUrl(), businessBanner.getBusinessName());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        if (showAllItems) {
            return businessBannerList != null ? businessBannerList.size() : 0;
        } else {
            // Display only the first 5 items for the ShopFragment
            return Math.min(businessBannerList.size(), 5);
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImageView, favImageview;
        TextView businesstextView;
        CardView cardView;
        RelativeLayout relativeLayout;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImageView = itemView.findViewById(R.id.businessimages);
            businesstextView= itemView.findViewById(R.id.businesstextviews);
            cardView = itemView.findViewById(R.id.businessimagecard);
            relativeLayout = itemView.findViewById(R.id.relativelayouts);
            favImageview = itemView.findViewById(R.id.favimageview);
        }
    }

    private void checkAndSetFavImageFromDatabase(ImageView favImageView, String businessName) {
        String userId = sharedPreferences.getString("mobilenumber", null);

        if (userId != null && businessName != null && !businessName.trim().isEmpty()) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Check if the user has a favorites node
                    if (snapshot.child("FavBusiness").exists()) {
                        DataSnapshot favBusinessSnapshot = snapshot.child("FavBusiness");

                        // Check if the businessName is marked as a favorite
                        if (favBusinessSnapshot.child(businessName).exists()) {
                            boolean isFav = favBusinessSnapshot.child(businessName).getValue(Boolean.class);
                            System.out.println("fcsdfg " + isFav);

                            if (isFav) {
                                // If 'fav' is true, set the favImageView to the filled image
                                favImageView.setImageResource(R.drawable.ic_baseline_favorite_fill_24);
                            } else {
                                // 'fav' is false, set the favImageView to the border image
                                favImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                            }
                        } else {
                            // 'businessName' key is not present, assume it's not a favorite
                            favImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        }
                    } else {
                        // User doesn't have any favorites, assume it's not a favorite
                        favImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });
        } else {
            // Handle the case where businessName, userId, or both are null
            favImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
    }

}

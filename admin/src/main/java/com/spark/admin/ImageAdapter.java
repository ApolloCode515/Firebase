package com.spark.admin;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<String> imageUrls;
    private View anchorView;
    private ImageClickListener imageClickListener;
    private RecyclerView recyclerView;



    public ImageAdapter(Context context, List<String> imageUrls, ImageClickListener imageClickListener, RecyclerView recyclerView) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.imageClickListener = imageClickListener;
        this.recyclerView = recyclerView; // Set the RecyclerView

    }
    public interface ImageClickListener {
        void onRemoveClick(int position);

    }


    public void setImageUrls(List<String> imageUrls) {
        if (imageUrls != null) {
            this.imageUrls = imageUrls;
        } else {
            this.imageUrls = new ArrayList<>(); // or any appropriate default value
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
            String imageUrl = imageUrls.get(holder.getAdapterPosition());

            Glide.with(context)
                    .load(imageUrl).centerCrop()
                    .into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    anchorView = v;
                    showFullImage(imageUrls, holder.getAdapterPosition());
                }
            });

        if (context instanceof ShopDetails || context instanceof Create_Profile) {
            holder.cancelImage.setVisibility(View.GONE); // Hide the cancel button
        } else {
            holder.cancelImage.setVisibility(View.VISIBLE); // Show the cancel button
        }

            holder.cancelImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageClickListener.onRemoveClick(position);
                    holder.cardView.setVisibility(View.GONE);
                }
            });

    }
    public void removeItem(int position) {
        if (position >= 0 && position < imageUrls.size()) {
            imageUrls.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }
    public CardView getCardView(int position) {
        if (position >= 0 && position < getItemCount()) {
            ImageViewHolder holder = (ImageViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
            if (holder != null) {
                return holder.cardView;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, cancelImage;
        CardView cardView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            cancelImage = itemView.findViewById(R.id.cancelimage);
            cardView = itemView.findViewById(R.id.cardprofile);
        }
    }

    private void showFullImage(List<String> imageUrls, int selectedImageIndex) {
        // Inflate the popup layout
        View popupView = LayoutInflater.from(context).inflate(R.layout.activity_full_screen, null);

        // Find the ViewPager in the popup layout
        ViewPager viewPager = popupView.findViewById(R.id.popup_image_view);
        ImageView cancelimageView = popupView.findViewById(R.id.close_image_view);

        // Create an adapter for the ViewPager
        FullImageAdapter adapter = new FullImageAdapter(context, imageUrls);
        viewPager.setAdapter(adapter);

        // Set the current item to the selected image index
        viewPager.setCurrentItem(selectedImageIndex);

        // Calculate the width and height of the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        // Create the PopupWindow and set its properties
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        cancelimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the popup window when cancelimageview is clicked
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });


        // Show the popup window at the center of the anchor view
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
    }

       // implementation 'com.jsibbold:zoomage:1.3.1'

//       Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_full_images);
//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//
//        ZoomageView zoomageView=dialog.findViewById(R.id.dialog_image_view);
//
//        Glide.with(context)
//                .load(imageUrl)
//                .into(zoomageView);
//
//        wlp.gravity = Gravity.CENTER;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
//        window.setAttributes(wlp);
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        dialog.show();
    }



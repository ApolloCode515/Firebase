package com.spark.swarajyabiz;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class ShopImageAdapter extends RecyclerView.Adapter<ShopImageAdapter.ViewHolder> {
    private List<String> imageUrls;
    private Context context;
    private OnImageClickListener onImageClickListener;

    public ShopImageAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        if (imageUrls != null) {
            this.imageUrls = imageUrls;
        } else {
            this.imageUrls = new ArrayList<>(); // or any appropriate default value
        }
    }

    public interface OnImageClickListener {
        void onImageClick(String imageUrl);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Glide.with(context)
                .load(imageUrl)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onImageClickListener != null) {
                    onImageClickListener.onImageClick(imageUrl);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.images_view);
        }
    }
    public void setOnImageClickListener(ShopImageAdapter.OnImageClickListener listener) {
        this.onImageClickListener = listener;
    }

    private void showFullImage(View anchorView, String imageUrl) {
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
    }

}

    // implementation 'com.jsibbold:zoomage:1.3.1'

//       Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dia log.setContentView(R.layout.dialog_full_images);
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






package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
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

//public class ItemImagesAdapter extends FirebaseRecyclerAdapter<String, ItemImagesAdapter.ImageViewHolder> {
//
//    public ItemImagesAdapter(@NonNull FirebaseRecyclerOptions<String> options) {
//        super(options);
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull ImageViewHolder holder, int position, @NonNull String imageUrl) {
//        Glide.with(holder.imageView.getContext())
//                .load(imageUrl)
//                .into(holder.imageView);
//    }
//
//    @NonNull
//    @Override
//    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
//        return new ImageViewHolder(view);
//    }
//
//    public static class ImageViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
//
//        public ImageViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.imageView);
//        }
//    }
//}

public class ItemImagesAdapter extends RecyclerView.Adapter<ItemImagesAdapter.ImageViewHolder>{

    private List<String> imagesUrls;
    private Context context;
    private View anchorView;
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private ImageClickListener imageClickListener;

    public ItemImagesAdapter(Context context, List<String> imagesUrls, ImageClickListener imageClickListener) {
        this.imagesUrls = imagesUrls;
        this.context = context;
        this.imageClickListener = imageClickListener;
    }

    public interface ImageClickListener {
        void onGalleryClick(int position);
    }




    public void setImagesUrls(List<String> imageUrls) {
        if (imageUrls != null) {
            this.imagesUrls = imageUrls;
        } else {
            this.imagesUrls = new ArrayList<>(); // or any appropriate default value
        }
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemimages, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String imageUrl = imagesUrls.get(holder.getAdapterPosition());


        Glide.with(context)
                .load(imageUrl)
                .into(holder.imageView);



        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (context instanceof ItemInformation) {
                    imageClickListener.onGalleryClick(position);
                } else if (context instanceof ItemDetails) {
                    showFullImage(v,imagesUrls, position);
                }
            }
        });

    }

    public void updateImageAtPosition(int position, String imageUrl) {
        if (position >= 0 && position < imagesUrls.size()) {
            imagesUrls.set(position, imageUrl);
            notifyItemChanged(position);
        }
    }
    public String getImageURLAtPosition(int position) {
        if (position >= 0 && position < imagesUrls.size()) {
            return imagesUrls.get(position);
        } else {
            return ""; // Return an empty string or a default value if the position is out of bounds
        }
    }



    @Override
    public int getItemCount() {
        return imagesUrls != null ? imagesUrls.size() : 0;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }


    private void showFullImage(View anchorView,List<String> imageUrls, int position) {
        // Inflate the popup layout
        View popupView = LayoutInflater.from(context).inflate(R.layout.activity_full_screen, null);

        // Find the ImageView in the popup layout
        ImageView imageView = popupView.findViewById(R.id.popup_image_view);
        ImageView cancelimageView = popupView.findViewById(R.id.close_image_view);

        // Load the image into the ImageView using Glide
        String imageUrl = imageUrls.get(position);
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

    }
}


//public class ItemImagesAdapter extends RecyclerView.Adapter<ItemImagesAdapter.ViewHolder> {
//
//    private Context context;
//    private List<String> imagesUrls;
//
//    public ItemImagesAdapter(Context context, List<String> imageUrls) {
//        this.context = context;
//        this.imagesUrls = imageUrls;
//    }
//
//    public void setImagesUrls(List<String> imageUrls) {
//        this.imagesUrls = imageUrls;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemimages, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        String imageUrl = imagesUrls.get(holder.getAdapterPosition());
//        Glide.with(context).load(imageUrl).into(holder.imageView);
//
//        // Retrieve the first image URL from the imageUrls map
//        if (item.getFirstImageUrl() != null && !item.getFirstImageUrl().isEmpty()) {
//            String firstImageUrl = item.getFirstImageUrl();
//
//            Glide.with(holder.imageView.getContext())
//                    .load(firstImageUrl).centerCrop()
//                    .into(holder.imageView);
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return imagesUrls != null ? imagesUrls.size() : 0;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.imageView);
//        }
//    }
//}
package com.spark.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.AdViewHolder> {

    private List<Ad> adList;
    private Context context;

    public AdAdapter(List<Ad> adList, Context context) {
        this.context = context;
        this.adList = adList;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_list, parent, false);
        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        Ad ad = adList.get(position);

//        // Load the image using Picasso or any other image loading library
//        Glide.with(holder.imageView.getContext())
//                .load(ad.getImageUrl()).centerCrop()
//                .into(holder.imageView);
//
//        System.out.println("fedf " +ad);
    }

    @Override
    public int getItemCount() {
        return adList != null ? adList.size() : 0;
    }

    public static class AdViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public AdViewHolder(View itemView) {
            super(itemView);
          imageView = itemView.findViewById(R.id.adimages);
        }

                public void bind(Ad ad) {
            Glide.with(imageView.getContext())
                    .load(ad.getImageUrl())
                    .centerCrop()
                    .into(imageView);

//            if (promotionCountText != null) {
//                // For advertisements, hide the promotionCountText
//                promotionCountText.setVisibility(View.GONE);
//            }
        }

    }
}

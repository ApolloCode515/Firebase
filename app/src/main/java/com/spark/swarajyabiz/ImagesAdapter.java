package com.spark.swarajyabiz;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class ImagesAdapter  extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>{
    private Context context;
    private List<String> imageUris;

    public ImagesAdapter(Context context, List<String> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImagesAdapter.ImagesViewHolder holder, int position) {
        String imageUri = imageUris.get(position);
        Glide.with(context).load(imageUri)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);
    }



    @Override
    public int getItemCount() {
        return imageUris != null ? imageUris.size() : 0;
    }

    public static class ImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }

}

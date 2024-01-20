package com.spark.swarajyabiz;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PostBannerAdapter extends RecyclerView.Adapter<PostBannerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PostBannerClass> bannerList;

    onClickImageListener onClickImageListener;

    public PostBannerAdapter(Context context, ArrayList<PostBannerClass> bannerList, onClickImageListener onClickImageListener) {
        this.context = context;
        this.bannerList = bannerList;
        this.onClickImageListener = onClickImageListener;
    }

    interface onClickImageListener{
            void onClickImage(int position, String imageUrls);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productimagelist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context).load(bannerList.get(position).getBannerImageUrls()).into(holder.imageView);
        System.out.println("wrsgvgrs" +bannerList.get(position).getBannerImageUrls());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, AddPostNew.class);
//                intent.putExtra("imageUri", bannerList.get(position).getBannerImageUrls());
//                context.startActivity(intent);
                onClickImageListener.onClickImage(position, bannerList.get(position).getBannerImageUrls());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image_view);
        }

    }
}

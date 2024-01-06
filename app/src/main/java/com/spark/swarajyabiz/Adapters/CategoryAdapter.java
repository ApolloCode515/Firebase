package com.spark.swarajyabiz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spark.swarajyabiz.ModelClasses.CategoryModel;
import com.spark.swarajyabiz.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context mContext;

    private ArrayList<CategoryModel> categoryModels;

    DataFetcher dataFetcher;

    public CategoryAdapter(Context mContext, ArrayList<CategoryModel> categoryModels, DataFetcher dataFetcher) {
        this.mContext = mContext;
        this.categoryModels = categoryModels;
        this.dataFetcher = dataFetcher;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorylay,parent,false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.nameTextView.setText(categoryModels.get(position).getCname());
        Glide.with(mContext)
                .load(categoryModels.get(position).getCimg())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView imageView;
        CardView mainCard;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cardText);
            imageView = itemView.findViewById(R.id.cardImg);
            mainCard = itemView.findViewById(R.id.cardclick);
            layout = itemView.findViewById(R.id.lay12);
        }
    }
}

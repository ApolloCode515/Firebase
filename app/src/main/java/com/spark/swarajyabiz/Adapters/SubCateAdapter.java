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
import com.spark.swarajyabiz.ModelClasses.SubCategoryModel;
import com.spark.swarajyabiz.R;

import java.util.ArrayList;

public class SubCateAdapter extends RecyclerView.Adapter<SubCateAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<SubCategoryModel> subCategoryModels;
    private OnItemClickListener onItemClickListener;

    public SubCateAdapter(Context mContext, ArrayList<SubCategoryModel> subCategoryModels, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.subCategoryModels = subCategoryModels;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SubCateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcatelay, parent, false);
        return new SubCateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCateAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return subCategoryModels.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Setter method for the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView imageView;
        CardView mainCard;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.crdtxt);
            imageView = itemView.findViewById(R.id.crdImg);
            mainCard = itemView.findViewById(R.id.maiv);
            //layout = itemView.findViewById(R.id.lay12);

            // Set click listener for the item view
            mainCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

        public void bind(int position) {
            SubCategoryModel categoryModel = subCategoryModels.get(position);
            nameTextView.setText(categoryModel.getSubName());
            Glide.with(mContext)
                    .load(categoryModel.getSubImg())
                    .into(imageView);
        }

    }
}

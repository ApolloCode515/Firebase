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
import com.spark.swarajyabiz.ModelClasses.MemberModel;
import com.spark.swarajyabiz.ModelClasses.SubCategoryModel;
import com.spark.swarajyabiz.R;

import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<MemberModel> memberModels;
    private OnItemClickListener onItemClickListener;

    public MemberAdapter(Context mContext, ArrayList<MemberModel> memberModels, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.memberModels = memberModels;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comembers, parent, false);
        return new MemberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return memberModels.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Setter method for the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView,count;
        ImageView imageView;
        CardView mainCard;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.userName);
            imageView = itemView.findViewById(R.id.shopImg);
            mainCard = itemView.findViewById(R.id.crdCliks);
            count = itemView.findViewById(R.id.shopCnt);
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
            MemberModel categoryModel = memberModels.get(position);
            nameTextView.setText(categoryModel.getMbName());
            count.setText(categoryModel.getShopName());
            Glide.with(mContext)
                    .load(categoryModel.getShopImage())
                    .into(imageView);
        }

    }
}

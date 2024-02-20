package com.spark.swarajyabiz.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.spark.swarajyabiz.CommInfo;
import com.spark.swarajyabiz.CommInfoGlobal;
import com.spark.swarajyabiz.ModelClasses.CommModel;
import com.spark.swarajyabiz.ModelClasses.CouponModel;
import com.spark.swarajyabiz.R;

import java.util.ArrayList;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CouponModel> couponModels;
    private OnItemClickListener onItemClickListener;

    public CouponAdapter(Context mContext, ArrayList<CouponModel> couponModels, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.couponModels = couponModels;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cpnlist, parent, false);
        return new CouponAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return couponModels.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Setter method for the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cpnId,cpnAmt;
        ImageView cpnfront,cpnback;
        CardView mainCard;

        // Define an array of colors
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cpnId = itemView.findViewById(R.id.cpnIdxc);
            cpnAmt = itemView.findViewById(R.id.cpnAmtXXX);
            cpnfront = itemView.findViewById(R.id.imgfront1);
            cpnback = itemView.findViewById(R.id.imgback1);
            mainCard = itemView.findViewById(R.id.cpnCardxx);

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
            CouponModel categoryModel = couponModels.get(position);
            cpnId.setText("# "+categoryModel.getCpnId());
            cpnAmt.setText("₹ "+categoryModel.getCpnAmt());

            Glide.with(mContext)
                    .load(categoryModel.getCpnFront())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(cpnfront);

            Glide.with(mContext)
                    .load(categoryModel.getCpnBack())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(cpnback);

        }

    }
}

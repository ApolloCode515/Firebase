package com.spark.swarajyabiz.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spark.swarajyabiz.ModelClasses.BoxCpnModel;
import com.spark.swarajyabiz.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BoxCpnAdapter extends RecyclerView.Adapter<BoxCpnAdapter.ViewHolder> {
    private final ArrayList<BoxCpnModel> dataList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(BoxCpnModel itemData);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public BoxCpnAdapter(ArrayList<BoxCpnModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public BoxCpnAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bpnitems, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoxCpnAdapter.ViewHolder holder, int position) {
        BoxCpnModel itemData = dataList.get(position);
        holder.bind(itemData);
    }

    @Override
    public int getItemCount() {
       return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgkkk);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(dataList.get(position));
                    }
                }
            });
        }

        public void bind(BoxCpnModel itemData) {
            // Load image from Firebase Storage using Glide

            Log.d("fgdsgadfgadgad",""+itemData.getFront());
//            Glide.with(itemView.getContext())
//                    .load(itemData.getFront()) // Assuming getFront() returns the image URL
//                    .into(imageView);

            Picasso.get()
                    .load(itemData.getFront())  // Assuming getFront() returns the image URL
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }
    }


}

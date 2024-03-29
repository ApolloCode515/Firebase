package com.spark.swarajyabiz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spark.swarajyabiz.CommInfo;
import com.spark.swarajyabiz.ModelClasses.CategoryModel;
import com.spark.swarajyabiz.ModelClasses.CommModel;
import com.spark.swarajyabiz.R;
import com.spark.swarajyabiz.ui.CommunityFragment;

import java.util.ArrayList;

public class CommClickAdapter extends RecyclerView.Adapter<CommClickAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CommModel> commModels;
    private OnItemClickListener onItemClickListener;
    private boolean isADDPostNew;

    private SparseBooleanArray selectedItems;

    public CommClickAdapter(Context mContext, ArrayList<CommModel> commModels, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.commModels = commModels;
        this.onItemClickListener = onItemClickListener;
        this.selectedItems = new SparseBooleanArray();
    }

    public void setisADDPostNew(boolean isADDPostNew) {
        this.isADDPostNew = isADDPostNew;
        notifyDataSetChanged();
    }

    public void checkAll() {
        for (CommModel model : commModels) {
            model.setChecked(true);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commclicklist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return commModels.size();
    }
    public interface OnItemClickListener {
        void onItemClick(int position, boolean isChecked);
    }

    // Setter method for the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView commName,mbrCnt;
        ImageView imageView;
        CardView mainCard;
        CheckBox checkBox;

        // Define an array of colors

        // Define the default and selected colors
        private final int defaultColor = Color.parseColor("#771591");
        private final int selectedColor = Color.parseColor("#239328");

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commName = itemView.findViewById(R.id.comuName);
            mbrCnt = itemView.findViewById(R.id.mbcnt);
            imageView = itemView.findViewById(R.id.comuImg);
            mainCard = itemView.findViewById(R.id.clikcc);
            checkBox = itemView.findViewById(R.id.checkBox);

            // Set click listener for the item view
//            mainCard.setOnClickListener(new View.OnClickListener() {
//                @Override
//
//                public void onClick(View v) {
//                    if (onItemClickListener != null) {
//                        onItemClickListener.onItemClick(getAdapterPosition());
//                        Intent intent=new Intent(mContext, CommInfo.class);
//                        mContext.startActivity(intent);
//                    }
//                }
//            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Toggle the checked state
                            commModels.get(position).setChecked(!commModels.get(position).isChecked());
                            notifyItemChanged(position);

                            // Handle item click
                            onItemClickListener.onItemClick(position, commModels.get(position).isChecked());
                        }
                    }
                }
            });

        }

        public void bind(int position) {
            CommModel categoryModel = commModels.get(position);
            commName.setText(categoryModel.getCommName());
            mbrCnt.setText(categoryModel.getMbrCount());
            Glide.with(mContext)
                    .load(categoryModel.getCommImg())
                    .into(imageView);

            checkBox.setChecked(categoryModel.isChecked());

        }

    }
}

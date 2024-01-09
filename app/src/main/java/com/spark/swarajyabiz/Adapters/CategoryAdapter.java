package com.spark.swarajyabiz.Adapters;

import android.content.Context;
import android.graphics.Color;
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
    private OnItemClickListener onItemClickListener;

    public CategoryAdapter(Context mContext, ArrayList<CategoryModel> categoryModels, OnItemClickListener listener) {
        this.mContext = mContext;
        this.categoryModels = categoryModels;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorylay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    // Interface for item click events
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

        // Define an array of colors

        // Define the default and selected colors
        private final int defaultColor = Color.parseColor("#771591");
        private final int selectedColor = Color.parseColor("#239328");

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cardText);
            imageView = itemView.findViewById(R.id.cardImg);
            mainCard = itemView.findViewById(R.id.cardclick);
            layout = itemView.findViewById(R.id.lay12);



            // Set click listener for the item view
            mainCard.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {

                    // Reset the color of all items to default
                    resetColors();

                    // Set the background color of the currently clicked item to selected color
                    layout.setBackgroundColor(selectedColor);

                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

        private void resetColors() {
            if (itemView.getParent() instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) itemView.getParent();
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View itemView = recyclerView.getChildAt(i);
                    if (itemView != null) {
                        LinearLayout itemLayout = itemView.findViewById(R.id.lay12);
                        if (itemLayout != null) {
                            itemLayout.setBackgroundColor(defaultColor);
                        }
                    }
                }
            }
        }

        public void bind(int position) {
            CategoryModel categoryModel = categoryModels.get(position);
            nameTextView.setText(categoryModel.getCname());
            Glide.with(mContext)
                    .load(categoryModel.getCimg())
                    .into(imageView);

            // Set the default color
            layout.setBackgroundColor(defaultColor);

        }

    }
}

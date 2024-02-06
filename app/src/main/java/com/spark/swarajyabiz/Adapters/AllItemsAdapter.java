package com.spark.swarajyabiz.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spark.swarajyabiz.ItemDetails;
import com.spark.swarajyabiz.ItemList;
import com.spark.swarajyabiz.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class AllItemsAdapter extends RecyclerView.Adapter<AllItemsAdapter.ViewHolder>{

    private List<ItemList> itemList;
    private Context context;

    public AllItemsAdapter(List<ItemList> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_items_list, parent, false);
        return new AllItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemList item = itemList.get(position);

        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText("₹ "+item.getSellPrice()+".00");
        //holder.descriptionTextView.setText(item.getDescription());
        holder.itemkey.setText(item.getItemkey());

        // Check if there is a description available
        if (item.getDescription() != null && !item.getDescription().isEmpty()) {
            holder.descriptionTextView.setText(item.getDescription());
            holder.descriptionTextView.setVisibility(View.VISIBLE); // Make the TextView visible
        } else {
            holder.descriptionTextView.setVisibility(View.GONE); // Hide the TextView
            setTopMargin(holder.nameTextView, dpToPx(25)); // Set top margin to 20dp
        }

        // Retrieve the first image URL from the imageUrls map
        if (item.getFirstImageUrl() != null && !item.getFirstImageUrl().isEmpty()) {
            String firstImageUrl = item.getFirstImageUrl();

            Glide.with(holder.imageView.getContext())
                    .load(firstImageUrl).centerCrop()
                    .into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    ItemList clickedItem = itemList.get(clickedPosition);

                    // Create an Intent to open the ItemDetailsActivity
                    Intent intent = new Intent(view.getContext(), ItemDetails.class);
                    intent.putExtra("itemName", clickedItem.getName());
                    intent.putExtra("itemPrice", clickedItem.getPrice());
                    intent.putExtra("itemDescription", clickedItem.getDescription());
                    intent.putExtra("itemKey", clickedItem.getItemkey());
                    intent.putExtra("shopName", clickedItem.getShopName());
                    intent.putExtra("contactNumber", clickedItem.getShopcontactNumber());
                    System.out.println("fgbgopoiv " +clickedItem.getShopcontactNumber());
                    intent.putExtra("district", clickedItem.getDistrict());
                    intent.putExtra("firstImageUrl", clickedItem.getFirstImageUrl());
                    intent.putExtra("itemOffer", clickedItem.getOffer());
                    intent.putExtra("itemMinqty", clickedItem.getMinqty());
                    intent.putExtra("itemWholesale", clickedItem.getWholesaleprice());
                    intent.putExtra("itemSellPrice", clickedItem.getSellPrice());
                    intent.putStringArrayListExtra("itemImages", (ArrayList<String>) clickedItem.getImagesUrls());

                    //  IntentDataHolder.setSharedIntent(intent);
                    // intent.putExtra("itemImage", clickedItem.getImageUrl());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
                    // Start the activity
                    view.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        TextView descriptionTextView;
        ImageView imageView;
        TextView itemkey;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemName);
            priceTextView = itemView.findViewById(R.id.itemPrice);
            descriptionTextView = itemView.findViewById(R.id.itemDescription);
            imageView = itemView.findViewById(R.id.itemimage);
            itemkey = itemView.findViewById(R.id.itemkey);

        }
    }

    private String formatPrice(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,##,##0.00", symbols);
        return "₹" + decimalFormat.format(price);
    }

    // Helper method to convert dp to pixels
    private int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    // Helper method to set the top margin for a view
    private void setTopMargin(View view, int margin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.topMargin = margin;
            view.requestLayout();
        }
    }
}


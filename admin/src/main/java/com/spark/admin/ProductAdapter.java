package com.spark.admin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<ItemList> {

    private Context context;
    public ProductAdapter(Context context, List<ItemList> itemList) {
        super(context, 0, itemList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;



        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.productimagelist, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ItemList item = getItem(position);

        if (item != null) {
            if (item.getImagesUrls() != null && !item.getImagesUrls().isEmpty()) {
                String firstImageUrl = item.getImagesUrls().get(0);

                Glide.with(holder.imageView.getContext())
                        .load(firstImageUrl)
                        .centerCrop()
                        .into(holder.imageView);

                // Set click listener for the image
                holder.cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create an Intent to open the ItemDetailsActivity
                        Intent intent = new Intent(context, ItemDetails.class);
                        intent.putExtra("itemName", item.getName());
                        intent.putExtra("itemPrice", item.getPrice());
                        intent.putExtra("itemDescription", item.getDescription());
                        intent.putExtra("itemKey", item.getItemkey());
                        intent.putExtra("shopName", item.getShopName());
                        intent.putExtra("district", item.getDistrict());
                        intent.putExtra("contactNumber", item.getShopcontactNumber());
                        intent.putExtra("firstImageUrl", firstImageUrl); // Pass the image URL
                        intent.putStringArrayListExtra("itemImages", (ArrayList<String>) item.getImagesUrls());
                        System.out.println("erfsfdg " +item.getShopName());

                        // Start the activity
                        context.startActivity(intent);
                    }
                });
            }
        }

        return view;
    }

    private static class ViewHolder {
        ImageView imageView;
        CardView cardview;

        ViewHolder(View itemView) {
            imageView = itemView.findViewById(R.id.product_image_view);
            cardview = itemView.findViewById(R.id.cardimage);
        }
    }
}


package com.spark.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class AllPromotedShopAdapter  extends RecyclerView.Adapter<AllPromotedShopAdapter.ShopViewHolder>{

    private List<PromoteShop> shopList;
    private Context context;
    private List<PromoteShop> filteredShopList;

    public AllPromotedShopAdapter( Context context, List<PromoteShop> shopList) {
        this.shopList = shopList;
        this.context = context;
        this.filteredShopList = new ArrayList<>(shopList);
    }

    public void setItems(List<PromoteShop> shopList) {
        this.shopList = shopList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AllPromotedShopAdapter.ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop, parent, false);
        return new ShopViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AllPromotedShopAdapter.ShopViewHolder holder, int position) {
        PromoteShop shop = shopList.get(position);

        Glide.with(holder.circleImageView.getContext())
                .load(shop.getUrl()).centerCrop()
                .into(holder.circleImageView);

        holder.textViewName.setText(shop.getName());
        holder.textViewShopName.setText(shop.getShopName());
        holder.textViewAddress.setText(shop.getAddress());
        holder.textViewcontactNumber.setText(shop.getContactNumber());
        holder.textViewdistrict.setText(shop.getDistrict());
        holder.textViewtaluka.setText(shop.getTaluka());
        String imageUrl = shop.getUrl();
        holder.bind(shop);
       /* String imageurl = null;
        imageurl= shop.getUrl();
        Picasso.get().load(imageurl).into(holder.circleImageView);
*/
        Glide.with(holder.circleImageView.getContext())
                .load(shop.getUrl()).centerCrop()
                .into(holder.circleImageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShopDetails.class);
                intent.putExtra("image", shop.getUrl());
                intent.putExtra("ShopName", shop.getShopName());
                intent.putExtra("Name", shop.getName());
                intent.putExtra("Address", shop.getAddress());
                intent.putExtra("contactNumber", shop.getContactNumber());
                intent.putExtra("PhoneNumber", shop.getPhoneNumber());
                intent.putExtra("District", shop.getDistrict());
                intent.putExtra("Taluka", shop.getTaluka());
                intent.putExtra("url", shop.getUrl());


                IntentDataHolder.setSharedIntent(intent);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return shopList.size();
    }




    public class ShopViewHolder extends RecyclerView.ViewHolder {

        public ImageView circleImageView;
        public TextView textViewShopName;
        public TextView textViewAddress;
        public TextView textViewcontactNumber;
        public TextView textViewName;
        public TextView textViewdistrict, textViewtaluka;
        public RecyclerView viewdetails;
        public CheckBox checkBox;

        public ShopViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profileimage);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewShopName = itemView.findViewById(R.id.textViewShopName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewcontactNumber = itemView.findViewById(R.id.textViewcontactNumber);
            textViewdistrict = itemView.findViewById(R.id.textViewdistrict);
            textViewtaluka = itemView.findViewById(R.id.textViewtaluka);
            viewdetails = itemView.findViewById(R.id.viewdetails);
            checkBox = itemView.findViewById(R.id.checkbox);


        }
        public void bind(PromoteShop shop) {
            textViewcontactNumber.setText(shop.getContactNumber());
        }

    }

}

package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso; // You can use Picasso or any other image loading library

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<ItemList> itemList;
    private String shopName;
    private Context context;
    private View anchorView;
    private PostClickListener PostClickListener;

    public PostAdapter(Context context, List<ItemList> itemList, PostClickListener postClickListener) {
        this.itemList = itemList;
        this.context = context;
        this.PostClickListener = postClickListener;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public interface PostClickListener {
        void onClickClick(int position);
        void onContactClick(int position);
        void onorderClick(int position);
        void oncallClick(int position);
        void onseeallClick(int position);
    }

    public void setData(List<ItemList> filteredData) {
        itemList = filteredData;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_lists, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ItemList item = itemList.get(position);

        // Bind data to the ViewHolder's views
        holder.bindPost( item, position);


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView postImage, shopimageview, profileimageview;
        private TextView postCaption, shopnametextview, captiontext, itemname, itemprice;
        private Button orderbtn, callbtn, seeallbtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.postimageview);
            profileimageview = itemView.findViewById(R.id.Profileimageview);
            postCaption = itemView.findViewById(R.id.captiontextview);
            captiontext = itemView.findViewById(R.id.captiontext);
            shopimageview = itemView.findViewById(R.id.shopimageview);
            shopnametextview = itemView.findViewById(R.id.shopnametextview);
            itemname = itemView.findViewById(R.id.itemtextview);
            itemprice = itemView.findViewById(R.id.pricetextview);
            orderbtn = itemView.findViewById(R.id.orderbtn);
            callbtn = itemView.findViewById(R.id.callbtn);
            seeallbtn = itemView.findViewById(R.id.seeallbtn);
        }

        public void bindPost(ItemList item, int position) {
            // Bind the Post data to the views
            if (item.getImagesUrls() != null && !item.getImagesUrls().isEmpty()) {
                String firstImageUrl = item.getImagesUrls().get(0); // Get the first image URL

                Picasso.get()
                        .load(firstImageUrl) // Load the first image URL
                        .resize(500, 500) // Resize the image to 500x500 pixels
                        .centerCrop()
                        .placeholder(R.color.black_overlay) // Center-crop the image within the target dimensions
                        .into(postImage);
            }
// Set the image into the ImageView postImage
            // Load the image using Picasso
            Picasso.get().load(item.getShopimage())
                    .placeholder(R.drawable.ic_outline_person_24)
                    .into(shopimageview);

            itemname.setText(item.getName());
            itemprice.setText(item.getPrice());
            // Apply the strikethrough effect
          //  itemprice.setPaintFlags(itemprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


//            String itemtext = "<b>Name :</b> " + item.getName();
//            itemname.setText(Html.fromHtml(itemtext));
//
//            String pricetext = "<b>Price :</b> " + item.getPrice();
//            itemprice.setText(Html.fromHtml(pricetext));

//                String captionText = "<b>Information :</b> " + item.getDescription();
//                postCaption.setText(Html.fromHtml(captionText));


            shopnametextview.setText(item.getShopName());

            if (item.getDescription().isEmpty()){
                postCaption.setVisibility(View.GONE);
                captiontext.setVisibility(View.GONE);

            }

            postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    anchorView = v;
                    if (item.getImagesUrls() != null && !item.getImagesUrls().isEmpty()) {
                        String firstImageUrl = item.getImagesUrls().get(0);
                        showFullImage(v, firstImageUrl);
                    }
                }
            });


            shopnametextview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostClickListener.onClickClick(position);
                }
            });

            orderbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostClickListener.onorderClick(position);
                }
            });

            callbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostClickListener.oncallClick(position);
                }
            });

            seeallbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostClickListener.onseeallClick(position);
                }
            });

//            profileimageview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    PopupMenu popupMenu = new PopupMenu(context, view);
//                    popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
//
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem menuItem) {
//                            switch (menuItem.getItemId()) {
//
//                                case R.id.menu_Profile:
//                                    // Start the FirstPage activity and clear the task stack
//                                    Intent intents = new Intent(context, ShopDetails.class);
//                                    intents.putExtra("contactNumber", item.getShopcontactNumber());
//                                    context.startActivity(intents);
//                                    return true;
//                                default:
//                                    return false;
//
//                            }
//                        }
//                    });
//
//                    popupMenu.show();
//                }
//            });


        }
    }

    private void showFullImage(View anchorView, String imageUrls) {
        // Check if imageUrl is not empty or null
        if (imageUrls != null && !imageUrls.isEmpty()) {
            // Inflate the popup layout
            View popupView = LayoutInflater.from(context).inflate(R.layout.activity_full_screen, null);

            // Find the ImageView in the popup layout
            ImageView imageView = popupView.findViewById(R.id.popup_image_view);
            ImageView cancelimageView = popupView.findViewById(R.id.close_image_view);

            // Load the image into the ImageView using Glide
            Glide.with(context)
                    .load(imageUrls)
                    .into(imageView);

            // Calculate the width and height of the popup window
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;

            // Create the PopupWindow and set its properties
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

            // Show the popup window at the center of the anchor view
            popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);


            cancelimageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close the popup window when cancelimageview is clicked
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });


        } else {
            // Show a toast message if imageUrl is empty or null
            Toast.makeText(context, "Image not available", Toast.LENGTH_SHORT).show();
        }
    }

}


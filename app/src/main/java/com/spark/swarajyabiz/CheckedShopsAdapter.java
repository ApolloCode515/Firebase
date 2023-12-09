package com.spark.swarajyabiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.rxjava3.annotations.NonNull;

public class CheckedShopsAdapter extends RecyclerView.Adapter<CheckedShopsAdapter.ShopViewHolder> {
    private List<PromoteShop> ShopsList;
    private Context context;
    private Set<Integer> selectedItems = new HashSet<>();
    private CheckedShopsAdapter.OnItemSelectionChangedListener itemSelectionChangedListener;
    private static final String USER_ID_KEY = "userID";
    private static SharedPreferences sharedPreferences;

    public CheckedShopsAdapter(Context context, List<PromoteShop> shopList,  SharedPreferences sharedPreferences) {
        this.ShopsList = shopList;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.promote_shop_list, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        PromoteShop shop = ShopsList.get(position);

        Glide.with(holder.circleImageView.getContext())
                .load(shop.getUrl()).centerCrop()
                .placeholder(R.drawable.logo)
                .into(holder.circleImageView);

        holder.textViewName.setText(shop.getName());
        holder.textViewShopName.setText(shop.getShopName());
        holder.textViewAddress.setText(shop.getAddress());
        holder.textViewcontactNumber.setText(shop.getContactNumber());
        holder.textViewdistrict.setText(shop.getDistrict());
        holder.textViewtaluka.setText(shop.getTaluka());
        String imageUrl = shop.getUrl();
        //holder.bind(shop, position);

        Glide.with(holder.circleImageView.getContext())
                .load(shop.getUrl()).centerCrop()
                .placeholder(R.drawable.logo)
                .into(holder.circleImageView);

        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullImage(v,imageUrl);
            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.inflate(R.menu.popup_menu); // Create a menu XML (popup_menu.xml) for your options
                //popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
//                            int position = getAdapterPosition();
//                            PromoteShop clickedShop = filteredShopList.get(position);

                        switch (item.getItemId()) {
                            case R.id.menu_details:
                                // Handle "Details" option
                                // Show details in a dialog or activity
                                Intent intent=new Intent(context,ShopDetails.class);
                                intent.putExtra("image",shop.getUrl());
                                intent.putExtra("ShopName",shop.getShopName());
                                intent.putExtra("Name",shop.getName());
                                intent.putExtra("Address",shop.getAddress());
                                intent.putExtra("contactNumber", shop.getContactNumber());
                                intent.putExtra("PhoneNumber", shop.getPhoneNumber());
                                intent.putExtra("District", shop.getDistrict());
                                intent.putExtra("Taluka", shop.getTaluka());
                                intent.putExtra("url", shop.getUrl());

                                IntentDataHolder.setSharedIntent(intent);

                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                return true;
                            case R.id.menu_remove:
                                // Handle "Remove" option
                                removeShopFromPromoted(shop); // Call the method to remove the shop
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                holder.bluroverlay.setVisibility(View.VISIBLE);
                // Show the popup menu
                popupMenu.show();

            }
        });

       // holder.checkBox.setChecked(itemSelectedState.get(position)); // Update checkbox state
        holder.checkBox.setChecked(selectedItems.contains(position)); // Update checkbox state
        holder.checkBox.setChecked(selectedItems.contains(position));

//        if (promotedShopPositions.contains(position)) {
//            holder.checkBox.setChecked(true);
//            holder.checkBox.setEnabled(true);
//
//        }

        // Set the CheckBox's click listener
        holder.checkBox.setOnClickListener(v -> {
            toggleSelection(position);
            //updateSelectedState(); // Update selection state
            // updateHeaderOverlay();
        });
    }

    @Override
    public int getItemCount() {
        return ShopsList.size();
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
        private View bluroverlay;

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
            bluroverlay = itemView.findViewById(R.id.blurOverlay);
            checkBox.setVisibility(View.GONE);
            checkBox.setOnClickListener(v -> {
                int position = getAdapterPosition();
                boolean isChecked = checkBox.isChecked( );
               // updatePromotionStatus(position, isChecked);

                if (position != RecyclerView.NO_POSITION) {
                    PromoteShop shop = ShopsList.get(position);
                    shop.setSelected(checkBox.isChecked());
                    notifyItemChanged(position);
                }
            });

            // Set up long click listener for item selection
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    toggleSelection(position);
                    return true; // Consume the long click event
                }
                return false;
            });


        }
        public void bind(PromoteShop shop, int position) {
            textViewcontactNumber.setText(shop.getContactNumber());
//            if (promotedShopPositions.contains(position)) {
//                checkBox.setChecked(true);
//            } else {
//                checkBox.setChecked(false);
//            }
        }

    }

    public interface OnItemSelectionChangedListener {
        void onItemSelectionChanged(int selectedItemCount);
    }

    public void setOnItemSelectionChangedListener(CheckedShopsAdapter.OnItemSelectionChangedListener listener) {
        this.itemSelectionChangedListener = listener;
    }

    public void toggleSelection(int position) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position);
        } else {
            selectedItems.add(position);
        }

        notifyDataSetChanged();

        if (itemSelectionChangedListener != null) {
            itemSelectionChangedListener.onItemSelectionChanged(getSelectedItemCount());
        }
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

//    public void updatePromotionStatus(int position, boolean promoted) {
//        if (promoted) {
//            promotedShopPositions.add(position);
//        } else {
//            promotedShopPositions.remove(position);
//        }
//    }
//
//
//    public void updateSelectedState() {
//        for (int i = 0; i < ShopsList.size(); i++) {
//            itemSelectedState.put(i, selectedItems.contains(i));
//        }
//    }

    public void clearSelectedItems() {
        for (int position : selectedItems) {
            notifyItemChanged(position); // Notify the specific item to update its state
        }
        selectedItems.clear();
    }

    public void setSelected(int position) {
        if (selectedItems.contains(position)){
            selectedItems.remove(position);
        } else {
            selectedItems.add(position);
        }
        notifyItemChanged(position);
    }

    public void setSelectedItems(Set<Integer> selectedItems) {
        this.selectedItems.clear();
        this.selectedItems.addAll(selectedItems);
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> selectedPositions = new ArrayList<>(selectedItems);
        return selectedPositions;
    }

//        public List<Integer> getUnselectedItems() {
//            List<Integer> unselectedItems = new ArrayList<>();
//            for (int i = 0; i < selectedItems.size(); i++) {
//                if (!selectedItems.get(i)) {
//                    unselectedItems.add(i);
//                }
//            }
//            return unselectedItems;
//        }
//    public void shuffleData() {
//        Collections.shuffle(ShopsList);
//        Collections.shuffle(filteredShopList);
//        notifyDataSetChanged();
//    }
//    // Create a method to update the filtered list
//    public void setFilteredList(List<PromoteShop> filteredList) {
//        this.filteredShopList = filteredList; // Create a new instance of filtered list to avoid reference issues
//    }
//
//    public void setPromotedShopPositions(Set<Integer> positions) {
//        this.promotedShopPositions.clear();
//        this.promotedShopPositions.addAll(positions);
//        notifyDataSetChanged();
//    }
//    public Set<Integer> getPromotedShopPositions() {
//        return promotedShopPositions;
//    }
//    public int getPromotedShopCount() {
//        return promotedShopPositions.size();
//    }

    private void showFullImage(View anchorView, String imageUrl) {
        // Inflate the popup layout
        View popupView = LayoutInflater.from(context).inflate(R.layout.activity_full_screen, null);

        // Find the ImageView in the popup layout
        ImageView imageView = popupView.findViewById(R.id.popup_image_view);

        // Load the image into the ImageView using Glide
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.logo)
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
    }


    private void removeShopFromPromoted(PromoteShop shop) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = sharedPreferences.getString("mobilenumber", null);
        if (userId != null) {
            // Use the userId as needed
            System.out.println("dffvf  " + userId);
        }
        Log.d("TAG", "onMenuItemClick: " +userId);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);

                    String count = shop.getContactNumber();
                    Log.d("TAG", "countte " +count);
                    DatabaseReference promotedShopsRef = FirebaseDatabase.getInstance().getReference("Shop")
                            .child(contactNumber)
                            .child("promotedShops")
                            .child(shop.getContactNumber());

                    promotedShopsRef.removeValue()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Shop removed successfully
                                    // You can show a success message or update UI if needed
                                    DatabaseReference shopDetailsRef = FirebaseDatabase.getInstance().getReference("Shop")
                                            .child(count)
                                            .child("promotionCount");

                                    DatabaseReference countDetailsRef = FirebaseDatabase.getInstance().getReference("Shop")
                                            .child(count).child("count")
                                            .child("promotionCount");

                                    DatabaseReference promoteShopsRef = FirebaseDatabase.getInstance().getReference("Shop")
                                            .child(count)
                                            .child("hePromoteYou")
                                            .child(contactNumber);

                                    promoteShopsRef.removeValue();

                                    shopDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Integer currentCount = dataSnapshot.getValue(Integer.class);

                                            if (currentCount != null && currentCount > 0) {
                                                currentCount--; // Decrement the count
                                                shopDetailsRef.setValue(currentCount)
                                                        .addOnCompleteListener(countUpdateTask -> {
                                                            if (countUpdateTask.isSuccessful()) {
                                                                // Count decremented successfully
                                                                // You can show a success message or update UI if needed
                                                            } else {
                                                                // Handle error
                                                                // You can show an error message or perform error handling here
                                                            }
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Handle any errors here
                                        }
                                    });
                                    countDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Integer currentCount = dataSnapshot.getValue(Integer.class);

                                            if (currentCount != null && currentCount > 0) {
                                                currentCount--; // Decrement the count
                                                countDetailsRef.setValue(currentCount)
                                                        .addOnCompleteListener(countUpdateTask -> {
                                                            if (countUpdateTask.isSuccessful()) {
                                                                // Count decremented successfully
                                                                // You can show a success message or update UI if needed
                                                            } else {
                                                                // Handle error
                                                                // You can show an error message or perform error handling here
                                                            }
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Handle any errors here
                                        }
                                    });

                                } else {
                                    // Handle error
                                    // You can show an error message or perform error handling here
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }






}
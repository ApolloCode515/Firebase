package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<ItemList> itemList;
    private Context context;
    private static final String USER_ID_KEY = "userID";
    private static SharedPreferences sharedPreferences;
    private static final int CREATE_CATALOG_REQUEST_CODE = 1;

    private OnItemClickListener listener;

    public ItemAdapter(List<ItemList> itemList, Context context, SharedPreferences sharedPreference, OnItemClickListener listener) {
        this.itemList = itemList;
        this.context = context;
        this.listener = listener;
        this.sharedPreferences = sharedPreferences;
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemList item = itemList.get(position);

        holder.nameTextView.setText(item.getName());
        holder.priceTextView.setText(item.getSellPrice());
        holder.sellTextView.setText(item.getPrice());
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

        List<String> imagesUrls = item.getImagesUrls();
        System.out.println("dffgj " +imagesUrls);
        if (imagesUrls != null && !imagesUrls.isEmpty()) {
            // Retrieve the first image URL from the list
            String firstImageUrl = imagesUrls.get(0);


            Glide.with(context)
                    .load(firstImageUrl)
                    .centerCrop()
                    .into(holder.imageView);
        } else {
            // If no images are available, you can set a placeholder image or hide the ImageView.
            // For example:
            // holder.imageView.setImageResource(R.drawable.placeholder_image);
            // or
         //   holder.imageView.setVisibility(View.GONE);
        }

//        // Retrieve the first image URL from the imageUrls map
//        if (item.getFirstImageUrl() != null && !item.getFirstImageUrl().isEmpty()) {
//            String firstImageUrl = item.getFirstImageUrl();
//
//            Glide.with(holder.imageView.getContext())
//                    .load(firstImageUrl).centerCrop()
//                    .into(holder.imageView);
//        }

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    if (listener != null) {
                        listener.onItemClicked(clickedPosition);

                    }
                }
            }
        });


//        holder.update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int clickedPosition = holder.getAdapterPosition();
//                if (clickedPosition != RecyclerView.NO_POSITION) {
//                    ItemList clickedItem = itemList.get(clickedPosition);
//
//                    // Create an Intent to open the ItemDetailsActivity
//                    Intent intent = new Intent(view.getContext(), ItemInformation.class);
//                    intent.putExtra("itemName", clickedItem.getName());
//                    intent.putExtra("itemPrice", clickedItem.getPrice());
//                    intent.putExtra("itemDescription", clickedItem.getDescription());
//                    intent.putExtra("itemKey", clickedItem.getItemkey());
//                    intent.putExtra("firstImageUrl", clickedItem.getFirstImageUrl());
//
//                  //  IntentDataHolder.setSharedIntent(intent);
//                   // intent.putExtra("itemImage", clickedItem.getImageUrl());
////                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    context.startActivity(intent);
//                    // Start the activity
//
//                    IntentItemDataHolder.setSharedItemIntent(intent);
//                    view.getContext().startActivity(intent);
//                }
//            }
//        });

        // Set up OnClickListener for the delete icon
//        holder.itemdelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDeleteDialog(position);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView, sellTextView;
        TextView descriptionTextView;
        ImageView imageView;
        TextView itemkey;
        ImageView itemdelete, update;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemName);
            priceTextView = itemView.findViewById(R.id.itemPrice);
            sellTextView = itemView.findViewById(R.id.itemSell);
            descriptionTextView = itemView.findViewById(R.id.itemDescription);
            imageView = itemView.findViewById(R.id.itemimage);
            itemkey = itemView.findViewById(R.id.itemkey);
            itemdelete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update);
        }
    }

    private String formatPrice(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,##,##0.00", symbols);
        return "₹" + decimalFormat.format(price);
    }

    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Item");
        builder.setMessage("Are you sure you want to delete this item?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Perform the delete action
                deleteItem(position);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    private void deleteItem(int position) {
        // Get the item from the itemList using the position
        ItemList itemToDelete = itemList.get(position);

        // Remove the item from the list
        itemList.remove(position);
        notifyItemRemoved(position);

        // Delete the item from Firebase using its unique identifier
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Shop");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = sharedPreferences.getString("mobilenumber", null);
        if (userId != null) {
            // Use the userId as needed
            System.out.println("dffvf  " + userId);
        }
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
//
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String ContactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    DatabaseReference itemRef = databaseReference.child(ContactNumber).child("items").child(itemToDelete.getItemkey());
                    itemRef.removeValue();

                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference("Shop/" + ContactNumber + "/items/" + itemToDelete.getItemkey() + "/imageUrls/");
                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int x = 0;
                                List<String> imageUrls = new ArrayList<>();
                                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                    String img = String.valueOf(dataSnapshot.child(String.valueOf(i)).getValue());
                                    Log.d("fgsdgfsdgsdf", "" + img);
                                    imageUrls.add(img);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Log.w(TAG, "onCancelled", databaseError.toException());
                        }
                    });

                }


     }

            @Override
            public void onCancelled(DatabaseError error) {

            }

        });
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

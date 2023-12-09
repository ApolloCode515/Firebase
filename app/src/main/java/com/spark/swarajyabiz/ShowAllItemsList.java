package com.spark.swarajyabiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class ShowAllItemsList extends AppCompatActivity {


    private RecyclerView recyclerView;
    private AllItemsAdapter itemAdapter;
    private List<ItemList> itemList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageView back, shopimage;
    String contactNumber,shopName, image;
    TextView shopname ;
    String ContactNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_items_list);

        back = findViewById(R.id.back);
        shopimage = findViewById(R.id.shopimage);
        shopname = findViewById(R.id.shopname);

        recyclerView = findViewById(R.id.viewitems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();  // Populate this list with retrieved data
        // Create and set the adapter
        itemAdapter = new AllItemsAdapter(itemList, this);
        recyclerView.setAdapter(itemAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.Background));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.Background));
            View decorsView = window.getDecorView();
            // Make the status bar icons dark
            decorsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        Intent sharedIntent = IntentDataHolder.getSharedIntent();
        if (sharedIntent != null) {
            ContactNumber = sharedIntent.getStringExtra("contactNumber");
             shopName = sharedIntent.getStringExtra("ShopName");
             image = sharedIntent.getStringExtra("image");
            String itemKey = sharedIntent.getStringExtra("itemKey");

            // Convert the image URI string to a URI object
           // Uri imageUri = Uri.parse(image);
            Log.d("fvfv fff", "" + image);
            shopname.setText(shopName);
            // Load image using Glide
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL); // Optional: Set caching strategy

            Glide.with(this)
                    .load(image).centerCrop()
                    .apply(requestOptions)
                    .into(shopimage);

        }

        FirebaseApp.initializeApp(this);
        String shopName = getIntent().getStringExtra("shopName");
        String shopImage = getIntent().getStringExtra("shopImage");
        String district = getIntent().getStringExtra("district");
        String taluka = getIntent().getStringExtra("taluka");
        String address = getIntent().getStringExtra("address");
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(this)
                .load(shopImage).centerCrop()
                .apply(requestOptions)
                .into(shopimage);
        shopname.setText(shopName);



        String shopcontactNum = getIntent().getStringExtra("contactNumber");
        databaseReference = firebaseDatabase.getReference("Shop").child(shopcontactNum).child("items");
        Log.d("databaseReference", "" + databaseReference);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the shop exists in the database
                if (dataSnapshot.exists()) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        //String itemkey = itemSnapshot.getKey();
                        String itemName = itemSnapshot.child("itemname").getValue(String.class);
                        String price = itemSnapshot.child("price").getValue(String.class);
                        String description = itemSnapshot.child("description").getValue(String.class);
                        String firstImageUrl = itemSnapshot.child("firstImageUrl").getValue(String.class);
                        String itemkey = itemSnapshot.child("itemkey").getValue(String.class);

//                                // Retrieve the first image URL from imageUrls
//                                String firstImageUrl = null;
//                                DataSnapshot imageUrlsSnapshot = itemSnapshot.child("imageUrls");
//                                for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
//                                    firstImageUrl = imageUrlSnapshot.getValue(String.class);
//                                    break;  // Retrieve only the first image URL
//                                }
                        List<String> imageUrls = new ArrayList<>();
                        DataSnapshot imageUrlsSnapshot = itemSnapshot.child("imageUrls");
                        for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                            String imageUrl = imageUrlSnapshot.getValue(String.class);
                            if (imageUrl != null) {
                                imageUrls.add(imageUrl);
                            }
                        }



                        ItemList item = new ItemList(shopName,image, shopcontactNum, itemName, price, description,
                                firstImageUrl, itemkey, imageUrls, district, taluka,address);
                        itemList.add(item);
                    }
                }

                    itemAdapter.notifyDataSetChanged();  // Notify the adapter about the changes  // Notify the adapter about the changes
            }
                @Override
                public void onCancelled (DatabaseError error){

                }
        });


//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        String currentUserUid = firebaseAuth.getCurrentUser().getUid();
//        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserUid);
//
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
//                    //System.out.println("contactnumber " + contactNumber);
//
//                    // Now that you have the contactNumber, retrieve item details
//                    DatabaseReference shopRef = databaseReference.child(contactNumber).child("items");
//                    shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            itemList.clear();  // Clear the list before populating with new data
//
//                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
//                                String itemkey = itemSnapshot.getKey();
//                                String itemName = itemSnapshot.child("itemname").getValue(String.class);
//                                String price = itemSnapshot.child("price").getValue(String.class);
//                                String description = itemSnapshot.child("description").getValue(String.class);
//                                String firstImageUrl = itemSnapshot.child("firstImageUrl").getValue(String.class);
////                                // Retrieve the first image URL from imageUrls
////                                String firstImageUrl = null;
////                                DataSnapshot imageUrlsSnapshot = itemSnapshot.child("imageUrls");
////                                for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
////                                    firstImageUrl = imageUrlSnapshot.getValue(String.class);
////                                    break;  // Retrieve only the first image URL
////                                }
//
//                                ItemList item = new ItemList(itemName, price, description, firstImageUrl, itemkey);
//                                itemList.add(item);
//                            }
//
//                            itemAdapter.notifyDataSetChanged();  // Notify the adapter about the changes
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            // Handle the error gracefully
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle the error gracefully
//            }
//        });

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

            }



            @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
        finish();
    }
}


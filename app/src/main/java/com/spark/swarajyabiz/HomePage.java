package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomePage extends AppCompatActivity implements PostAdapter.PostClickListener{

    private RecyclerView recyclerView;
    private List<Post> postList = new ArrayList<>(); // Create a list to store post details
    private List<ItemList> itemList = new ArrayList<>();
    private PostAdapter postAdapter;
    List<Shop> shopList;
    String shopcontactNumber;
    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        back = findViewById(R.id.back);
        shopList = new ArrayList<>();

        recyclerView = findViewById(R.id.postrecyclerview);
        postAdapter = new PostAdapter(this, itemList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);

        retrievePostDetails(); // Call the method to retrieve post details

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Method to retrieve post details from Firebase
    private void retrievePostDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Shop");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear(); // Clear the existing list before adding new data
                for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {
                    Shop shop = shopSnapshot.getValue(Shop.class);
                    String shopName = shopSnapshot.child("shopName").getValue(String.class);
                    String shopimage = shopSnapshot.child("url").getValue(String.class);
                    shopcontactNumber = shopSnapshot.child("contactNumber").getValue(String.class);
                    String destrict = shopSnapshot.child("district").getValue(String.class);
                    String taluka = shopSnapshot.child("taluka").getValue(String.class);
                    String address = shopSnapshot.child("address").getValue(String.class);
                    System.out.println("rgdfg "+shopName);
                   // postAdapter.setShopName(shopName);

                    DataSnapshot itemsSnapshot = shopSnapshot.child("items");
                    for (DataSnapshot itemSnapshot : itemsSnapshot.getChildren()) {
                        String itemkey = itemSnapshot.getKey();

                        String itemName = itemSnapshot.child("itemname").getValue(String.class);
                        String price = itemSnapshot.child("price").getValue(String.class);
                        String sellprice = itemSnapshot.child("sell").getValue(String.class);
                        String offer = itemSnapshot.child("offer").getValue(String.class);
                        String description = itemSnapshot.child("description").getValue(String.class);
                        String firstimage = itemSnapshot.child("firstImageUrl").getValue(String.class);
                        String wholesale = itemSnapshot.child("wholesale").getValue(String.class);
                        String minqty = itemSnapshot.child("minquantity").getValue(String.class);
                        System.out.println("jfhv " +firstimage);

                        if (TextUtils.isEmpty(firstimage)) {
                            // Set a default image URL here
                            firstimage = String.valueOf(R.drawable.ic_outline_shopping_bag_24);
                        }

                        List<String> imageUrls = new ArrayList<>();
                        DataSnapshot imageUrlsSnapshot = itemSnapshot.child("imageUrls");
                        for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                            String imageUrl = imageUrlSnapshot.getValue(String.class);
                            if (imageUrl != null) {
                                imageUrls.add(imageUrl);
                            }
                        }

                        ItemList item = new ItemList(shopName,shopimage,shopcontactNumber,
                                itemName, price, sellprice, description, firstimage, itemkey, imageUrls, destrict,taluka,address, offer, wholesale, minqty);
                        itemList.add(item);
                    }

                }
                Collections.shuffle(itemList);
                // Notify the adapter that the data has changed
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });


    }

    @Override
    public void onClickClick(int position) {

        String clickedShopcontactNumber = itemList.get(position).getShopcontactNumber();
        Intent intent = new Intent(HomePage.this, ShopDetails.class);
        intent.putExtra("contactNumber", clickedShopcontactNumber);
        startActivity(intent);

    }

    @Override
    public void onContactClick(int position) {

    }

    @Override
    public void onorderClick(int position) {

    }

    @Override
    public void oncallClick(int position) {

    }

    @Override
    public void onseeallClick(int position) {

    }
}


//        ViewPager2 viewPager = findViewById(R.id.viewPager);
//        TabLayout tabLayout = findViewById(R.id.tabLayout);
//
//        TabPageAdapter pagerAdapter = new TabPageAdapter(this);
//        viewPager.setAdapter(pagerAdapter);
//
//        new TabLayoutMediator(tabLayout, viewPager,
//                (tab, position) -> {
//                    switch (position) {
//                        case 0:
//                            tab.setText("Home");
//                            break;
//                        case 1:
//                            tab.setText("Shops");
//                            break;
//                        case 2:
//                            tab.setText("Profile");
//                            break;
//                    }
//                }).attach();
//
//
//    }
//}
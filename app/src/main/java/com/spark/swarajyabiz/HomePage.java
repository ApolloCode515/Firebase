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
    String shopcontactNumber, couponfront, couponback, extraAmt;
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
                        String servingArea = itemSnapshot.child("servingArea").getValue(String.class);
                        String status = itemSnapshot.child("status").getValue(String.class);
                        String itemCate = itemSnapshot.child("itemCate").getValue(String.class);
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
                        String couponStatus = itemSnapshot.child("couponStatus").getValue(String.class);
                        databaseReference.child(itemkey).child("coupons").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    couponfront = snapshot.child("front").getValue(String.class);
                                    couponback = snapshot.child("back").getValue(String.class);
                                    extraAmt = snapshot.child("extraAmt").getValue(String.class);
                                    System.out.println("ergfx " +couponfront);
                                }
                            }

                            @Override
                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                            }
                        });
                        ItemList item = new ItemList();
                        item.setShopName(shopName);
                        item.setShopimage(shopimage);
                        item.setShopcontactNumber(shopcontactNumber);
                        item.setAddress(address);
                        item.setDistrict(destrict);
                        item.setTaluka(taluka);
                        item.setName(itemName);
                        item.setPrice(price);
                        item.setSellPrice(sellprice);
                        item.setDescription(description);
                        item.setFirstImageUrl(firstimage);
                        item.setItemkey(itemkey);
                        item.setImagesUrls(imageUrls);
                        item.setOffer(offer);
                        item.setWholesaleprice(wholesale);
                        item.setMinqty(minqty);
                        item.setServingArea(servingArea);
                        item.setStatus(status);
                        item.setItemCate(itemCate);
                        item.setCouponfront(couponfront);
                        item.setCouponback(couponback);
                        item.setExtraAmt(extraAmt);
                        item.setCouponStatus(couponStatus);

//                                    ItemList item = new ItemList(shopname,shopimage,shopcontactNumber, itemName, price, sellprice, description,
//                                            firstImageUrl, itemkey, imageUrls, destrict, taluka,address, offer, wholesale, minqty, servingArea, status,
//                                            itemCate);
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
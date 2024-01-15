package com.spark.swarajyabiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class PostInfo extends AppCompatActivity implements ShopAdapter.ClickListener {

    String shopname, shopadd, shopImg, postDesc, postImg, contactkey, postType, postCate, postKeys, shopContact, viewCount, clickCount;
    TextView postDec, bizName, bizAdd, postkeys, clickcount, viewcount;
    ImageView bizImg, postimg, back;

    RecyclerView recyclerViewShops;
    ShopAdapter shopAdapter;
    List<Shop> shopList;
    List<Shop> filteredList;

    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info);

        bizName = findViewById(R.id.bizname);
        bizAdd = findViewById(R.id.bizadd);
        bizImg = findViewById(R.id.userImg);
        postDec = findViewById(R.id.postde);
        postimg = findViewById(R.id.postImg);
        postkeys = findViewById(R.id.postkays);
        back = findViewById(R.id.back);
        clickcount = findViewById(R.id.clickcount);
        viewcount = findViewById(R.id.viewcount);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
            View decorsView = window.getDecorView();
            // Make the status bar icons dark
            decorsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }


        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Shop");

         contactkey = getIntent().getStringExtra("contactKey");
         postDesc = getIntent().getStringExtra("postDesc");
         postType = getIntent().getStringExtra("postType");
         postImg = getIntent().getStringExtra("postImg");
         postKeys = getIntent().getStringExtra("postKeys");
         postCate = getIntent().getStringExtra("postCate");
         shopname = getIntent().getStringExtra("shopname");
         shopImg = getIntent().getStringExtra("shopimagex");
         shopadd = getIntent().getStringExtra("shopaddress");
         shopContact = getIntent().getStringExtra("shopContact");
         viewCount = getIntent().getStringExtra("viewcount");
         clickCount = getIntent().getStringExtra("clickcount");

         System.out.println("sdvvre " +postDesc);
         System.out.println("sdvfdd " +postKeys);

         if (postDesc!=null){
             postDec.setVisibility(View.VISIBLE);
         }else {
             postDec.setVisibility(View.GONE);
         }

         if (postDesc!=null){
             postkeys.setVisibility(View.VISIBLE);
         }else {
             postkeys.setVisibility(View.GONE);
         }

         if (postType.equals("Text")){
             postimg.setVisibility(View.GONE);
         }

         System.out.println("sdvdfrg " +shopContact);

        Glide.with(this).load(shopImg).into(bizImg);
        Glide.with(this).load(postImg).into(postimg);
        bizName.setText(shopname);
        bizAdd.setText(shopadd);
        postDec.setText(postDesc);
        postkeys.setText(postKeys);
        viewcount.setText(viewCount);
        clickcount.setText(clickCount);


        // Initialize RecyclerView
        recyclerViewShops = findViewById(R.id.viewdetails);
        recyclerViewShops.setHasFixedSize(true);
        recyclerViewShops.setLayoutManager(new LinearLayoutManager(this));

        shopList = new ArrayList<>();
        filteredList = new ArrayList<>();
        shopAdapter = new ShopAdapter(filteredList, this, this:: onClick);
        recyclerViewShops.setAdapter(shopAdapter);
        readDataFromFirebase();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void readDataFromFirebase() {
        databaseReference.child(shopContact).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing shop list
                shopList.clear();

                // Process the retrieved data
//                for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {

                Boolean profileverify = dataSnapshot.child("profileverified").getValue(Boolean.class);
                // long promotedShopCount = shopSnapshot.child("promotedShops").getChildrenCount();
                System.out.println("sdfdf " + profileverify);
                // Check if profile is verified (true) before adding to the list
                if (profileverify != null && profileverify) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String shopName = dataSnapshot.child("shopName").getValue(String.class);
                    //Log.d("FirebaseData", "shopName: " + shopName);
                    String contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String url = dataSnapshot.child("url").getValue(String.class);
                    String service = dataSnapshot.child("service").getValue(String.class);
                    String taluka = dataSnapshot.child("taluka").getValue(String.class);
                    String district = dataSnapshot.child("district").getValue(String.class);
                    String shopcategory = dataSnapshot.child("shopcategory").getValue(String.class);

                    // Retrieve the count of promoted shops
                    int promotedShopCount = dataSnapshot.child("promotionCount").getValue(Integer.class);
                    int ordercount = dataSnapshot.child("promotionCount").getValue(Integer.class);
                    int requestcount = dataSnapshot.child("promotionCount").getValue(Integer.class);
                    Log.d("TAG", "proc " + contactNumber);

                    List<ItemList> itemList = new ArrayList<>();
                    // Retrieve posts for the current shop
                    DataSnapshot postsSnapshot = dataSnapshot.child("items");
                    for (DataSnapshot itemSnapshot : postsSnapshot.getChildren()) {
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

                        ItemList item = new ItemList(shopName,url,contactNumber, itemName, price, sellprice,
                                description, firstimage, itemkey, imageUrls, district, taluka,address, offer, wholesale, minqty, servingArea, status);
                        itemList.add(item);
                    }



                    // Create a Shop object and add it to the shop list
                    Shop shop = new Shop(name, shopName, contactNumber, address, url, service, district,
                            taluka, promotedShopCount, itemList, ordercount, requestcount,shopcategory);
                    shopList.add(shop);
                }


                // Update the filtered list with the original list
                filteredList.clear();
                filteredList.addAll(shopList);
                // Notify the adapter about the data change
                shopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.e("FirebaseError", "Failed to read value: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onClick(int position) {

    }
}
package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class PostInfo extends AppCompatActivity implements ShopAdapter.ClickListener, IntrestAdapter.OnImgClickListener {

    String shopname, shopadd, shopImg, postDesc, postImg, contactkey, postType, postCate, postKeys, shopContact, viewCount, clickCount,
            userId, currentUserName, couponfront, couponback, extraAmt;
    TextView postDec, bizName, bizAdd, postkeys, clickcount, viewcount, intresttext;
    ImageView bizImg, postimg, back;

    RecyclerView recyclerViewShops, intreastrecyclerView;
    ShopAdapter shopAdapter;
    List<Shop> shopList;
    List<Shop> filteredList;
    RelativeLayout shoplayout;
    DatabaseReference databaseReference, userRef;
    ShimmerTextView proTextView;
    IntrestAdapter intrestAdapter;
    List<IntrestClass> intrestClassList = new ArrayList<>();  // Initialize the list outside the loop
    LinearLayout verifylay, profilelay, callLay;

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
        shoplayout= findViewById(R.id.shoplayout);
        proTextView = findViewById(R.id.proTags);
        intreastrecyclerView = findViewById(R.id.intrestView);
        intresttext = findViewById(R.id.intresttext);
        verifylay = findViewById(R.id.verifyLay);
        profilelay = findViewById(R.id.profileLay);
        callLay = findViewById(R.id.callLay);
        intresttext.setVisibility(View.GONE);

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

        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Shop");
        userRef = FirebaseDatabase.getInstance().getReference("Users");

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

        String[] parts = postCate.split("&&");
        proTextView.setText(parts[0]);
//        proTextView.setTextColor(Color.parseColor("#FFFFFF"));
//        proTextView.setReflectionColor(Color.parseColor("#9C27B0"));
        Shimmer shimmer = new Shimmer();
        shimmer.start(proTextView);

        userRef.child(shopContact).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    boolean premium = snapshot.child("premium").getValue(boolean.class);
                   // currentUserName = snapshot.child("name").getValue(String.class);
                    if (premium){
                        verifylay.setVisibility(View.VISIBLE);
                    }else {
                        verifylay.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });




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
//        viewcount.setText(viewCount);
//        clickcount.setText(clickCount);

        int ViewCount = Integer.parseInt(viewCount);
        viewcount.setText(formatViewCount(ViewCount) +" Views");
        int ClickCount = Integer.parseInt(clickCount);
        clickcount.setText(formatViewCount(ClickCount)+" Clicks");

        // Initialize RecyclerView
        recyclerViewShops = findViewById(R.id.viewdetails);
        recyclerViewShops.setHasFixedSize(true);
        recyclerViewShops.setLayoutManager(new LinearLayoutManager(this));

        shopList = new ArrayList<>();
        filteredList = new ArrayList<>();
        shopAdapter = new ShopAdapter(filteredList, this, this:: onClick);
        recyclerViewShops.setAdapter(shopAdapter);
        readDataFromFirebase();
        retrieveIntrest();
        notification();

        profilelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShopDetails.class);
                intent.putExtra("contactNumber",shopContact);
                startActivity(intent);
            }
        });

        callLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to initiate a phone call
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shopContact));
                startActivity(callIntent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static String formatViewCount(int viewCount) {
        if (viewCount < 1000) {
            // If the view count is less than 1000, simply return the original count
            return String.valueOf(viewCount);
        } else if (viewCount < 1000000) {
            // If the view count is between 1000 and 999,999, format it as "x.yk"
            return String.format(Locale.getDefault(), "%.1fk", viewCount / 1000.0);
        } else {
            // If the view count is a million or more, format it as "xm"
            return String.format(Locale.getDefault(), "%dm", viewCount / 1000000);
        }
    }
    @Override
    public void onClick(int position) {

    }

    private void notification(){

        if (!userId.equals(shopContact)) {
            userRef.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        currentUserName = snapshot.child("name").getValue(String.class);
                    }
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                }
            });

// Check if the notification already exists for the currentusercontactNum
            DatabaseReference notificationRef = databaseReference.child(shopContact).child("notification");
            System.out.println("w4tegdvzx " + notificationRef);
            String key = notificationRef.push().getKey();
            notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        String message = currentUserName + " view your post.";
                        // Create a map to store the message
                        Map<String, Object> notificationData = new HashMap<>();
                        notificationData.put("message", message);
                        notificationData.put("contactNumber", userId);
                        notificationData.put("key", userId);

                        // Store the message under the currentusercontactNum
                        notificationRef.child(key).setValue(notificationData);

                        // Increment the notification count for the shop
                        DatabaseReference shopNotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                                .child(shopContact)
                                .child("notificationcount");

                        DatabaseReference NotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                                .child(shopContact).child("count")
                                .child("notificationcount");


                        // Read the current count and increment it by 1
                        shopNotificationCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int currentCount = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
                                int newCount = currentCount + 1;

                                // Update the notification count
                                shopNotificationCountRef.setValue(newCount);
                                NotificationCountRef.setValue(newCount);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle onCancelled event
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled event
                }
            });
        }
    }

    private void readDataFromFirebase() {
        databaseReference.child(contactkey).addValueEventListener(new ValueEventListener() {
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
                        item.setShopimage(url);
                        item.setShopcontactNumber(contactNumber);
                        item.setAddress(address);
                        item.setDistrict(district);
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

    public void retrieveIntrest() {
        System.out.println("3rgedv " + shopContact);



        userRef.child(shopContact).child("FavBusiness").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String favBusinessKey = dataSnapshot.getKey();
                        Log.d("sgvcf", favBusinessKey);

                        // Check if other contact numbers have the same favBusinessKey
                        checkAndRetrieveProfileUrl(favBusinessKey);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }

    private void checkAndRetrieveProfileUrl(String favBusinessKey) {
        userRef.orderByChild("FavBusiness/" + favBusinessKey).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String contactNumber = userSnapshot.getKey();
                        Log.d("rgfergwefd", contactNumber);

                        // Check if the contact number is present in the "shop" node
                        if (!shopContact.equals(contactNumber)) {
                            checkShopNode(contactNumber);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }

    private void checkShopNode(String contactNumber) {
        databaseReference.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot shopSnapshot) {
                if (shopSnapshot.exists()) {
                    // If the contact number is present in the "shop" node, retrieve the shop URL
                    String shopImageUrl = shopSnapshot.child("url").getValue(String.class);
                    Log.d("4rwgr5gerg", shopImageUrl);

                    // Check if the image is already in the list
                    if (!isShopImageInList(shopImageUrl)) {
                        IntrestClass intrestClass = new IntrestClass();
                        intrestClass.setShopImage(shopImageUrl);
                        intrestClass.setShopContactNumber(contactNumber);
                        intrestClassList.add(intrestClass);

                        System.out.println("svdcf " +contactNumber);

                        // Create the adapter outside the loop and set it after the loop completes
                        intrestAdapter = new IntrestAdapter(PostInfo.this, intrestClassList, PostInfo.this);
                        intreastrecyclerView.setLayoutManager(new GridLayoutManager(PostInfo.this, 4)); // Change 3 to the desired number of columns
                        intreastrecyclerView.setAdapter(intrestAdapter);
                        intresttext.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }

    private boolean isShopImageInList(String shopImageUrl) {
        for (IntrestClass intrestClass : intrestClassList) {
            if (intrestClass.getShopImage() != null && intrestClass.getShopImage().equals(shopImageUrl)) {
                return true;  // Shop image already in the list
            }
        }
        return false;  // Shop image not in the list
    }

    @Override
    public void ImgClick(int position) {

        // Get the selected item from the list
        IntrestClass selectedShop = intrestClassList.get(position);

        // Create an Intent to start the ShopDetails activity
        Intent intent = new Intent(getApplicationContext(), ShopDetails.class);

        // Pass relevant data to the ShopDetails activity using Intent extras
        intent.putExtra("contactNumber", selectedShop.getShopContactNumber());
        intent.putExtra("shopImageUrl", selectedShop.getShopImage());

        // Start the ShopDetails activity
        startActivity(intent);

    }
}
package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
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
import okhttp3.internal.cache.DiskLruCache;

public class CreateCatalogList extends AppCompatActivity implements ItemAdapter.OnItemClickListener {

    RelativeLayout additem, createcatlogtext;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<ItemList> itemList;
    EditText itemname;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    String contactNumber, couponfront, couponback, extraAmt;
    ImageView back, catalogshopimage;
    TextView catalogshopname,newCpn;
    private static final int CREATE_CATALOG_REQUEST_CODE = 1; // You can choose any integer value

    private static final String USER_ID_KEY = "userID";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_catalog_list);

        additem = findViewById(R.id.additem);
        itemname = findViewById(R.id.itemnames);
        back = findViewById(R.id.back);
        catalogshopname = findViewById(R.id.catalogshopname);
        catalogshopimage= findViewById(R.id.catalogshopimage);
        createcatlogtext = findViewById(R.id.createcatlogtext);
        newCpn = findViewById(R.id.newCpn11s);
        createcatlogtext.setVisibility(View.GONE);

        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

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


        recyclerView = findViewById(R.id.viewitems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();  // Populate this list with retrieved data
        // Create and set the adapter
        itemAdapter = new ItemAdapter(itemList, this, sharedPreference, this);
        recyclerView.setAdapter(itemAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        Intent sharedIntent = IntentDataHolder.getSharedIntent();
        if (sharedIntent != null) {
            contactNumber = sharedIntent.getStringExtra("contactNumber");
        }

        Log.d("imageUri", "" + contactNumber);


        FirebaseApp.initializeApp(this);
        databaseReference = firebaseDatabase.getReference("Shop");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
//
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                   String ContactNumber = dataSnapshot.child("contactNumber").getValue(String.class);

                   databaseReference.child(ContactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot snapshot) {
                           String shopimage = snapshot.child("url").getValue(String.class);
                           String shopname = snapshot.child("shopName").getValue(String.class);
                           String shopcontactNumber = snapshot.child("contactNumber").getValue(String.class);
                           String destrict = snapshot.child("district").getValue(String.class);
                           String taluka = snapshot.child("taluka").getValue(String.class);
                           String address = snapshot.child("address").getValue(String.class);

                            catalogshopname.setText(shopname);
                           RequestOptions requestOptions = new RequestOptions()
                                   .diskCacheStrategy(DiskCacheStrategy.ALL); // Optional: Set caching strategy

                           Glide.with(CreateCatalogList.this)
                                   .load(shopimage).centerCrop()
                                   .diskCacheStrategy(DiskCacheStrategy.DATA)
                                   .transition(DrawableTransitionOptions.withCrossFade())
                                   .apply(requestOptions)
                                   .into(catalogshopimage);

                    //System.out.println("contactnumber " + contactNumber);
//                    DatabaseReference shopref = databaseReference.child(ContactNumber).child("items");
                     DatabaseReference shopref = FirebaseDatabase.getInstance().getReference("Products").child(ContactNumber);
                    // Now that you have the contactNumber, retrieve item details
                     shopref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            itemList.clear();  // Clear the list before populating with new data
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                    //String itemkey = itemSnapshot.getKey();
                                    String itemName = itemSnapshot.child("itemname").getValue(String.class);
                                    String price = itemSnapshot.child("price").getValue(String.class);
                                    String sellprice = itemSnapshot.child("sell").getValue(String.class);
                                    String offer = itemSnapshot.child("offer").getValue(String.class);
                                    String description = itemSnapshot.child("description").getValue(String.class);
                                    String firstImageUrl = itemSnapshot.child("firstImageUrl").getValue(String.class);
                                    String itemkey = itemSnapshot.child("itemkey").getValue(String.class);
                                    String wholesale = itemSnapshot.child("wholesale").getValue(String.class);
                                    String minqty = itemSnapshot.child("minquantity").getValue(String.class);
                                    String servingArea = itemSnapshot.child("servingArea").getValue(String.class);
                                    String status = itemSnapshot.child("status").getValue(String.class);
                                    String itemCate = itemSnapshot.child("itemCate").getValue(String.class);


                                    List<String> imageUrls = new ArrayList<>();
                                    DataSnapshot imageUrlsSnapshot = itemSnapshot.child("imageUrls");
                                    for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                                        String imageUrl = imageUrlSnapshot.getValue(String.class);
                                        if (imageUrl != null) {
                                            imageUrls.add(imageUrl);
                                        }
                                    }

                                    DataSnapshot couponSnap = itemSnapshot.child("coupons");
                                    for (int i=0; i<couponSnap.getChildrenCount();i++){
                                        couponfront = couponSnap.child("front").getValue(String.class);
                                        couponback = couponSnap.child("back").getValue(String.class);
                                        extraAmt = couponSnap.child("extraAmt").getValue(String.class);
                                        System.out.println("rgsvc "+shopref.child(itemkey).child("coupons").toString());
                                    }
                                    String couponStatus = itemSnapshot.child("CurrentCpnId").getValue(String.class);
                                    System.out.println("wrds " +couponStatus);

//                                    ItemList item = new ItemList();
//                                    item.setShopName(shopname);
//                                    item.setShopimage(shopimage);
//                                    item.setShopcontactNumber(shopcontactNumber);
//                                    item.setAddress(address);
//                                    item.setDistrict(destrict);
//                                    item.setTaluka(taluka);
//                                    item.setName(itemName);
//                                    item.setPrice(price);
//                                    item.setSellPrice(sellprice);
//                                    item.setDescription(description);
//                                    item.setFirstImageUrl(firstImageUrl);
//                                    item.setItemkey(itemkey);
//                                    item.setImagesUrls(imageUrls);
//                                    item.setOffer(offer);
//                                    item.setWholesaleprice(wholesale);
//                                    item.setMinqty(minqty);
//                                    item.setServingArea(servingArea);
//                                    item.setStatus(status);
//                                    item.setItemCate(itemCate);
//                                    item.setCouponfront(couponfront);
//                                    item.setCouponback(couponback);
//                                    item.setExtraAmt(extraAmt);
//                                    item.setCouponStatus(couponStatus);
                                    System.out.println("ergfx " +couponfront);
                                    ItemList item = new ItemList(shopname,shopimage,shopcontactNumber, itemName, price, sellprice, description,
                                            firstImageUrl, itemkey, imageUrls, destrict, taluka,address, offer, wholesale, minqty, servingArea, status,
                                            itemCate, couponfront, couponback, extraAmt, couponStatus);
                                    itemList.add(item);
                                    itemAdapter.notifyDataSetChanged();  // Notify the adapter about the changes
                                    // Check if itemList is empty
                                    if (itemList.isEmpty()) {
                                        // If itemList is empty, show the createcatlogtext TextView
                                        createcatlogtext.setVisibility(View.VISIBLE);
                                    } else {
                                        // If itemList is not empty, hide the createcatlogtext TextView
                                        createcatlogtext.setVisibility(View.GONE);
                                    }
                                }
                            }




                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle the error gracefully
                        }
                    });
                       }

                       @Override
                       public void onCancelled(DatabaseError error) {

                       }
                   });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error gracefully
            }
        });

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), AddItems.class));
                // Start createcatlogpage and specify a request code
                Intent intent = new Intent(CreateCatalogList.this, AddItems.class);
                startActivityForResult(intent, CREATE_CATALOG_REQUEST_CODE);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(CreateCatalogList.this, FragmentProfile.class); // Replace "PreviousActivity" with the appropriate activity class
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
                finish(); // Finish the current activity
            }
        });

        newCpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(CreateCatalogList.this,CouponMaker.class);
                startActivity(intent1);
            }
        });

    }
    @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
//        Intent intent = new Intent(CreateCatalogList.this, Profile.class); // Replace "PreviousActivity" with the appropriate activity class
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
        finish(); // Finish the current activity

    }

    @Override
    public void onItemClicked(int position) {

        ItemList clickedItem = itemList.get(position);
        Intent intent = new Intent(this, ItemInformation.class);
        intent.putExtra("itemName", clickedItem.getName());
        intent.putExtra("itemPrice", clickedItem.getPrice());
        intent.putExtra("itemSell", clickedItem.getSellPrice());
        intent.putExtra("itemDescription", clickedItem.getDescription());
        intent.putExtra("itemKey", clickedItem.getItemkey());
        intent.putExtra("firstImageUrl", clickedItem.getFirstImageUrl());
        intent.putExtra("wholesale", clickedItem.getWholesaleprice());
        intent.putExtra("minquantity", clickedItem.getMinqty());
        intent.putExtra("servingArea", clickedItem.getServingArea());
        intent.putExtra("itemCate", clickedItem.getItemCate());
        intent.putExtra("couponfront", clickedItem.getCouponfront());
        intent.putExtra("couponback", clickedItem.getCouponback());
        intent.putExtra("extraAmt", clickedItem.getExtraAmt());
        intent.putExtra("CurrentCpnId", clickedItem.getCouponStatus());

        System.out.println("34rgs " +clickedItem.getCouponback());
        startActivityForResult(intent, CREATE_CATALOG_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_CATALOG_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // A new item has been added, refresh the RecyclerView
                // itemList.clear(); // Clear the existing data
                // Populate itemList with updated data from Firebase
                recreate();
                itemAdapter.notifyDataSetChanged(); // Refresh the RecyclerView
            }
        }
    }
}


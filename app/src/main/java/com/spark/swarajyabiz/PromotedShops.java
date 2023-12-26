package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class PromotedShops extends AppCompatActivity {

    String contactNumber;
    ImageView back;
    private static final String USER_ID_KEY = "userID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promoted_shops);

        back=findViewById(R.id.back);

        RecyclerView recyclerView = findViewById(R.id.viewshops);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<PromoteShop> shopList = new ArrayList<>();
        AllPromotedShopAdapter promoteAdapter = new AllPromotedShopAdapter(PromotedShops.this, shopList);
        recyclerView.setAdapter(promoteAdapter);

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



        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference().child("Shop");
        DatabaseReference userRef = database.getReference("Users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
       String userId = sharedPreference.getString(USER_ID_KEY, null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

        Intent sharedIntent = IntentDataHolder.getSharedIntent();
        if (sharedIntent != null) {
            contactNumber = sharedIntent.getStringExtra("contactNumber");
            System.out.println("ContactNumbersss " + contactNumber);
            if (contactNumber != null) {

                DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("Shop").child(contactNumber);

                shopRef.child("promotedShops").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        shopList.clear();  // Clear the list before populating with new data
                        for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {
                            // Parse shop details from the snapshot
                            String name = shopSnapshot.child("name").getValue(String.class);
                            String shopName = shopSnapshot.child("shopName").getValue(String.class);
                            Log.d("FirebaseData", "shopName: " + shopName);
                            String contactNumber = shopSnapshot.child("contactNumber").getValue(String.class);
                            String address = shopSnapshot.child("address").getValue(String.class);
                            String url = shopSnapshot.child("url").getValue(String.class);
                            String service = shopSnapshot.child("service").getValue(String.class);
                            String taluka = shopSnapshot.child("taluka").getValue(String.class);
                            String district = shopSnapshot.child("district").getValue(String.class);
                            System.out.println("retrive " + taluka);
                           // boolean isShopPromoted = shopSnapshot.child(shopName).exists();
                            int promotedShopCount = shopSnapshot.child("promotionCount").getValue(Integer.class);
                            // Create a Shop object and add it to the shop list
                            PromoteShop shop = new PromoteShop(name, shopName, contactNumber, address, url, service, district, taluka
                                    , promotedShopCount);
                            shopList.add(shop);
                        }
                        promoteAdapter.notifyDataSetChanged();  // Notify the adapter about the changes
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error gracefully
                    }
                });
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
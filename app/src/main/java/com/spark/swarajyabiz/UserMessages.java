package com.spark.swarajyabiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
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

public class UserMessages extends AppCompatActivity {

    DatabaseReference databaseRef, userRef, orderRef;
    String userId, shopContactNumber, keys, currentUserContactNumber;
    RecyclerView recyclerView;
    MessagesAdapter orderAdapter;
    List<OrderKey> orderList;
    Order order;
    private List<String> orderKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messages);


        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference().child("Shop");
        userRef = database.getReference("Users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();


        recyclerView = findViewById(R.id.messagedetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        orderAdapter = new MessagesAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);

        Intent sharedIntent = IntentDataHolder.getSharedIntent();
        if (sharedIntent != null) {
            shopContactNumber = sharedIntent.getStringExtra("contactNumber");
            String shopName = sharedIntent.getStringExtra("ShopName");
            String name = sharedIntent.getStringExtra("Name");
            String district = sharedIntent.getStringExtra("District");

        }

        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUserContactNumber = dataSnapshot.child("contactNumber").getValue(String.class);


                    databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {
                                    DataSnapshot ordersSnapshot = shopSnapshot.child("orders");
                                    String key = shopSnapshot.getKey();
                                    orderRef = databaseRef.child(key).child("orders");
                                    System.out.println("tgrg " + orderRef);

                                    orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                orderKeys.clear();
                                                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                                                    keys = orderSnapshot.getKey();
                                                    System.out.println("rtgyrefg" + keys);
                                                    orderList.add(new OrderKey(keys, order)); // Add the order key to the list

                                                    // Notify the adapter that the data has changed
                                                    orderAdapter.notifyDataSetChanged();




//                                                  if (currentUserContactNumber.equals(keys)) {
//                                                        DatabaseReference userorderRef = orderRef.child(keys);
//                                                        System.out.println("sfgf " + userorderRef);
//                                                        userorderRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot Ordersnapshot) {
//                                                                if (Ordersnapshot.exists()) {
//
//                                                                    for (DataSnapshot orderSnapshot : Ordersnapshot.getChildren()) {
//                                                                        if (orderSnapshot.hasChild("itemName") &&
//                                                                                orderSnapshot.hasChild("firstImageUrl") &&
//                                                                                orderSnapshot.hasChild("buyerContactNumber") &&
//                                                                                orderSnapshot.hasChild("buyerName") &&
//                                                                                orderSnapshot.hasChild("quantity") &&
//                                                                                orderSnapshot.hasChild("status") &&
//                                                                                orderSnapshot.hasChild("timestamp")) {
//
//                                                                            String itemName = orderSnapshot.child("itemName").getValue(String.class);
//                                                                            System.out.println("efygfe " + itemName);
//                                                                            String firstImageUrl = orderSnapshot.child("firstImageUrl").getValue(String.class);
//                                                                            String buyerContactNumber = orderSnapshot.child("buyerContactNumber").getValue(String.class);
//                                                                            String buyerName = orderSnapshot.child("buyerName").getValue(String.class);
//                                                                            String quantity = orderSnapshot.child("quantity").getValue(String.class);
//                                                                            String status = orderSnapshot.child("status").getValue(String.class);
//                                                                            Object timestamp = orderSnapshot.child("timestamp").getValue();
//                                                                            Object datetamp = orderSnapshot.child("datetamp").getValue();
//                                                                            String orderKey = orderSnapshot.child("orderkey").getValue(String.class);
//                                                                            String shopOwnerContactNumber = orderSnapshot.child("shopOwnerContactNumber").getValue(String.class);
//                                                                            String shopimage = orderSnapshot.child("shopImage").getValue(String.class);
//
//                                                                            System.out.println("sfhgf " + buyerContactNumber);// Add more fields as needed
//                                                                            // Create an Order object and add it to the list
//                                                                            Order order = new Order(itemName, firstImageUrl, buyerContactNumber, buyerName, quantity, status, timestamp,
//                                                                                    datetamp, orderKey, shopOwnerContactNumber, shopimage);
//                                                                            orderList.add(order);
//                                                                        }
//                                                                    }
//                                                                }
//                                                                orderAdapter.notifyDataSetChanged();
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//                                                            }
//                                                        });
//
//                                                    }

                                                }
                                                orderAdapter.notifyDataSetChanged();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                orderAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Handle errors
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle errors
            }
        });

        ImageView back = findViewById(R.id.back);
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
    }


}
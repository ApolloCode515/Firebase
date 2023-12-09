package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class OrderHistory extends AppCompatActivity implements HistoryAdapter.OrderClickListener {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, usersRef;
    FirebaseAuth firebaseAuth;

    String contactNumber, senderID;
    ImageView back;
    TextView history, clearhistory;

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<orders> ordersList;
    private static final String USER_ID_KEY = "userID";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        back = findViewById(R.id.back);
        clearhistory = findViewById(R.id.clear);

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

        recyclerView = findViewById(R.id.orderhistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ordersList = new ArrayList<>();
        adapter = new HistoryAdapter(ordersList, this);
        recyclerView.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseApp.initializeApp(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Shop");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
       String userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }
        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        clearhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderHistory.this);
                builder.setTitle("History Clear");
                builder.setMessage("Are you sure you want to clear history?");
                builder.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle the delete action here
                        // You can put your Firebase database delete code here

                        // Example code to delete a node in Firebase Realtime Database
                        databaseReference.child(contactNumber).child("history").removeValue();
                        Log.d("TAG", "onClick: " +contactNumber);
                        // Close the dialog
                        dialogInterface.dismiss();
                        // Redirect to the business page
                        Intent intent = new Intent(OrderHistory.this, OrderLists.class); // Replace "PreviousActivity" with the appropriate activity class
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish(); // Finish the current activity
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked on Cancel, so just close the dialog
                        dialogInterface.dismiss();
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

        @Override
    public void onRemoveClick(int position) {
        orders order = ordersList.get(position);
//        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Shop")
//                .child(contactNumber) // Replace with the appropriate reference
//                .child("history")
//                .child(order.getKey()); // Use the unique key to reference the order
//
//
//            DatabaseReference historychatRef = FirebaseDatabase.getInstance().getReference("Shop")
//                    .child(contactNumber) // Replace with the appropriate reference
//                    .child("history").child(key).child("chats")
//                    .child(order.getKey()); // Use the same unique key to reference the order in history
//
//            DatabaseReference historyButtonChatsRef = FirebaseDatabase.getInstance().getReference("Shop")
//                    .child(contactNumber)
//                    .child("history")
//                    .child(key)
//                    .child("buttonchats")
//                    .child(order.getKey());
//
//            orderRef.removeValue();yy

            DatabaseReference shopRef = databaseReference.child(contactNumber).child("history");
            shopRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                    ordersList.clear();
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {

                        String key = orderSnapshot.getKey();
                        System.out.println("fdd " + key);
                        ordersList.clear();

                        DatabaseReference historyorderRef = FirebaseDatabase.getInstance().getReference("Shop")
                                .child(contactNumber) // Replace with the appropriate reference
                                .child("history").child(key)
                                .child(order.getKey()); // Use the same unique key to reference the order in history

                        DatabaseReference historychatRef = FirebaseDatabase.getInstance().getReference("Shop")
                                .child(contactNumber) // Replace with the appropriate reference
                                .child("history").child(key).child("chats")
                                .child(order.getKey()); // Use the same unique key to reference the order in history

                        DatabaseReference historyButtonChatsRef = FirebaseDatabase.getInstance().getReference("Shop")
                                .child(contactNumber)
                                .child("history")
                                .child(key)
                                .child("buttonchats")
                                .child(order.getKey());

                        DatabaseReference userorderRef = FirebaseDatabase.getInstance().getReference("Users")
                                .child(senderID)
                                .child("ordered") // Replace with the appropriate reference
                                .child(contactNumber)
                                .child(order.getKey()); // Use the same unique key to reference the order in history

                        DatabaseReference userchatRef = FirebaseDatabase.getInstance().getReference("Users")
                                .child(senderID)
                                .child("ordered") // Replace with the appropriate reference
                                .child(contactNumber).child("chats")
                                .child(order.getKey()); // Use the same unique key to reference the order in history

                        DatabaseReference userButtonChatsRef = FirebaseDatabase.getInstance().getReference("Users")
                                .child(senderID)
                                .child("ordered") // Replace with the appropriate reference
                                .child(contactNumber)
                                .child("buttonchats")
                                .child(order.getKey());


//                        historyorderRef.removeValue();
//                        historychatRef.removeValue();
//                        historyButtonChatsRef.removeValue();
//                        userorderRef.removeValue();
//                        userchatRef.removeValue();
//                        userButtonChatsRef.removeValue();

                        historyorderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Get the order data
                                    Map<String, Object> orderData = (Map<String, Object>) dataSnapshot.getValue();

                                    // Move the order data to history
                                    historyorderRef.setValue(orderData).addOnCompleteListener(orderMoveTask -> {
                                        if (orderMoveTask.isSuccessful()) {
                                            // Order data successfully moved to history
                                            // Now, remove the order from the "orders" node
                                            historyorderRef.removeValue().addOnCompleteListener(orderRemoveTask -> {
                                                if (orderRemoveTask.isSuccessful()) {
                                                    // Order successfully removed from "orders" node
                                                    // Notify the adapter of data changes
                                                    // decrementOrderCountForShop(order.getKey());
                                                    ///  ordersList.remove(position);
                                                    adapter.notifyDataSetChanged();
                                                } else {
                                                    // Handle the case where removing the order fails
                                                }
                                            });

                                            // Move the chats data to history
                                            historychatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot chatSnapshot) {
                                                    if (chatSnapshot.exists()) {
                                                        Map<String, Object> chatData = (Map<String, Object>) chatSnapshot.getValue();
                                                        historychatRef.setValue(chatData).addOnCompleteListener(chatMoveTask -> {
                                                            if (chatMoveTask.isSuccessful()) {
                                                                // Chats data successfully moved to history
                                                                // Now, remove the chats data
                                                                historychatRef.removeValue();
                                                                //    ordersList.remove(position);
                                                                adapter.notifyDataSetChanged();
                                                            } else {
                                                                // Handle the case where moving chats to history fails
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    // Handle onCancelled event
                                                }
                                            });

                                            // Move the buttonchats data to history
                                            historyButtonChatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot buttonChatSnapshot) {
                                                    if (buttonChatSnapshot.exists()) {
                                                        Map<String, Object> buttonChatData = (Map<String, Object>) buttonChatSnapshot.getValue();
                                                        historyButtonChatsRef.setValue(buttonChatData).addOnCompleteListener(buttonChatMoveTask -> {
                                                            if (buttonChatMoveTask.isSuccessful()) {
                                                                // Buttonchats data successfully moved to history
                                                                // Now, remove the buttonchats data
                                                                historyButtonChatsRef.removeValue();
                                                                // ordersList.remove(position);
                                                                adapter.notifyDataSetChanged();
                                                            } else {
                                                                // Handle the case where moving buttonchats to history fails
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    // Handle onCancelled event
                                                }
                                            });
                                        } else {
                                            // Handle the case where moving the order to history fails
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Handle onCancelled event
                            }
                        });

                    }
                }
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                    // Handle onCancelled event
                }
            });

    }

    @Override
    public void onContactClick(int position) {
        orders order = ordersList.get(position);
        String contactNumber = order.getBuyerContactNumber();
        String orderkey = order.getKey();
        System.out.println("safdgb " +orderkey);
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
//        dialIntent.setData(Uri.parse("tel:" + contactNumber));
        boolean isBottonCardVisible = false;
        Intent orderDetailsIntent = new Intent(getApplicationContext(), OrderDetails.class);
        orderDetailsIntent.putExtra("isBottonCardVisible", isBottonCardVisible);

        orderDetailsIntent.putExtra("buyerContactNumber", contactNumber);
        orderDetailsIntent.putExtra("orderkey", orderkey);

        startActivity(orderDetailsIntent);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Clear the existing orders list to avoid duplicates
        ordersList.clear();

        // Fetch and populate the orders list again
        fetchOrdersAndPopulateList();
    }

    private void fetchOrdersAndPopulateList() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    DatabaseReference shopRef = databaseReference.child(contactNumber).child("history");
                    System.out.println("items " + shopRef);
                    ordersList.clear();
                    shopRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {

                                String  keys = orderSnapshot.getKey();
                                System.out.println("fhuyfg " +keys);
                                DatabaseReference userordersRef = shopRef.child(keys);

                                userordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {

                                            if (!orderSnapshot.getKey().equals("buttonchats") &&
                                                    !orderSnapshot.getKey().equals("chats")) {
                                                String itemName = orderSnapshot.child("itemName").getValue(String.class);
                                                System.out.println("efygfe " + itemName);
                                                String firstImageUrl = orderSnapshot.child("firstImageUrl").getValue(String.class);
                                                String buyerContactNumber = orderSnapshot.child("buyerContactNumber").getValue(String.class);
                                                String buyerName = orderSnapshot.child("buyerName").getValue(String.class);
                                                String quantity = orderSnapshot.child("quantity").getValue(String.class);
                                                String status = orderSnapshot.child("status").getValue(String.class);
                                                String timestamp = orderSnapshot.child("timestamp").getValue(String.class);
                                                String datetamp = orderSnapshot.child("datetamp").getValue(String.class);
                                                String orderKey = orderSnapshot.child("orderkey").getValue(String.class);
                                                String shopOwnerContactNumber = orderSnapshot.child("shopOwnerContactNumber").getValue(String.class);
                                                String shopimage = orderSnapshot.child("shopImage").getValue(String.class);
                                                senderID = orderSnapshot.child("senderID").getValue(String.class);
                                                String reciverID = orderSnapshot.child("receiverID").getValue(String.class);
                                                orders order = new orders(itemName, buyerName, buyerContactNumber, orderKey, datetamp, timestamp, quantity, shopimage,
                                                        shopOwnerContactNumber, shopOwnerContactNumber, firstImageUrl, senderID, reciverID);

                                                ordersList.add(order);
                                            }

//                                            if (orderSnapshot.hasChild("itemName") &&
//                                                    orderSnapshot.hasChild("firstImageUrl") &&
//                                                    orderSnapshot.hasChild("buyerContactNumber") &&
//                                                    orderSnapshot.hasChild("buyerName") &&
//                                                    orderSnapshot.hasChild("quantity") &&
//                                                    orderSnapshot.hasChild("status") &&
//                                                    orderSnapshot.hasChild("timestamp")) {
//                                                orders order = orderSnapshot.getValue(orders.class);
//                                                senderID = orderSnapshot.child("senderID").getValue(String.class);
//                                                String keyss = orderSnapshot.child("timestamp").getValue(String.class);
//                                                System.out.println("fhuyfg " + keyss);
//                                                order.setKey(orderSnapshot.getKey()); // Set the unique key from Firebase
//                                                ordersList.add(order);
//                                            }
                                        }
                                        // Reverse the order of the list to show recent items first
                                        Collections.reverse(ordersList);
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

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
package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class OrderLists extends AppCompatActivity implements OrdersAdapter.OrderClickListener{

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, usersRef, shopRef;
    String contactNumber, keys, senderID, notificationorderkey, orderkey;
    ImageView back;
    TextView history, orderinfotextview;
    Button boostbutton;

    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private List<orders> ordersList;
    private static final String USER_ID_KEY = "userID";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_lists);

        back = findViewById(R.id.back);
        history = findViewById(R.id.history);
        orderinfotextview = findViewById(R.id.orderinfo);
        boostbutton = findViewById(R.id.boostbutton);

        recyclerView = findViewById(R.id.orderdetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ordersList = new ArrayList<>();
        adapter = new OrdersAdapter(ordersList, this);
        recyclerView.setAdapter(adapter);

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

//        // Fetch orders data from Firebase
//        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Shop")
//                .child(contactNumber) // Replace with the appropriate reference
//                .child("orders");

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


        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OrderHistory.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        contactNumber = getIntent().getStringExtra("contactNumber");
        System.out.println("grff " +contactNumber);
        databaseReference.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //String shopimage = snapshot.child("url").getValue(String.class);
                    String shopimage = getIntent().getStringExtra("shopimage");
                    String shopname = getIntent().getStringExtra("shopName");
                    System.out.println("dfgdsdgvgv " +shopname);

                    if (ordersList.isEmpty()) {
                        // No orders present, show the "orderinfo" TextView and "boostButton"
                        orderinfotextview.setVisibility(View.VISIBLE);
                        boostbutton.setVisibility(View.VISIBLE);
                        boostprofile(shopimage, shopname);
                    } else {
                        // Orders are present, hide the "orderinfo" TextView and "boostButton"
                        orderinfotextview.setVisibility(View.GONE);
                        boostbutton.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });

    }

//    @Override
//    public void onRemoveClick(int position) {
//        orders order = ordersList.get(position);
//        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Shop")
//                .child(contactNumber) // Replace with the appropriate reference
//                .child("orders")
//                .child(order.getKey()); // Use the unique key to reference the order
//        orderRef.removeValue();
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public void onRemoveClick(int position) {

            orders order = ordersList.get(position);
            orderkey = order.getKey();
            DatabaseReference shopRef = databaseReference.child(contactNumber).child("orders");
            shopRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                    ordersList.clear();
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        String key = orderSnapshot.getKey();
                        System.out.println("fdd " + key);
                        //  ordersList.clear();

                        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Shop")
                                .child(contactNumber) // Replace with the appropriate reference
                                .child("orders").child(key)
                                .child(order.getKey()); // Use the unique key to reference the order


                        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("Shop")
                                .child(contactNumber) // Replace with the appropriate reference
                                .child("orders").child(key).child("chats")
                                .child(order.getKey()); // Use the unique key to reference the order

                        DatabaseReference buttonchatsRef = FirebaseDatabase.getInstance().getReference("Shop")
                                .child(contactNumber) // Replace with the appropriate reference
                                .child("orders").child(key).child("buttonchats")
                                .child(order.getKey()); // Use the unique key to reference the order

                        System.out.println("sdfdf " + chatsRef);

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

                        userorderRef.removeValue();
                        userchatRef.removeValue();
                        userButtonChatsRef.removeValue();
                        // adapter.notifyDataSetChanged();

                        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                            ordersRef.removeValue().addOnCompleteListener(orderRemoveTask -> {
                                                if (orderRemoveTask.isSuccessful()) {
                                                    DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("Shop")
                                                            .child(contactNumber).child("notification").child(orderkey);

                                                    System.out.println("errdg "+notificationRef);
                                                    notificationRef.removeValue();

                                                    decrementnotificationCountForShop();
                                                    // Notify the adapter of data changes
                                                    decrementOrderCountForShop(order.getKey());
//                                                    ordersList.remove(position);
                                                    adapter.notifyDataSetChanged();
                                                } else {
                                                    // Handle the case where removing the order fails
                                                }
                                            });

                                            // Move the chats data to history
                                            chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot chatSnapshot) {
                                                    if (chatSnapshot.exists()) {
                                                        Map<String, Object> chatData = (Map<String, Object>) chatSnapshot.getValue();
                                                        historychatRef.setValue(chatData).addOnCompleteListener(chatMoveTask -> {
                                                            if (chatMoveTask.isSuccessful()) {
                                                                // Chats data successfully moved to history
                                                                // Now, remove the chats data
                                                                chatsRef.removeValue();
                                                                //   ordersList.remove(position);
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
                                            buttonchatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot buttonChatSnapshot) {
                                                    if (buttonChatSnapshot.exists()) {
                                                        Map<String, Object> buttonChatData = (Map<String, Object>) buttonChatSnapshot.getValue();
                                                        historyButtonChatsRef.setValue(buttonChatData).addOnCompleteListener(buttonChatMoveTask -> {
                                                            if (buttonChatMoveTask.isSuccessful()) {
                                                                // Buttonchats data successfully moved to history
                                                                // Now, remove the buttonchats data
                                                                buttonchatsRef.removeValue();
                                                                //    ordersList.remove(position);
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

    private void decrementnotificationCountForShop(){

        DatabaseReference shopNotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                .child(contactNumber)
                .child("notificationcount");

        DatabaseReference NotificationCountRef = FirebaseDatabase.getInstance().getReference("Shop")
                .child(contactNumber).child("count")
                .child("notificationcount");


        shopNotificationCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long currentCount = dataSnapshot.getValue(Long.class);
                    if (currentCount > 0) {
                        // Decrement the count
                        shopNotificationCountRef.setValue(currentCount - 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

        NotificationCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long currentCount = dataSnapshot.getValue(Long.class);
                    if (currentCount > 0) {
                        // Decrement the count
                        NotificationCountRef.setValue(currentCount - 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

    }
    private void decrementOrderCountForShop(String orderKey) {
        DatabaseReference shopNodeRef = databaseReference.child(contactNumber);
        DatabaseReference orderCountRef = shopNodeRef.child("ordercount");
        DatabaseReference countRef = databaseReference.child(contactNumber).child("count").child("ordercount");

        orderCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long currentOrderCount = 0;
                if (dataSnapshot.exists()) {
                    currentOrderCount = (long) dataSnapshot.getValue();
                }

                // Decrement the order count if it's greater than 0
                if (currentOrderCount > 0) {
                    currentOrderCount--;
                    orderCountRef.setValue(currentOrderCount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });

        countRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long currentOrderCount = 0;
                if (dataSnapshot.exists()) {
                    currentOrderCount = (long) dataSnapshot.getValue();
                }

                // Decrement the order count if it's greater than 0
                if (currentOrderCount > 0) {
                    currentOrderCount--;
                    countRef.setValue(currentOrderCount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });

    }


    @Override
    public void onContactClick(int position) {
        orders order = ordersList.get(position);

        String contactNumber = order.getBuyerContactNumber();
        String orderkey = order.getKey();
        System.out.println("dvfb " +orderkey);
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
//        dialIntent.setData(Uri.parse("tel:" + contactNumber));
        Intent orderDetailsIntent = new Intent(getApplicationContext(), OrderDetails.class);

        orderDetailsIntent.putExtra("buyerContactNumber", contactNumber);
        orderDetailsIntent.putExtra("orderkey", orderkey);
        boolean isBottonCardVisible = true; // Set this to true if you want it initially visible
        orderDetailsIntent.putExtra("isBottonCardVisible", isBottonCardVisible);
        startActivity(orderDetailsIntent);
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//
//
//                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {
//                                    DataSnapshot ordersSnapshot = shopSnapshot.child("orders");
//                                    String key = shopSnapshot.getKey();
//                                   DatabaseReference orderRef = databaseReference.child(key).child("orders");
//                                    System.out.println("tgrg " + orderRef);
//
//                                    orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//                                            if (snapshot.exists()) {
//                                               // orderKeys.clear();
//                                                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
//                                                    keys = orderSnapshot.getKey();
//                                                    System.out.println("rtgyrefg" + keys);
//                                                    DatabaseReference userordersRef = shopRef.child(keys);
//
//                                                    userordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                        @Override
//                                                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//
//                                                            for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
//                                                                if (orderSnapshot.hasChild("itemName") &&
//                                                                        orderSnapshot.hasChild("firstImageUrl") &&
//                                                                        orderSnapshot.hasChild("buyerContactNumber") &&
//                                                                        orderSnapshot.hasChild("buyerName") &&
//                                                                        orderSnapshot.hasChild("quantity") &&
//                                                                        orderSnapshot.hasChild("status") &&
//                                                                        orderSnapshot.hasChild("timestamp")) {
//                                                                    orders order = orderSnapshot.getValue(orders.class);
//                                                                    String keyss = orderSnapshot.child("timestamp").getValue(String.class);
//                                                                    System.out.println("fhuyfg " + keyss);
//                                                                    order.setKey(orderSnapshot.getKey()); // Set the unique key from Firebase
//                                                                    ordersList.add(order);
//                                                                }
//                                                            }
//                                                            // Reverse the order of the list to show recent items first
//                                                         //   Collections.reverse(ordersList);
//                                                            adapter.notifyDataSetChanged();
//                                                        }
//
//                                                        @Override
//                                                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//                                                        }
//                                                    });
//                                                }
//                                                adapter.notifyDataSetChanged();
//                                            }
//
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//                                        }
//                                    });
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError error) {
//                            // Handle errors
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Handle errors
//            }
//        });

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
                    //contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    shopRef = databaseReference.child(contactNumber).child("orders");
                    System.out.println("items " + shopRef);
                    ordersList.clear();
                    shopRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ordersList.clear();
                            for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {

                                keys = orderSnapshot.getKey();
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
                                                System.out.println("guhfh " +shopimage);
                                                senderID = orderSnapshot.child("senderID").getValue(String.class);
                                                String reciverID = orderSnapshot.child("receiverID").getValue(String.class);
                                                orders order = new orders(itemName, buyerName, buyerContactNumber, orderKey, datetamp, timestamp, quantity, shopimage,
                                                        shopOwnerContactNumber, shopOwnerContactNumber, firstImageUrl, senderID, reciverID);

                                                ordersList.add(order);
                                            }

                                            databaseReference.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()){
                                                        //String shopimage = snapshot.child("url").getValue(String.class);
                                                        String shopimage = getIntent().getStringExtra("shopimage");
                                                        System.out.println("dfgdgv " +shopimage);
                                                        String shopname = getIntent().getStringExtra("shopName");
                                                        System.out.println("dfgdgv " +shopname);

                                                        if (ordersList.isEmpty()) {
                                                            // No orders present, show the "orderinfo" TextView and "boostButton"
                                                            orderinfotextview.setVisibility(View.VISIBLE);
                                                            boostbutton.setVisibility(View.VISIBLE);
                                                            boostprofile(shopimage, shopname);
                                                        } else {
                                                            // Orders are present, hide the "orderinfo" TextView and "boostButton"
                                                            orderinfotextview.setVisibility(View.GONE);
                                                            boostbutton.setVisibility(View.GONE);
                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                                }
                                            });


//                                            if (orderSnapshot.hasChild("itemName") &&
//                                                    orderSnapshot.hasChild("firstImageUrl") &&
//                                                    orderSnapshot.hasChild("buyerContactNumber") &&
//                                                    orderSnapshot.hasChild("buyerName") &&
//                                                    orderSnapshot.hasChild("quantity") &&
//                                                    orderSnapshot.hasChild("status") &&
//                                                    orderSnapshot.hasChild("timestamp")) {
//                                                orders order = orderSnapshot.getValue(orders.class);
//                                                String keyss = orderSnapshot.child("timestamp").getValue(String.class);
//                                                 senderID = orderSnapshot.child("senderID").getValue(String.class);
//                                                System.out.println("fhuyfg " + keyss);
//                                                order.setKey(orderSnapshot.getKey()); // Set the unique key from Firebase
//                                                ordersList.add(order);
//                                            }
                                        }
                                        // Reverse the order of the list to show recent items first
                                        Collections.reverse(ordersList);
                                        adapter.notifyDataSetChanged();

                                        // Check the size of the ordersList

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

    public void boostprofile(String shopimage, String shopname) {
        boostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidImageUrl(shopimage)) {
                    String appUrl = "https://play.google.com/store/apps/details?id=com.spark.swarajyabiz&hl=en-IN"; // Replace with your app's actual Play Store URL
                    String message = shopname+ "\n" +
                            "च नवीन ॲप आल आहे. " +shopname+ " shop मधील प्रॉडक्ट खरेदी करण्यासाठी आजच ॲप डाऊनलोड करा. व तुमच्या संपर्कातील लोकांना " +shopname+" च ॲप अवश्य शेअर करा. \n" + appUrl;
                    // Download the image to local storage
                    downloadImageAndShare(shopimage, message);
                } else {
                    // Handle the case where the image URL is not valid
                    Toast.makeText(OrderLists.this, "Invalid image URL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidImageUrl(String imageUrl) {
        // Implement your validation logic here
        // Check if the URL is a valid image URL
        // You can use libraries like Picasso or Glide to validate the URL
        // Return true if it's a valid image URL, otherwise return false
        // You may also check if the file format is supported
        // and if the image exists on the server
        return true;
    }

    private void downloadImageAndShare(String imageUrl, final String message) {
        Glide.with(OrderLists.this)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        // Save the bitmap to local storage (e.g., app's cache directory)
                        // Then, share the locally saved image using an Intent

                        // Example code for saving to local storage
                        // Save the bitmap to a file

                        File cachePath = new File(getCacheDir(), "images");
                        cachePath.mkdirs();
                        FileOutputStream stream;
                        try {
                            File imageFile = new File(cachePath, "image.jpg");
                            stream = new FileOutputStream(imageFile);
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            stream.flush();
                            stream.close();

                            // Share the locally saved image with WhatsApp
                            shareImageWithWhatsApp(imageFile, message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        // Handle case when the resource is cleared
                    }
                });
    }

    private void shareImageWithWhatsApp(File imageFile, String message) {
        Uri imageUri = FileProvider.getUriForFile(
                OrderLists.this,
                BuildConfig.APPLICATION_ID + ".provider",
                imageFile
        );

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setPackage("com.whatsapp"); // Restrict to WhatsApp
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
        finish();
    }
}
package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.accessibility.CaptioningManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class MyOrders extends AppCompatActivity implements MyOrderAdapter.OrderClickListener {

    DatabaseReference databaseRef, userRef, orderRef;
    String userId, shopContactNumber, keys, currentUserContactNumber, key, shopnumber;
    RecyclerView recyclerView;
    MyOrderAdapter orderAdapter;
    List<Order> orderList;
    private static final String USER_ID_KEY = "userID";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);


        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference().child("Shop");
        userRef = database.getReference("Users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

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

        recyclerView = findViewById(R.id.myordershistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        orderAdapter = new MyOrderAdapter(orderList, this, this);
        //orderAdapter = new MyOrderAdapter(orderList, (MyOrderAdapter.OrderClickListener) this);
        recyclerView.setAdapter(orderAdapter);


        Intent sharedIntent = IntentDataHolder.getSharedIntent();
        if (sharedIntent != null) {
            shopContactNumber = sharedIntent.getStringExtra("contactNumber");
            System.out.println("vbnyfg " +shopContactNumber);
            String shopName = sharedIntent.getStringExtra("ShopName");
            String name = sharedIntent.getStringExtra("Name");
            String district = sharedIntent.getStringExtra("District");


        }



        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        shopnumber = getIntent().getStringExtra("contactNumber");
        System.out.println("ffvrf " +shopnumber);
    }

//    @Override
//    public void onMessageClick(int position) {
//        Order order = orderList.get(position);
//    }


    public void onRemoveClick(int position) {
        Order order = orderList.get(position);
        String orderKey = order.getOrderKey(); // Replace with the actual method to get the order key
       String shopcontactnumber = order.getShopOwnerContactNumber();

       // System.out.println("rgfgf " +shopcontactnumber);

        // Remove the order from the shop's node
       // removeOrderFromShopNode(orderKey, position);

        // Remove the order from the user's node
        removeOrderFromUserNode(orderKey, position, shopcontactnumber);
    }

    @Override
    public void onMessageClick(int position) {
        Order order = orderList.get(position);

        String contactNumber = order.getBuyerContactNumber();
        String ownercontactNumber = order.getShopOwnerContactNumber();
        String orderkey = order.getOrderKey();
        System.out.println("sdvdv " + ownercontactNumber);
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
//        dialIntent.setData(Uri.parse("tel:" + contactNumber));
        Intent orderDetailsIntent = new Intent(getApplicationContext(), UserOrderdetails.class);

        orderDetailsIntent.putExtra("buyerContactNumber", contactNumber);
        orderDetailsIntent.putExtra("orderkey", orderkey);
        orderDetailsIntent.putExtra("ownercontactNumber" , ownercontactNumber);
        boolean isBottonCardVisible = false; // Set this to true if you want it initially visible
        orderDetailsIntent.putExtra("isBottonCardVisible", isBottonCardVisible);
        startActivity(orderDetailsIntent);

    }

    private void removeOrderFromUserNode(String orderKey, int position, String shopContactNumber) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this order?");
        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            orderList.clear();
                            for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {
                                DataSnapshot ordersSnapshot = shopSnapshot.child("orders");
                                 key = shopSnapshot.getKey();
                                DatabaseReference orderRef = databaseRef.child(key).child("orders");

                                orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                                                String keys = orderSnapshot.getKey();
                                                    DatabaseReference userorderRef = orderRef.child(currentUserContactNumber);

                                                    DatabaseReference chatsRef = userorderRef.child("chats")
                                                            .child(orderKey);

                                                    DatabaseReference buttonchatsRef = userorderRef.child("buttonchats")
                                                            .child(orderKey);

                                                    userorderRef.child(orderKey).removeValue(new DatabaseReference.CompletionListener() {
                                                        @Override
                                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                            if (error == null) {


                                                                // You can also remove the order from your local orderList
                                                                userorderRef.child(orderKey).removeValue();
                                                                orderAdapter.notifyItemRemoved(position);
                                                                orderAdapter.notifyItemRangeChanged(position, orderList.size());
                                                                decrementOrderCountForShop(orderKey, shopContactNumber);
                                                                decrementnotificationCountForShop(shopContactNumber);
                                                                orderAdapter.notifyDataSetChanged();
                                                            } else {
                                                                // Handle the error
                                                                Toast.makeText(getApplicationContext(), "Failed to remove order: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("Shop")
                                                        .child(shopContactNumber).child("notification");
                                                System.out.println("hgbgfb " +notificationRef);

                                                notificationRef.child(orderKey).removeValue(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@androidx.annotation.Nullable DatabaseError error, @androidx.annotation.NonNull DatabaseReference ref) {
                                                        notificationRef.child(orderKey).removeValue();
                                                    }
                                                });

                                                    chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                chatsRef.removeValue()
                                                                        .addOnCompleteListener(removeTask -> {
                                                                            if (removeTask.isSuccessful()) {
                                                                                // Order successfully removed from "orders" node
                                                                                // Notify the adapter of data changes
                                                                                orderAdapter.notifyDataSetChanged();
                                                                            } else {
                                                                                // Handle the case where removing the order fails
                                                                            }
                                                                        });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError error) {
                                                            // Handle onCancelled event
                                                        }
                                                    });

                                                    buttonchatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                buttonchatsRef.removeValue()
                                                                        .addOnCompleteListener(removeTask -> {
                                                                            if (removeTask.isSuccessful()) {
                                                                                // Order successfully removed from "orders" node
                                                                                // Notify the adapter of data changes
                                                                                orderAdapter.notifyDataSetChanged();
                                                                            } else {
                                                                                // Handle the case where removing the order fails
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
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                userRef.child(userId).child("ordered").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {
                                DataSnapshot ordersSnapshot = shopSnapshot.child("ordered");
                                String key = shopSnapshot.getKey();
                                DatabaseReference orderRef = userRef.child(userId).child("ordered").child(key);
                                System.out.println("gfsgsaf " + key);
                                DatabaseReference chatsRef = orderRef.child("chats")
                                        .child(orderKey); // Use the unique key to reference the order

                                DatabaseReference buttonchatsRef = orderRef.child("buttonchats")
                                        .child(orderKey); // Use the unique key to reference the order

                                System.out.println("sdfdfe " +chatsRef);

                                orderRef.child(orderKey).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                                        if (error == null) {
                                            // Order successfully removed from Firebase
                                            // You can also remove the order from your local orderList
                                            //                                    orderList.remove(position);
                                            //                                    orderAdapter.notifyItemRemoved(position);
                                            //                                    orderAdapter.notifyItemRangeChanged(position, orderList.size());
                                        } else {
                                            // Handle the error
                                            Toast.makeText(getApplicationContext(), "Failed to remove order: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });



                                chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            chatsRef.removeValue()
                                                    .addOnCompleteListener(removeTask -> {
                                                        if (removeTask.isSuccessful()) {
                                                            // Order successfully removed from "orders" node
                                                            // Notify the adapter of data changes
                                                            orderAdapter.notifyDataSetChanged();
                                                        } else {
                                                            // Handle the case where removing the order fails
                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Handle onCancelled event
                                    }
                                });

                                buttonchatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            buttonchatsRef.removeValue()
                                                    .addOnCompleteListener(removeTask -> {
                                                        if (removeTask.isSuccessful()) {
                                                            // Order successfully removed from "orders" node
                                                            // Notify the adapter of data changes
                                                            orderAdapter.notifyDataSetChanged();
                                                        } else {
                                                            // Handle the case where removing the order fails
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
                    }
                });

                fetchOrdersAndPopulateList();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked Cancel, do nothing or handle accordingly
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


//    private void removeOrderFromShopNode(String orderKey, int position) {
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("Are you sure you want to delete this order?");
//        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//        });
//        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                // User clicked Cancel, do nothing or handle accordingly
//            }
//        });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//
//    }

private void decrementnotificationCountForShop(String shopContactNumber){
//    Intent sharedIntent = IntentDataHolder.getSharedIntent();
//    if (sharedIntent != null) {
        //String shopContactNumber = getIntent().getStringExtra("contactNumber");
        DatabaseReference shopNodeRef = databaseRef.child(shopContactNumber);
        DatabaseReference orderCountRef = shopNodeRef.child("notificationcount");
        DatabaseReference countRef = databaseRef.child(shopContactNumber).child("count").child("notificationcount");

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

 //   }
}

    private void decrementOrderCountForShop(String orderKey, String shopContactNumber) {
//        Intent sharedIntent = IntentDataHolder.getSharedIntent();
//        if (sharedIntent != null) {
           // String shopContactNumber = getIntent().getStringExtra("contactNumber");
            System.out.println("fghxgh " +shopContactNumber);
            DatabaseReference shopNodeRef = databaseRef.child(shopContactNumber);
            DatabaseReference orderCountRef = shopNodeRef.child("ordercount");
            DatabaseReference countRef = databaseRef.child(shopContactNumber).child("count").child("ordercount");

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

      //  }
    }

    @Override
    public void onResume() {
        super.onResume();
        orderList.clear();

        fetchOrdersAndPopulateList();
    }

    private void fetchOrdersAndPopulateList() {
        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUserContactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    System.out.println("tgrg " + currentUserContactNumber);

                    databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {
                                    DataSnapshot ordersSnapshot = shopSnapshot.child("orders");
                                    String key = shopSnapshot.getKey();
                                    System.out.println("fgvv " +key);
                                    orderRef = databaseRef.child(key).child("orders");


                                    orderRef.child(currentUserContactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                                                    keys = orderSnapshot.getKey();
                                                    if (!orderSnapshot.getKey().equals("buttonchats") &&
                                                            !orderSnapshot.getKey().equals("chats")) {
                                                        String itemName = orderSnapshot.child("itemName").getValue(String.class);
                                                        System.out.println("efygfe " + itemName);
                                                        String firstImageUrl = orderSnapshot.child("firstImageUrl").getValue(String.class);
                                                        String buyerContactNumber = orderSnapshot.child("buyerContactNumber").getValue(String.class);
                                                        String buyerName = orderSnapshot.child("buyerName").getValue(String.class);
                                                        String quantity = orderSnapshot.child("quantity").getValue(String.class);
                                                        String status = orderSnapshot.child("status").getValue(String.class);
                                                        Object timestamp = orderSnapshot.child("timestamp").getValue();
                                                        Object datetamp = orderSnapshot.child("datetamp").getValue();
                                                        String orderKey = orderSnapshot.child("orderkey").getValue(String.class);
                                                        String shopOwnerContactNumber = orderSnapshot.child("shopOwnerContactNumber").getValue(String.class);
                                                        String shopimage = orderSnapshot.child("shopImage").getValue(String.class);
                                                        String senderID = orderSnapshot.child("senderID").getValue(String.class);
                                                        String reciverID = orderSnapshot.child("senderID").getValue(String.class);

                                                        System.out.println("sfhgf " + buyerContactNumber);// Add more fields as needed
                                                        // Create an Order object and add it to the list
                                                        Order order = new Order(itemName, firstImageUrl, buyerContactNumber, buyerName, quantity, status, timestamp, datetamp,
                                                                orderKey, shopOwnerContactNumber, shopimage, senderID, reciverID);
                                                        orderList.add(order);
                                                    }

//                                                    if (orderSnapshot.hasChild("itemName") &&
//                                                            orderSnapshot.hasChild("firstImageUrl") &&
//                                                            orderSnapshot.hasChild("buyerContactNumber") &&
//                                                            orderSnapshot.hasChild("buyerName") &&
//                                                            orderSnapshot.hasChild("quantity") &&
//                                                            orderSnapshot.hasChild("status") &&
//                                                            orderSnapshot.hasChild("timestamp") &&
//                                                            orderSnapshot.hasChild("datetamp") &&
//                                                            orderSnapshot.hasChild("orderkey") &&
//                                                            orderSnapshot.hasChild("shopOwnerContactNumber") &&
//                                                            orderSnapshot.hasChild("shopImage") &&
//                                                            orderSnapshot.hasChild("shopImage") &&
//                                                            orderSnapshot.hasChild("senderID")) {
//
//                                                        String itemName = orderSnapshot.child("itemName").getValue(String.class);
//                                                        System.out.println("efygfe " + itemName);
//                                                        String firstImageUrl = orderSnapshot.child("firstImageUrl").getValue(String.class);
//                                                        String buyerContactNumber = orderSnapshot.child("buyerContactNumber").getValue(String.class);
//                                                        String buyerName = orderSnapshot.child("buyerName").getValue(String.class);
//                                                        String quantity = orderSnapshot.child("quantity").getValue(String.class);
//                                                        String status = orderSnapshot.child("status").getValue(String.class);
//                                                        Object timestamp = orderSnapshot.child("timestamp").getValue();
//                                                        Object datetamp = orderSnapshot.child("datetamp").getValue();
//                                                        String orderKey = orderSnapshot.child("orderkey").getValue(String.class);
//                                                        String shopOwnerContactNumber = orderSnapshot.child("shopOwnerContactNumber").getValue(String.class);
//                                                        String shopimage = orderSnapshot.child("shopImage").getValue(String.class);
//                                                        String senderID = orderSnapshot.child("senderID").getValue(String.class);
//                                                        String reciverID = orderSnapshot.child("senderID").getValue(String.class);
//
//                                                        System.out.println("sfhgf " + buyerContactNumber);// Add more fields as needed
//                                                        // Create an Order object and add it to the list
//                                                        Order order = new Order(itemName, firstImageUrl, buyerContactNumber, buyerName, quantity, status, timestamp, datetamp,
//                                                                orderKey, shopOwnerContactNumber, shopimage, senderID, reciverID);
//                                                        orderList.add(order);
//                                                    }else {
                                                        // Debugging: Print out the keys that don't meet the condition
//                                                        System.out.println("Order data is missing required fields for key: " + orderSnapshot.getKey());
//                                                    }
                                                }
                                            }
                                            orderAdapter.notifyDataSetChanged();

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

    }
}
//        userRef.child(userId).child("Myorders").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
//                        String itemName = orderSnapshot.child("itemName").getValue(String.class);
//                        System.out.println("sfhgf " +itemName);
//                        String firstImageUrl = orderSnapshot.child("firstImageUrl").getValue(String.class);
//                        String buyerContactNumber = orderSnapshot.child("buyerContactNumber").getValue(String.class);
//                        String buyerName = orderSnapshot.child("buyerName").getValue(String.class);
//                        String quantity = orderSnapshot.child("quantity").getValue(String.class);
//                        String status = orderSnapshot.child("status").getValue(String.class);
//                        Object timestamp = orderSnapshot.child("timestamp").getValue();
//
//                        Order order = new Order(itemName, firstImageUrl, buyerContactNumber, buyerName, quantity, status, timestamp);
//                        orderList.add(order);
//                    }
//                    orderAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//
//            }
//        });

//    }
//}
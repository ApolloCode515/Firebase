package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tsurkis.timdicator.Timdicator;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ItemDetails extends AppCompatActivity implements ItemImagesAdapter.ImageClickListener, ScratchCardView.RevealListener{

    private RecyclerView recyclerViewImages;
    private List<String> imageUrls;
    int size;
    private RecyclerView imageRecyclerView;
    private ItemImagesAdapter imageAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, usersRef, userRef, databaseRef, orderHistoryRef, chatRef;
    String contactNumber, itemContactNumber, Contactnumber, userId, formattedTime,formattedDate, currentuserName; // all same contact but location different
    private List<ItemList> itemList;
    ImageView back, shopimage, percentimg;
    Button btnmessage;
    String itemkey;
    String itemName, numericPart, orderKey;
    String itemPrice, firstImageUrl, shopName, district, address, shopImage;
    TextView itemNameTextView, itemDescriptionTextView, offertextview, pricetextview,sellTextView, shopname, shopaddress, shopdistrict, shoptaluka,
              minqty, enterqty, totalamt, whsaleprice, discount, discounttext;
    private boolean isFirstOrder = true;
    Intent intents;
    private Timdicator timdicator;
    private static final String USER_ID_KEY = "userID";
    private LinearLayout dotsLayout;
    private int currentPosition = 0;
    CardView shopcard, wholesalelay;
    RecyclerView recyclerViewShops;
    ShopAdapter shopAdapter;
    List<Shop> shopList;
    private List<Shop> filteredList;
    boolean toggle = false;
    RadioButton rdWholesale;
    LinearLayout  callayout;

    private static final long DOUBLE_CLICK_TIME_INTERVAL = 300; // Define the time interval for a double click (in milliseconds)
    private long lastClickTime = 0; // Initialize the last click time
    private Dialog dialog;
    int totalamts=0;

    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        itemDescriptionTextView = findViewById(R.id.descriptiontext);
        itemDescriptionTextView.setVisibility(View.GONE);
        btnmessage = findViewById(R.id.btnmessage);
        back = findViewById(R.id.back);
        dotsLayout = findViewById(R.id.dotsLayout);
        offertextview = findViewById(R.id.offertextview);
        pricetextview = findViewById(R.id.pricetextview);
        sellTextView = findViewById(R.id.sellprice);
        rdWholesale = findViewById(R.id.rdWholesale);
        wholesalelay = findViewById(R.id.whsalelay);
        minqty = findViewById(R.id.minqty);
        enterqty = findViewById(R.id.enterqty);
        whsaleprice = findViewById(R.id.whsaleprice);
        totalamt = findViewById(R.id.totalamt);
        discount = findViewById(R.id.discount);
        callayout = findViewById(R.id.callayout);
        percentimg = findViewById(R.id.percentimg);
        discounttext = findViewById(R.id.discounttext);


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


        // Initialize RecyclerView
        imageRecyclerView = findViewById(R.id.horizantalrecyclerview);
        imageRecyclerView.setHasFixedSize(true);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //int spacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        // imageRecyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(spacing));

        // Create an instance of the ImageAdapter
        imageAdapter = new ItemImagesAdapter(this, imageUrls, this);
        imageRecyclerView.setAdapter(imageAdapter);

        imageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                updateDots();
            }
        });

// Set the initial background
       // updateBackground(rdWholesale);

        // Create and display the dots




        itemContactNumber = getIntent().getStringExtra("contactNumber");
        System.out.println("sdfvd " +itemContactNumber);
        Intent sharedIntent = IntentDataHolder.getSharedIntent();
        if (sharedIntent != null) {
            Contactnumber = sharedIntent.getStringExtra("contactNumber");
            System.out.println("dfh " +Contactnumber);

            //Log.d("contactNumber", "" + contactNumber);
//            // Convert the image URI string to a URI object
//            Uri imageUri = Uri.parse(image);
//            Log.d("imageUri", "" + imageUri);
//            catalogshopname.setText(shopName);
//            // Load image using Glide
//            RequestOptions requestOptions = new RequestOptions()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL); // Optional: Set caching strategy
//
//            Glide.with(this)
//                    .load(image).centerCrop()
//                    .apply(requestOptions)
//                    .into(catalogshopimage);

        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseApp.initializeApp(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Shop");
        databaseRef = firebaseDatabase.getReference().child("Shop");
        userRef = firebaseDatabase.getReference("Users");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
         userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }
        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);




//        btnmessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatabaseReference shopRef = databaseReference.child(contactNumber);
//                intent = new Intent(getApplicationContext(), PlaceOrder.class);
//                getfirstimage();
//
//                // Construct the order information
//                String itemName = itemNameTextView.getText().toString();
//                System.out.println("itemname"+itemName);
//
//                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            String currentUserDisplayName = dataSnapshot.child("Name").getValue(String.class);
//                            String currentUserContactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
//                            Log.d("currentUserContactNumber", "" + contactNumber);
//
//                            DatabaseReference shopRef = databaseReference.child(contactNumber).child("items");
//                            System.out.println("items " + shopRef);
//                            String orderKey = databaseReference.child(contactNumber).child("orders").push().getKey();
//
//                            DatabaseReference orderRef = databaseReference.child(contactNumber).child("orders").child(orderKey);
//
//                            Map<String, Object> orderData = new HashMap<>();
//                            orderData.put("itemName", itemName);
//                            orderData.put("buyerName", currentUserDisplayName);
//                            orderData.put("buyerContactNumber", currentUserContactNumber);
//
//                            DatabaseReference shopref = databaseReference.child(ContactNumber);
//                            DatabaseReference ordersRef = shopref.child("orders");
//                            String orderId = ordersRef.push().getKey();
//
//                            ordersRef.child(orderId).setValue(orderData)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Toast.makeText(ItemDetails.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            // Handle failure to store the order
//                                        }
//                                    });
//
//                            // Store the order in the user's "orders" node
//                            DatabaseReference userOrdersRef = usersRef.child("ordered");
//                            Log.d("TAG", "onDataChangesdfsd " +userOrdersRef);
//                            userOrdersRef.child(orderId).setValue(orderData)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            // Handle success (if needed)
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            // Handle failure to store the order in user's node
//                                        }
//                                    });
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        // Handle the error gracefully
//                    }
//                });
//            }
//        });

//        shopcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Create an Intent to start ShopDetailsActivity
//                Intent intent = new Intent(getApplicationContext(), ShopDetails.class);
//
//                // Pass data to the intent using putExtra
//                intent.putExtra("contactNumber", contactNumber);
//                // Start the activity
//                startActivity(intent);
//            }
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemDetails.this.finish();
            }
        });


        // Retrieve data from Intent
        Intent intent = getIntent();
         itemName = intent.getStringExtra("itemName");
         itemPrice = intent.getStringExtra("itemPrice");
        String itemDescription = intent.getStringExtra("itemDescription");
         firstImageUrl = intent.getStringExtra("firstImageUrl");
        shopName = intent.getStringExtra("shopName");
        district = intent.getStringExtra("district");
        shopImage = intent.getStringExtra("shopimage");
        String taluka = intent.getStringExtra("taluka");
        address = intent.getStringExtra("address");
        String itemoffer = intent.getStringExtra("itemOffer");
        String itemSellPrice = intent.getStringExtra("itemSellPrice");
        String wholesale = intent.getStringExtra("itemWholesale");
        String Minqty = intent.getStringExtra("itemMinqty");

        System.out.println("sedvs s " +wholesale);
        whsaleprice.setText(wholesale);
        minqty.setText(Minqty);
        sellTextView.setText(itemSellPrice);



//        rdWholesale.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggle = !toggle; // Toggle the value
//                rdWholesale.setChecked(toggle);
//                wholesalelay.setVisibility(toggle ? View.VISIBLE : View.GONE);
//
////                try {
////                    if (itemSellPrice != null) {
////                         numericPart = itemSellPrice
////                                .replaceAll("[^0-9.]+", "");  // Remove non-numeric characters except the dot
////                        numericPart = numericPart.replaceAll("\\.0*$", "");
////                        if (numericPart.endsWith(".")) {
////                            numericPart = numericPart.substring(0, numericPart.length() - 1);
////
////
////                        }
////
////                        // Do something with the processed numericPart if needed
////                    } else {
////                        // Handle the case where itemSellPrice is null
////                        // You might want to log a message or take appropriate action
////                    }
////                } catch (Exception e) {
////                    // Handle exceptions if necessary
////                    e.printStackTrace(); // Example: Print the stack trace for debugging
////                }
//
//
//
//
//
//
//
//
//            }
//        });


        enterqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enterqtys = enterqty.getText().toString().trim();
                if (!enterqtys.isEmpty()){
                    int wholesalepr = Integer.parseInt(wholesale);
                    double entqty = Double.parseDouble(enterqtys);
                    double miqty = Double.parseDouble(Minqty);
                    if (entqty < miqty) {
                        callayout.setVisibility(View.VISIBLE);
                        try {
                            int qty = Integer.parseInt(enterqtys);
                            int sellprice = Integer.parseInt(itemSellPrice);
                            totalamts = sellprice * qty;
                            totalamt.setText(sellprice+ " x " +qty+ " = " +totalamts);
                            System.out.println("sdcvzdf " +sellprice);
                            int dis1 = (sellprice * qty);
                            int dis2 = (sellprice * qty);
                            int discounts = dis1 - dis2;
                            String disct = String.valueOf(discounts);
                            //discount.setText(disct);
                            percentimg.setVisibility(View.GONE);

                            discounttext.setText("Minimum quantity for extra discount is " +Minqty);

                        } catch (NumberFormatException e) {
                            // Handle the case where the input is not a valid integer
                            e.printStackTrace(); // Example: Print the stack trace for debugging
                        }

                    }else {
                        callayout.setVisibility(View.VISIBLE);
                        try {
                            int qty = Integer.parseInt(enterqtys);
                            totalamts = wholesalepr * qty;
                            totalamt.setText(wholesalepr+ " x " +qty+ " = " +totalamts);
                            int sellprice = Integer.parseInt(itemSellPrice);
                            System.out.println("sdcvzdf " +sellprice);
                            int dis1 = (sellprice * qty);
                            int dis2 = (wholesalepr * qty);
                            int discounts = dis1 - dis2;
                            String disct = String.valueOf(discounts);
                            discount.setText(disct);
                            percentimg.setVisibility(View.VISIBLE);
                            discounttext.setText("Yay! Your total discount is \u20B9");
                        } catch (NumberFormatException e) {
                            // Handle the case where the input is not a valid integer
                            e.printStackTrace(); // Example: Print the stack trace for debugging
                        }
                    }
                }
                else {
                    callayout.setVisibility(View.GONE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        offertextview.setText("( "+itemoffer+" off )");
        pricetextview.setText("₹ "+itemPrice+".00");
        pricetextview.setPaintFlags(pricetextview.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        boolean flag = intent.getBooleanExtra("flag", false);
        System.out.println("asjfbjfb " +flag);

        // Initialize RecyclerView
        recyclerViewShops = findViewById(R.id.viewdetails);
        recyclerViewShops.setHasFixedSize(true);
        recyclerViewShops.setLayoutManager(new LinearLayoutManager(this));

        shopList = new ArrayList<>();
        filteredList = new ArrayList<>();
        shopAdapter = new ShopAdapter(filteredList, this, this:: onClick);
        recyclerViewShops.setAdapter(shopAdapter);
        readDataFromFirebase();
        RelativeLayout shoplayout = findViewById(R.id.shoplayout);
        // Show or hide the RecyclerView based on whether you are on the Home fragment
        if (flag) {
            shoplayout.setVisibility(View.VISIBLE);
            readDataFromFirebase();  // Load data when on the Home fragment
        } else {
            shoplayout.setVisibility(View.GONE);
        }


        if (itemDescription!=null && !itemDescription.isEmpty())
        {
            itemDescriptionTextView.setVisibility(View.VISIBLE);
            String Description =  intent.getStringExtra("itemDescription");
            itemDescriptionTextView.setText(Html.fromHtml(Description));
            //itemDescriptionTextView.setTextColor(Color.parseColor("#FF0000"));
        }
        else{
            itemDescriptionTextView.setVisibility(View.GONE);
        }

         itemkey = intent.getStringExtra("itemKey");
        Log.d("itemKey ",""+itemkey);
        Log.d("itemName ",""+itemName);
        ArrayList<String> itemImageUrls = intent.getStringArrayListExtra("itemImageUrls");


        // Use the retrieved data to populate your layout elements
        itemNameTextView = findViewById(R.id.Name);

       // TextView itemDescriptionTextView = findViewById(R.id.description);


        itemNameTextView.setText(itemName);

       // itemDescriptionTextView.setText(itemDescription);

        List<String> itemImages = intent.getStringArrayListExtra("itemImages");
        imageAdapter.setImagesUrls(itemImages);
        imageAdapter.notifyDataSetChanged();

        createDots(itemImages.size());
        updateDots();

        getData();
      //getfirstimage();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    DatabaseReference shopRef = databaseReference.child(contactNumber).child("items");
                    System.out.println("items " + contactNumber);

                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    currentuserName = dataSnapshot.child("name").getValue(String.class);

                    orderHistoryRef = databaseRef.child(itemContactNumber).child("orders")
                            .child(contactNumber);// Adjust the reference to your Firebase structure

//                    orderHistoryRef = databaseRef.child(shopContactNumber).child("orders")
//                            .child(contactNumber);// Adjust the reference to your Firebase structure
                    System.out.println("sgrgr " +currentuserName);
                    chatRef = databaseRef.child(itemContactNumber).child("orders").child(contactNumber)
                            .child("chats"); // Adjust the reference to your Firebase structure
                    // Fetch order history data
                    System.out.println("wewfrg" +contactNumber);

                    //fetchOrderHistoryAndChatMessages(orderHistoryRef, chatRef);
                    // Fetch chat message data
                    //fetchChatMessages(chatRef);
                    System.out.println("sdfsfs " +currentuserName);

                    if (contactNumber != null && itemContactNumber != null && contactNumber.equals(itemContactNumber)
                    ) {
                        // The contact numbers match, so hide the button
                        btnmessage.setVisibility(View.GONE);
                    } else if (contactNumber.equals(Contactnumber)){
                        // The contact numbers don't match, so allow button click
//                        btnmessage.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                intents = new Intent(getApplicationContext(), PlaceOrder.class);
//                                // Pass the necessary data to PlaceOrder (e.g., contactNumber)
//                                intents.putExtra("contactNumber", contactNumber);
//                                intents.putExtra("itemName", itemName);
//                                intents.putExtra("firstImageUrl", firstImageUrl);
//                                intents.putExtra("shopName", shopName);
//                                intents.putExtra("district", address);
//
//                                // Start the PlaceOrder activity
//                                startActivity(intents);
//                               // getfirstimage();
//                            }
//                        });
                    } else {
                        btnmessage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

//                                DatabaseReference couponRef = FirebaseDatabase.getInstance().getReference("Products").child(itemContactNumber).child(itemkey).child("coupons");
//
//                                couponRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//                                        if (snapshot.exists()) {
//                                            String back = snapshot.child("back").getValue(String.class);
//                                            String front = snapshot.child("front").getValue(String.class);
//                                            String extraAmt = snapshot.child("extraAmt").getValue(String.class);
//
//                                            // The "coupons" node is present, show the bottom sheet dialog
//                                            bottomSheetDialog();
//                                        } else {
//                                            // The "coupons" node is not present, handle accordingly (e.g., show a message)
//                                            Toast.makeText(ItemDetails.this, "No coupons available", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//                                        // Handle onCancelled
//                                    }
//                                });

                                placeorderbtn();

//                                String quantity = enterqty.getText().toString().trim();
//                                if (quantity.isEmpty()){
//                                    enterqty.setError("PLease enter quantity.");
//                                } else {
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetails.this);
//                                    builder.setTitle("Place Order");
//                                    builder.setMessage("Are you sure you want to place the order?");
//                                    builder.setPositiveButton("Order", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            // Place the order logic goes here
//
//                                            long clickTime = System.currentTimeMillis();
//
//                                            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_INTERVAL) {
//                                                // Double click detected, send a message
//                                                sendMessage();
//                                                //bottom();
//                                                // You can add code here to update the UI or show a success message
//                                            } else {
//                                                // Single click detected and quantity is not empty, place an order
//                                                placeOrder(clickTime);
//                                                // textView.setText("Order is placed successfully with quantity: " + quantity);
//                                                // Set the flag to true when the submit button is clicked
//
//                                            }
//
////                lastClickTime = clickTime; // Update the last click time
////
////                // Store the current time to Firebase
////                storeCurrentTimeToFirebase(clickTime);
//
//                                            userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot currentUsersnapshot) {
//                                                    if (currentUsersnapshot.exists()) {
//                                                        String currentusercontactNum = currentUsersnapshot.child("contactNumber").getValue(String.class);
//                                                        System.out.println("sfsgr " + currentusercontactNum);
//                                                        String currentUserName = currentUsersnapshot.child("name").getValue(String.class);
//
//                                                        // Get a reference to the "notification" node under "shopRef"
//                                                        DatabaseReference notificationRef = databaseRef.child(itemContactNumber).child("notification");
//
//                                                        // Generate a random key for the notification
//                                                        String notificationKey = notificationRef.push().getKey();
//
//                                                        // Create a message
//                                                        String message = currentUserName + " ordered " + itemName + " product.";
//                                                        String order = itemName;
//
//                                                        // Create a map to store the message
//                                                        Map<String, Object> notificationData = new HashMap<>();
//                                                        notificationData.put("message", message);
//                                                        notificationData.put("order", order);
//                                                        notificationData.put("orderkey", orderKey);
//
//                                                        // Store the message under the generated key
//                                                        if (!TextUtils.isEmpty(orderKey)) {
//                                                            // Notification data setup and setting it to the database
//                                                            notificationRef.child(orderKey).setValue(notificationData);
//                                                        }
//                                                        DatabaseReference shopNotificationCountRef = databaseRef.child(itemContactNumber)
//                                                                .child("notificationcount");
//                                                        DatabaseReference NotificationCountRef = databaseRef.child(itemContactNumber).child("count")
//                                                                .child("notificationcount");
//
//                                                        // Read the current count and increment it by 1
//                                                        shopNotificationCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                                int currentCount = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
//                                                                int newCount = currentCount + 1;
//
//                                                                // Update the notification count
//                                                                shopNotificationCountRef.setValue(newCount);
//                                                                NotificationCountRef.setValue(newCount);
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(@NonNull DatabaseError error) {
//                                                                // Handle onCancelled event
//                                                            }
//                                                        });
//
//
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//                                                }
//                                            });
//                                            // Close the dialog
//                                            dialogInterface.dismiss();
//                                            placeOrderDialog();
//                                        }
//                                    });
//                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            // User clicked on Cancel, so just close the dialog
//                                            dialogInterface.dismiss();
//                                        }
//                                    });
//
//                                    AlertDialog alertDialog = builder.create();
//                                    alertDialog.show();
//                                }
                            }
                        });

                    }



                    shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                String itemName = itemSnapshot.child("itemname").getValue(String.class);
                                String itemPrice = itemSnapshot.child("price").getValue(String.class);
                                //String itemPrice = itemPriceTextView.getText().toString();

                                DatabaseReference requestsRef = databaseReference.child(contactNumber).child("requests");
                                //DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference().child("Shop").child(shopId).child("requests");


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle the error gracefully
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error gracefully
            }
        });

    }

    private  void placeorderbtn(){
        DatabaseReference couponRef = FirebaseDatabase.getInstance().getReference("Products").child(itemContactNumber).child(itemkey).child("coupons");

        couponRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String back = snapshot.child("back").getValue(String.class);
                    String front = snapshot.child("front").getValue(String.class);
                    String extraAmt = snapshot.child("extraAmt").getValue(String.class);

                    // The "coupons" node is present, show the bottom sheet dialog
                    bottomSheetDialog();
                } else {
                    // The "coupons" node is not present, handle accordingly (e.g., show a message)
                    String quantity = enterqty.getText().toString().trim();
                                if (quantity.isEmpty()){
                                    enterqty.setError("PLease enter quantity.");
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetails.this);
                                    builder.setTitle("Place Order");
                                    builder.setMessage("Are you sure you want to place the order?");
                                    builder.setPositiveButton("Order", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // Place the order logic goes here

                                            long clickTime = System.currentTimeMillis();

                                            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_INTERVAL) {
                                                // Double click detected, send a message
                                                sendMessage();
                                                //bottom();
                                                // You can add code here to update the UI or show a success message
                                            } else {
                                                // Single click detected and quantity is not empty, place an order
                                                placeOrder(clickTime);
                                                // textView.setText("Order is placed successfully with quantity: " + quantity);
                                                // Set the flag to true when the submit button is clicked

                                            }

//                lastClickTime = clickTime; // Update the last click time
//
//                // Store the current time to Firebase
//                storeCurrentTimeToFirebase(clickTime);

                                            userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot currentUsersnapshot) {
                                                    if (currentUsersnapshot.exists()) {
                                                        String currentusercontactNum = currentUsersnapshot.child("contactNumber").getValue(String.class);
                                                        System.out.println("sfsgr " + currentusercontactNum);
                                                        String currentUserName = currentUsersnapshot.child("name").getValue(String.class);

                                                        // Get a reference to the "notification" node under "shopRef"
                                                        DatabaseReference notificationRef = databaseRef.child(itemContactNumber).child("notification");

                                                        // Generate a random key for the notification
                                                        String notificationKey = notificationRef.push().getKey();

                                                        // Create a message
                                                        String message = currentUserName + " ordered " + itemName + " product.";
                                                        String order = itemName;

                                                        // Create a map to store the message
                                                        Map<String, Object> notificationData = new HashMap<>();
                                                        notificationData.put("message", message);
                                                        notificationData.put("order", order);
                                                        notificationData.put("orderkey", orderKey);

                                                        // Store the message under the generated key
                                                        if (!TextUtils.isEmpty(orderKey)) {
                                                            // Notification data setup and setting it to the database
                                                            notificationRef.child(orderKey).setValue(notificationData);
                                                        }
                                                        DatabaseReference shopNotificationCountRef = databaseRef.child(itemContactNumber)
                                                                .child("notificationcount");
                                                        DatabaseReference NotificationCountRef = databaseRef.child(itemContactNumber).child("count")
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
                                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                                }
                                            });
                                            // Close the dialog
                                            dialogInterface.dismiss();
                                            placeOrderDialog();
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // User clicked on Cancel, so just close the dialog
                                            dialogInterface.dismiss();
                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }
    private void sendMessage() {


        // Send a message in the Firebase Realtime Database with status "sent"
        DatabaseReference messagesRef = databaseRef.child(itemContactNumber).child("messages");
        String messageId = messagesRef.push().getKey();
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("text", "Your message text");
        messageData.put("senderId", userId);
        messageData.put("status", "sent"); // Status is initially set to "sent"
        messagesRef.child(messageId).setValue(messageData);
    }


    private void placeOrder(long clickTime) {

        // Format the timestamp as "hh:mm a" (e.g., "10:00 AM")
        SimpleDateFormat TimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        formattedTime = TimeFormat.format(new Date(clickTime));

        // Format the timestamp as "hh:mm a" (e.g., "10:00 AM")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ", Locale.getDefault());
        formattedDate = dateFormat.format(new Date(clickTime));

        // Place the order in the Firebase Realtime Database with status "pending"
        DatabaseReference ordersRef = databaseRef.child(itemContactNumber).child("orders").child(contactNumber);
        orderKey = generateShortRandomId(itemkey);
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("itemName", itemName);
        orderData.put("firstImageUrl", firstImageUrl);
        orderData.put("buyerContactNumber", contactNumber);
        System.out.println("shgbhb" + currentuserName);
        orderData.put("buyerName", currentuserName);
        orderData.put("orderkey", orderKey);
        orderData.put("quantity", enterqty.getText().toString());
        orderData.put("status", "pending"); // Status is initially set to "pending"
        orderData.put("timestamp", formattedTime);
        orderData.put("datetamp", formattedDate);
        orderData.put("shopOwnerContactNumber", itemContactNumber);
        orderData.put("shopImage", shopImage);
        orderData.put("shopName", shopName);
        orderData.put("senderID", userId);
        orderData.put("receiverID", itemContactNumber);
       String amt = String.valueOf(totalamts);
        orderData.put("totalAmt", amt);
        // Store the success message as a user message
        String successMessage = "Order is placed successfully of " +itemName+ " with quantity: " + enterqty.getText().toString();
// String userMessageKey = "usermsg" + messageIndex;
        DatabaseReference userMessageref = databaseRef.child(itemContactNumber).child("orders").child(contactNumber)
                .child("chats").child(orderKey);
        String messageKey = userMessageref.push().getKey();
        // Create a Map to represent the message, sender, and receiver
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("message", successMessage);
        messageData.put("sender", userId); // Set sender as current user ID
        messageData.put("receiver", itemContactNumber); // Set receiver as shop contact number
        messageData.put("timestamp", formattedTime);
        messageData.put("datetamp", formattedDate);
        messageData.put("chatkey", messageKey);
// Print out the messageData to ensure it's not null
        System.out.println("messageData: " + messageData);

        ordersRef.child(orderKey).setValue(orderData);
        userMessageref.child(messageKey).setValue(messageData);

        // Place the order in the Firebase Realtime Database with status "pending"
        DatabaseReference orderedRef = userRef.child(userId).child("ordered").child(itemContactNumber);
        // DatabaseReference MyordersRef = userRef.child(userId).child("Myorders");
        orderData.put("itemName", itemName);
        orderData.put("firstImageUrl", firstImageUrl);
        orderData.put("buyerContactNumber", contactNumber);
        System.out.println("shgbhb" + currentuserName);
        orderData.put("buyerName", currentuserName);
        orderData.put("orderkey", orderKey);
        orderData.put("quantity", enterqty.getText().toString());
        orderData.put("status", "pending"); // Status is initially set to "pending"
        orderData.put("timestamp", formattedTime);
        orderData.put("datetamp", formattedDate);
        orderData.put("shopOwnerContactNumber", itemContactNumber);
        orderData.put("shopImage", shopImage);
        orderData.put("shopName", shopName);
        orderData.put("senderID", userId);
        orderData.put("receiverID", itemContactNumber);
        orderData.put("totalAmt", amt);
        // Add a timestamp field to the ordered item data
        //orderData.put("timestamp", formattedTime);
        DatabaseReference userMessageRef = userRef.child(userId).child("ordered").child(itemContactNumber)
                .child("chats").child(orderKey);

// Continue with storing other order data in the "ordered" node
        orderedRef.child(orderKey).setValue(orderData);
        //  MyordersRef.child(orderKey).setValue(orderData);
        userMessageRef.child(messageKey).setValue(messageData);

        DatabaseReference shopNodeRef = databaseRef.child(itemContactNumber);
        DatabaseReference orderCountRef = shopNodeRef.child("ordercount");
        DatabaseReference countRef = databaseRef.child(itemContactNumber).child("count").child("ordercount");

        // Use a ValueEventListener to get the current order count and update it
        orderCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long currentOrderCount = 0;
                if (dataSnapshot.exists()) {
                    currentOrderCount = (long) dataSnapshot.getValue();
                }

                // Increment the order count and update it in the database
                currentOrderCount++;
                orderCountRef.setValue(currentOrderCount);
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

                // Increment the order count and update it in the database
                currentOrderCount++;
                countRef.setValue(currentOrderCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });


//        ChatMessage chatMessage = new ChatMessage();
//        chatMessage.setMessage(successMessage);
//        chatMessage.setSender(userId);
//        chatMessage.setReceiver(shopContactNumber);
//        chatMessage.setTimestamp(formattedTime);
//       // chatMessage.setDatetamp(formattedDate);
//
//        combinedAdapter.addMessage(chatMessage); // Add the message to your adapter's dataset
//        recyclerView.scrollToPosition(combinedAdapter.getItemCount() - 1); // Scroll to the new message
//        combinedAdapter.notifyItemInserted(combinedAdapter.getItemCount() - 1); // Notify the adapter about the new item

    }

    public static String generateShortRandomId(String mobileNumber) {
        // Get the current timestamp in milliseconds
        long timestamp = System.currentTimeMillis();

        // Format the timestamp (including milliseconds)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String formattedTimestamp = dateFormat.format(new Date(timestamp));

        // Combine mobile number and timestamp
        String timestampId = mobileNumber + formattedTimestamp;

        // Generate a random ID from the timestampId using SHA-256 hashing
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(timestampId.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // Take the first 10 characters as the shortened random ID
            return hexString.toString().substring(0, 15);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception according to your application's needs
            return null;
        }
    }

    private void updateBackground(RadioButton radioButton) {
        if (toggle) {
            radioButton.setBackgroundColor(getResources().getColor(R.color.mainbrandcolor));
        } else {
            radioButton.setBackground(getResources().getDrawable(R.drawable.checkbox_selector));
            wholesalelay.setVisibility(View.GONE);
        }
    }

    private void onClick(int i) {
    }

    @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
        ItemDetails.this.finish();
    }

    private void readDataFromFirebase() {
        databaseReference.child(itemContactNumber).addValueEventListener(new ValueEventListener() {
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
                        shopName = dataSnapshot.child("shopName").getValue(String.class);
                        //Log.d("FirebaseData", "shopName: " + shopName);
                        String contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                         address = dataSnapshot.child("address").getValue(String.class);
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

                            ItemList item = new ItemList(shopName,url,contactNumber, itemName, price, sellprice,
                                    description, firstimage, itemkey, imageUrls, district, taluka,address, offer, wholesale,
                                    minqty, servingArea, status, itemCate);
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


    private void storeItemNameOnly(DatabaseReference ordersRef, String itemName) {
        // Fetch the current list of item names
        ordersRef.child("itemNames").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> itemNames = new ArrayList<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String existingItemName = itemSnapshot.getValue(String.class);
                        itemNames.add(existingItemName);
                    }
                }

                // Add the new item name to the list
                itemNames.add(itemName);

                // Update the item names in the Firebase node
                ordersRef.child("itemNames").setValue(itemNames)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Order successfully stored in the database
                                // You can show a confirmation message if needed
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure to store the order
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    public void getfirstimage() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    Log.d("fgsdgfsdgsdf", "" + contactNumber);
//                    String ContactNumber = getIntent().getStringExtra("contactNumber");
//                    String itemkey = getIntent().getStringExtra("itemkey");
                    DatabaseReference ref = database.getReference("Shop/" + itemContactNumber + "/items/" + itemkey + "/firstImageUrl/" );
                    System.out.println("fsdfsdf" +ref);
                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String firstImageUrl = dataSnapshot.getValue(String.class);
                                System.out.println("dfsd " + firstImageUrl);
                             //   intent = new Intent(getApplicationContext(), PlaceOrder.class);
                                // Create an Intent to pass the value to the next activity
                                intents.putExtra("firstImageUrl", firstImageUrl);
                                intents.putExtra("itemName", itemName);
                                intents.putExtra("itemPrice", itemPrice);

                                // Start the next activity
                                startActivity(intents);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Log.w(TAG, "onCancelled", databaseError.toException());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

    }

    public void getData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    Log.d("fgsdgfsdgsdf", "" + contactNumber);
                    DatabaseReference ref = database.getReference("Products/" + itemContactNumber + "/" + itemkey + "/imageUrls/");
                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
                    System.out.println("wedsvxc " +ref);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int x = 0;
                                 imageUrls = new ArrayList<>();
                                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                    String img = String.valueOf(dataSnapshot.child(String.valueOf(i)).getValue());
                                    Log.d("fgsdgfsdgsdfzx", "" + img);
                                    imageUrls.add(img);
                                     size = imageUrls.size();
                                    System.out.println("regfr " +size);
//                                    if (x++ == dataSnapshot.getChildrenCount() - 1) {
//                                        // Log.d("ovov",""+uname);
//                                        ItemList shop = new ItemList();
//                                        // Update the image URLs in the Shop object
//                                         shop.setImagesUrls(imageUrls);
//                                        // Notify the adapter about the data change
//                                        imageAdapter.setImagesUrls(imageUrls);
//                                        imageAdapter.notifyDataSetChanged();
//                                    }

                                }
                                imageAdapter.setImagesUrls(imageUrls);
                                imageAdapter.notifyDataSetChanged();
                            } else {
                                // Log.d("sasff","no "+expdate);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Log.w(TAG, "onCancelled", databaseError.toException());
                        }
                    });

                    DatabaseReference imageref = database.getReference("Products/" + contactNumber + "/" + itemkey + "/imageUrls/");
                    //final Query latest = ref.child(langx.getSelectedItem().toString()).orderByKey().limitToLast(1);
                    imageref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int x = 0;
                                 imageUrls = new ArrayList<>();
                                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                    String img = String.valueOf(dataSnapshot.child(String.valueOf(i)).getValue());
                                    Log.d("ImageURL", "Index: " + i + " - URL: " + img);
                                    imageUrls.add(img);
                                }
                                imageAdapter.setImagesUrls(imageUrls);
                                imageAdapter.notifyDataSetChanged();

                            } else {
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });



                }
            }

                    @Override
                    public void onCancelled (DatabaseError error){

                    }
                });
    }

    private void createDots(int numberOfDots) {
        dotsLayout.removeAllViews();
        for (int i = 0; i < numberOfDots; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_unselected));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dotsLayout.addView(dot);
        }
    }

    private void updateDots() {
        int totalDots = dotsLayout.getChildCount();
        int visiblePosition = ((LinearLayoutManager) imageRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        if (imageUrls != null && !imageUrls.isEmpty() && visiblePosition != RecyclerView.NO_POSITION && visiblePosition < imageUrls.size()) {
            if (currentPosition >= 0 && currentPosition < totalDots) {
                ImageView previousDot = (ImageView) dotsLayout.getChildAt(currentPosition);
                if (previousDot != null) {
                    previousDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_unselected));
                }
            }
            currentPosition = visiblePosition;

            if (currentPosition >= 0 && currentPosition < totalDots) {
                ImageView selectedDot = (ImageView) dotsLayout.getChildAt(currentPosition);
                if (selectedDot != null) {
                    selectedDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_selected));
                }
            }
        } else {
            // Debug logging to help identify the issue
            Log.d("DotsDebug", "imageUrls: " + imageUrls);
            Log.d("DotsDebug", "visiblePosition: " + visiblePosition);
            Log.d("DotsDebug", "currentPosition: " + currentPosition);
            Log.d("DotsDebug", "totalDots: " + totalDots);

            if (totalDots > 0) {
                currentPosition = 0; // Set the current position to the first dot
                ImageView selectedDot = (ImageView) dotsLayout.getChildAt(currentPosition);
                if (selectedDot != null) {
                    selectedDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_selected));
                }
            }
        }
    }


    @Override
    public void onGalleryClick(int position) {

    }

    private void placeOrderDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.place_order_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button chatbtn = dialog.findViewById(R.id.chatButton);
        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderDetailsIntent = new Intent(getApplicationContext(), UserOrderdetails.class);
                orderDetailsIntent.putExtra("buyerContactNumber", userId);
                orderDetailsIntent.putExtra("orderkey", orderKey);
                orderDetailsIntent.putExtra("ownercontactNumber" , itemContactNumber);
                boolean isBottonCardVisible = false; // Set this to true if you want it initially visible
                orderDetailsIntent.putExtra("isBottonCardVisible", isBottonCardVisible);
                startActivity(orderDetailsIntent);
            }
        });


    }

    private void bottomSheetDialog(){
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.placeorder_bottom_sheet, null);

        // Customize the BottomSheetDialog as needed
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView back = bottomSheetView.findViewById(R.id.back);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ScratchCardView scratchCardView = bottomSheetView.findViewById(R.id.scrachCard);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        KonfettiView konfettiView = bottomSheetView.findViewById(R.id.konfettiView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView couponBackImg = bottomSheetView.findViewById(R.id.couponback);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView coupontext = bottomSheetView.findViewById(R.id.coupontext);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        DatabaseReference couponRef = FirebaseDatabase.getInstance().getReference("Products").child(itemContactNumber).child(itemkey).child("coupons");

        couponRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String back= snapshot.child("back").getValue(String.class);
                    String front= snapshot.child("front").getValue(String.class);
                    String extraAmt = snapshot.child("extraAmt").getValue(String.class);

                    Glide.with(ItemDetails.this).load(back).into(couponBackImg);
                    scratchCardView.setScratchImageUrl(front);
                    coupontext.setText("₹ "+extraAmt);

                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });

        scratchCardView.setRevealListener(new ScratchCardView.RevealListener() {
            @Override
            public void onRevealed() {
                // Handle reveal completion
                // Toast.makeText(Scratch_Coupon.this, "ol", Toast.LENGTH_SHORT).show();
                scratchCardView.setVisibility(View.GONE);

                konfettiView.build()
                        .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(1000L)
                        .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                        .addSizes(new Size(12,5))
                        .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                        //.setPosition(50f, konfettiView.getWidth()+10f, -50f, -50f)
                        .streamFor(300, 1000L);

            }
        });

        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }

    @Override
    public void onRevealed() {
        // Handle the reveal event here
        Toast.makeText(this, "Card Revealed!", Toast.LENGTH_SHORT).show();
    }
}


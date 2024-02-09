package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import io.reactivex.rxjava3.annotations.NonNull;

public class PlaceOrder extends AppCompatActivity implements CombinedAdapter.OrderClickListener {

    TextView shopnametextview, locationtextview, itemNameTextView, intrestedtextview, timetextview, errortextview, textView,
    timetextview1, timetextview2;
    ImageView callImageView, itemimage;
    EditText quantityedittext;
    Button button1, button2, button3, button4, button5, submitButton;
    RelativeLayout relativeLayout, relativeLayout1, relativeLayout2;
    String contactNumber, userId, currentuserName, shopContactNumber,shopimage, shopcontactNumber;
    private DatabaseReference databaseRef;
    private DatabaseReference userRef;
    DatabaseReference requestRef, orderHistoryRef, chatRef;
    private static final long DOUBLE_CLICK_TIME_INTERVAL = 300; // Define the time interval for a double click (in milliseconds)
    private long lastClickTime = 0; // Initialize the last click time
    String itemName,firstImageUrl, shopuserid, orderKey, formattedTime,formattedDate, chatorderkey, name, shopname;
    RecyclerView recyclerView;
    private CombinedAdapter combinedAdapter;
    private List<Object> combinedList;
    OrderHistoryAdapter adapter;
    private Set<String> addedOrderKeys = new HashSet<>();
    private Set<String> addedChatMessageKeys = new HashSet<>();
    private Set<String> addedButtonChatMessageKeys = new HashSet<>();
    private List<Order> orderHistoryList = new ArrayList<>();
    List<ChatMessage> chatMessageList = new ArrayList<>();
    boolean submitButtonClicked = false;
    private static final String USER_ID_KEY = "userID";
    private boolean isButtonVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        shopnametextview = findViewById(R.id.shopnameTextView);
        locationtextview = findViewById(R.id.loactiontextview);
        callImageView = findViewById(R.id.callImageView);
        itemimage = findViewById(R.id.itemimage);
        intrestedtextview = findViewById(R.id.intrestedtextview);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        quantityedittext = findViewById(R.id.quantityedittext);
        timetextview = findViewById(R.id.timetextview);
        errortextview = findViewById(R.id.errortext);
        textView = findViewById(R.id.textview);
        relativeLayout = findViewById(R.id.relative1);
        relativeLayout1 = findViewById(R.id.itemdetails);
        relativeLayout2 = findViewById(R.id.relative2);
        timetextview1 = findViewById(R.id.timetextview);
        timetextview2 = findViewById(R.id.timeTextView);
        NestedScrollView scrollView = findViewById(R.id.scrollView);


        // Request focus for "relative2" to scroll to it
//        relativeLayout2.requestFocus();
//
//        // Smoothly scroll to "relative2"
//        scrollView.post(new Runnable() {
//            @Override
//            public void run() {
//                scrollView.smoothScrollTo(0, relativeLayout2.getTop());
//            }
//        });

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentTime = sdf.format(calendar.getTime());

        // Set the current time to both TextViews
        timetextview1.setText(currentTime);
        timetextview2.setText(currentTime);


       bottom();

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference().child("Shop");
        userRef = database.getReference("Users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        SharedPreferences  sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
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
            // Add this line to adjust the window when the keyboard opens
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
            View decorsView = window.getDecorView();
            // Make the status bar icons dark
            decorsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }


        Intent intent = getIntent();
        if (intent != null) {
            itemName = intent.getStringExtra("itemName");
            firstImageUrl = getIntent().getStringExtra("firstImageUrl");

            itemNameTextView = findViewById(R.id.ItemName);
            itemNameTextView.setText(itemName);
            intrestedtextview.setText("I am intrested in " +itemName);

            // Load the image into the ImageView using Glide
            Glide.with(this)
                    .load(firstImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemimage);

        }

        shopname = getIntent().getStringExtra("shopName");
        String location = getIntent().getStringExtra("district");
        button1.setText("Hi " +shopname);
        shopnametextview.setText(shopname);
        locationtextview.setText(location);
        shopContactNumber = getIntent().getStringExtra("contactNumber");
        System.out.println("gvfrfg " +shopContactNumber);
        callImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to initiate a phone call
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shopContactNumber));
                // Start the phone call activity
                startActivity(callIntent);
            }
        });
        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("shopcontactNumber", shopContactNumber);
        editor.apply();

        Intent sharedIntent = IntentDataHolder.getSharedIntent();
        if (sharedIntent != null) {
            shopcontactNumber = sharedIntent.getStringExtra("contactNumber");

            shopimage = sharedIntent.getStringExtra("image");

            String shopName = sharedIntent.getStringExtra("ShopName");
             name = sharedIntent.getStringExtra("Name");
            String district = sharedIntent.getStringExtra("District");
            System.out.println("sfj " +shopimage);
            button1.setText("Hi " +name);
            shopnametextview.setText(shopName);
            locationtextview.setText(district);

//            DatabaseReference useridRef = databaseRef.child(shopContactNumber);
//            useridRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                       shopuserid = dataSnapshot.child("userId").getValue(String.class);
//
//                        System.out.println("sdfsfs " +shopuserid);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//                }
//            });

//            requestRef = databaseRef.child(shopContactNumber).child("requests");
//            Log.d("TAG", "aehgatha " + requestRef);

            callImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Create an Intent to initiate a phone call
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shopContactNumber));
                    // Start the phone call activity
                    startActivity(callIntent);
                }
            });

            callImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Create an Intent to initiate a phone call
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shopContactNumber));
                    // Start the phone call activity
                    startActivity(callIntent);
                }
            });

//            requestRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//
//                    // Check if userId exists in the snapshot and its value is "yes"
//                    if (snapshot.hasChild(userId) && snapshot.child(userId).getValue(String.class).equals("yes")) {
//                        // If "yes," show the call ImageView
//                       // callImageView.setVisibility(View.VISIBLE);
//                    } else {
//                        // If not "yes" or userId doesn't exist, hide the call ImageView
//                       // callImageView.setVisibility(View.GONE);
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//                }
//            });
        }

        Intent sharedItemIntent = IntentItemDataHolder.getSharedItemIntent();
        if (sharedItemIntent != null) {


        }


//
         submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = quantityedittext.getText().toString().trim();
                long clickTime = System.currentTimeMillis();

                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_INTERVAL) {
                    // Double click detected, send a message
                    sendMessage();
                    //bottom();
                    // You can add code here to update the UI or show a success message
                } else if (quantity.isEmpty()) {
                   errortextview.setVisibility(View.VISIBLE);
                } else {
                    // Single click detected and quantity is not empty, place an order
                    errortextview.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE);
                    relativeLayout1.setVisibility(View.GONE);
                    relativeLayout2.setVisibility(View.GONE);
                    scrollView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    placeOrder(clickTime);
                   // textView.setText("Order is placed successfully with quantity: " + quantity);
                    // Set the flag to true when the submit button is clicked
                    submitButtonClicked = true;
                }

//                lastClickTime = clickTime; // Update the last click time
//
//                // Store the current time to Firebase
//                storeCurrentTimeToFirebase(clickTime);

                userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot currentUsersnapshot) {
                        if (currentUsersnapshot.exists()){
                            String currentusercontactNum = currentUsersnapshot.child("contactNumber").getValue(String.class);
                            System.out.println("sfsgr " +currentusercontactNum);
                            String currentUserName = currentUsersnapshot.child("name").getValue(String.class);

                            // Get a reference to the "notification" node under "shopRef"
                            DatabaseReference notificationRef = databaseRef.child(shopContactNumber).child("notification");

                            // Generate a random key for the notification
                            String notificationKey = notificationRef.push().getKey();

                            // Create a message
                            String message = currentUserName + " ordered " +itemName + " product.";
                            String order = itemName;

                            // Create a map to store the message
                            Map<String, Object> notificationData = new HashMap<>();
                            notificationData.put("message", message);
                            notificationData.put("order", order);
                            notificationData.put("orderkey",orderKey);

                            // Store the message under the generated key
                            if (!TextUtils.isEmpty(orderKey)) {
                                // Notification data setup and setting it to the database
                                notificationRef.child(orderKey).setValue(notificationData);
                            }
                            DatabaseReference shopNotificationCountRef = databaseRef.child(shopContactNumber)
                                    .child("notificationcount");
                            DatabaseReference NotificationCountRef = databaseRef.child(shopContactNumber).child("count")
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

            }
        });

        String shopnumber = getIntent().getStringExtra("contactNumber");
        System.out.println("erfgdc " +shopnumber);

        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    currentuserName = dataSnapshot.child("name").getValue(String.class);

                    orderHistoryRef = databaseRef.child(shopnumber).child("orders")
                            .child(contactNumber);// Adjust the reference to your Firebase structure

//                    orderHistoryRef = databaseRef.child(shopContactNumber).child("orders")
//                            .child(contactNumber);// Adjust the reference to your Firebase structure
                    System.out.println("sgrgr " +currentuserName);
                    chatRef = databaseRef.child(shopnumber).child("orders").child(contactNumber)
                            .child("chats"); // Adjust the reference to your Firebase structure
                    // Fetch order history data
                    System.out.println("wewfrg" +contactNumber);

                    fetchOrderHistoryAndChatMessages(orderHistoryRef, chatRef);
                    // Fetch chat message data
                    //fetchChatMessages(chatRef);
                    System.out.println("sdfsfs " +currentuserName);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitButtonClicked) {
                    String buttonText = button1.getText().toString();
                    sendMessageToChatsNode(buttonText);
                    sendMessageTochatsNode(buttonText);
                   // bottom();
                } else {
                    // Submit button was not clicked first, store the button's text in the previous orderKey
                    Toast.makeText(PlaceOrder.this, "Please send the order first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitButtonClicked) {
                    String buttonText = "Please send me the price of "+itemName;
                    sendMessageToChatsNode(buttonText);
                    sendMessageTochatsNode(buttonText);
                   // bottom();
                } else {

                    Toast.makeText(PlaceOrder.this, "Please send the order first", Toast.LENGTH_SHORT).show();

//                    String buttonText = "Please send me the price of "+itemName;
//                    sendMessageToChatsNode(buttonText);
//                    sendMessageTochatsNode(buttonText);
//                    relativeLayout1.setVisibility(View.GONE);
//                    relativeLayout2.setVisibility(View.GONE);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitButtonClicked) {
                    String buttonText = "Hi "+shopname+ " is "+itemName+ " currently in stock?";
                    sendMessageToChatsNode(buttonText);
                    sendMessageTochatsNode(buttonText);
                 //   bottom();
                } else {
//                    String buttonText = "Hi "+name+ " is "+itemName+ " currently in stock?";
//                    sendMessageToChatsNode(buttonText);
//                    sendMessageTochatsNode(buttonText);
//                    relativeLayout1.setVisibility(View.GONE);
//                    relativeLayout2.setVisibility(View.GONE);
                    Toast.makeText(PlaceOrder.this, "Please send the order first", Toast.LENGTH_SHORT).show();

                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitButtonClicked) {
                    String buttonText = button4.getText().toString();
                    sendMessageToChatsNode(buttonText);
                    sendMessageTochatsNode(buttonText);
                    relativeLayout1.setVisibility(View.GONE);
                    relativeLayout2.setVisibility(View.GONE);
                  //  bottom();
                }else {
                    Toast.makeText(PlaceOrder.this, "Please send the order first", Toast.LENGTH_SHORT).show();

                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitButtonClicked) {
                    String buttonText = "Can you please share more details about "+itemName+ "?";
                    sendMessageToChatsNode(buttonText);
                    sendMessageTochatsNode(buttonText);
                    //bottom();
                } else {
                    Toast.makeText(PlaceOrder.this, "Please send the order first", Toast.LENGTH_SHORT).show();

                }
            }
        });



        recyclerView = findViewById(R.id.history);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // This will display data from the bottom
        recyclerView.setLayoutManager(layoutManager);
//        adapter = new OrderHistoryAdapter(orderHistoryList);
//        recyclerView.setAdapter(adapter);

        // Create sample data for order history and chat messages
        List<Order> orderHistoryList = createSampleOrderHistory();
        List<ChatMessage> chatMessageList = createSampleChatMessages();

        // Combine both lists into a single list

        combinedList = new ArrayList<>();
        combinedList.addAll(orderHistoryList);
        combinedList.addAll(chatMessageList);

        // Create and set the CombinedAdapter on the RecyclerView
        combinedAdapter = new CombinedAdapter(combinedList, sharedPreference, this::onSubmitClick);
        recyclerView.setAdapter(combinedAdapter);

        CardView createButton = findViewById(R.id.downbtn);
        NestedScrollView nestedScrollView = findViewById(R.id.scrollView);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle the button visibility when clicked
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(recyclerView.FOCUS_DOWN);
                    }
                });
                isButtonVisible = !isButtonVisible;
                createButton.setVisibility(isButtonVisible ? View.VISIBLE : View.GONE);
            }
        });
        // Initially, set the button to be invisible
        createButton.setVisibility(View.GONE);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    // Scrolling down, hide the button
                    createButton.setVisibility(View.GONE);
                } else if (dy < 0) {
                    // Scrolling up, show the button
                    createButton.setVisibility(View.VISIBLE);
                }
            }
        });


        // fetchOrderKeys();

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Sample method to create sample order history data
    private List<Order> createSampleOrderHistory() {
        List<Order> orderHistoryList = new ArrayList<>();
        return orderHistoryList;
    }


    // Sample method to create sample chat message data
    private List<ChatMessage> createSampleChatMessages() {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        // Populate the chatMessageList with sample chat messages
        return chatMessageList;
    }

    private void placeOrder(long clickTime) {

        // Format the timestamp as "hh:mm a" (e.g., "10:00 AM")
        SimpleDateFormat TimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        formattedTime = TimeFormat.format(new Date(clickTime));

        // Format the timestamp as "hh:mm a" (e.g., "10:00 AM")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ", Locale.getDefault());
        formattedDate = dateFormat.format(new Date(clickTime));

        // Place the order in the Firebase Realtime Database with status "pending"
        DatabaseReference ordersRef = databaseRef.child(shopContactNumber).child("orders").child(contactNumber);
        orderKey = ordersRef.push().getKey();
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("itemName", itemName);
        orderData.put("firstImageUrl", firstImageUrl);
        orderData.put("buyerContactNumber", contactNumber);
        System.out.println("shgbhb" + currentuserName);
        orderData.put("buyerName", currentuserName);
        orderData.put("orderkey", orderKey);
        orderData.put("quantity", quantityedittext.getText().toString());
        orderData.put("status", "pending"); // Status is initially set to "pending"
        orderData.put("timestamp", formattedTime);
        orderData.put("datetamp", formattedDate);
        orderData.put("shopOwnerContactNumber", shopContactNumber);
        orderData.put("shopImage", shopimage);
        orderData.put("shopName", shopname);
        orderData.put("senderID", userId);
        orderData.put("receiverID", shopContactNumber);
        // Store the success message as a user message
        String successMessage = "Order is placed successfully of " +itemName+ " with quantity: " + quantityedittext.getText().toString();
// String userMessageKey = "usermsg" + messageIndex;
        DatabaseReference userMessageref = databaseRef.child(shopContactNumber).child("orders").child(contactNumber)
                .child("chats").child(orderKey);
        String messageKey = userMessageref.push().getKey();
        // Create a Map to represent the message, sender, and receiver
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("message", successMessage);
        messageData.put("sender", userId); // Set sender as current user ID
        messageData.put("receiver", shopContactNumber); // Set receiver as shop contact number
        messageData.put("timestamp", formattedTime);
        messageData.put("datetamp", formattedDate);
        messageData.put("chatkey", messageKey);
// Print out the messageData to ensure it's not null
        System.out.println("messageData: " + messageData);

        ordersRef.child(orderKey).setValue(orderData);
        userMessageref.child(messageKey).setValue(messageData);

        // Place the order in the Firebase Realtime Database with status "pending"
        DatabaseReference orderedRef = userRef.child(userId).child("ordered").child(shopContactNumber);
       // DatabaseReference MyordersRef = userRef.child(userId).child("Myorders");
        orderData.put("itemName", itemName);
        orderData.put("firstImageUrl", firstImageUrl);
        orderData.put("buyerContactNumber", contactNumber);
        System.out.println("shgbhb" + currentuserName);
        orderData.put("buyerName", currentuserName);
        orderData.put("orderkey", orderKey);
        orderData.put("quantity", quantityedittext.getText().toString());
        orderData.put("status", "pending"); // Status is initially set to "pending"
        orderData.put("timestamp", formattedTime);
        orderData.put("datetamp", formattedDate);
        orderData.put("shopOwnerContactNumber", shopContactNumber);
        orderData.put("shopImage", shopimage);
        orderData.put("shopName", shopname);
        orderData.put("senderID", userId);
        orderData.put("receiverID", shopContactNumber);
        // Add a timestamp field to the ordered item data
        //orderData.put("timestamp", formattedTime);
        DatabaseReference userMessageRef = userRef.child(userId).child("ordered").child(shopContactNumber)
                .child("chats").child(orderKey);

// Continue with storing other order data in the "ordered" node
        orderedRef.child(orderKey).setValue(orderData);
      //  MyordersRef.child(orderKey).setValue(orderData);
        userMessageRef.child(messageKey).setValue(messageData);

        DatabaseReference shopNodeRef = databaseRef.child(shopContactNumber);
        DatabaseReference orderCountRef = shopNodeRef.child("ordercount");
        DatabaseReference countRef = databaseRef.child(shopContactNumber).child("count").child("ordercount");

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

    private void bottom(){
        CardView createButton = findViewById(R.id.downbtn);
        NestedScrollView nestedScrollView = findViewById(R.id.scrollView);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle the button visibility when clicked
                nestedScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                    }
                });
                isButtonVisible = !isButtonVisible;
                createButton.setVisibility(isButtonVisible ? View.VISIBLE : View.GONE);
            }
        });
        createButton.setVisibility(View.GONE);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // Scrolling down, hide the button
                    createButton.setVisibility(View.GONE);
                } else if (scrollY < oldScrollY) {
                    // Scrolling up, show the button
                    createButton.setVisibility(View.VISIBLE);
                }
            }
        });



    }
    // Function to delete an order

    // Function to send a message to the chats node
    private void sendMessageToChatsNode(String message) {
        while (true) {
            DatabaseReference ordersRef = databaseRef.child(shopContactNumber).child("orders").child(contactNumber);
            DatabaseReference userMessageRef = userRef.child(userId).child("ordered").child(shopContactNumber)
                    .child("buttonchats").child(orderKey);
                    System.out.println("sfjhhg " +ordersRef.toString());
            String ChatorderKey = userMessageRef.push().getKey();
            // Create a Map to represent the message, sender, and receiver
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", message);
            messageData.put("sender", userId); // Set sender as current user ID
           // messageData.put("receiver", shopuserid); // Set receiver as shop contact number
            messageData.put("timestamp", formattedTime);
            messageData.put("datetamp", formattedDate);
            messageData.put("chatkey", ChatorderKey);

            // Try to set the message data with the user message key
            try {
               // userMessageRef.setValue(messageData);
                userMessageRef.child(ChatorderKey).setValue(messageData);
                break; // Exit the loop if successful
            } catch (DatabaseException e) {
                // If the key already exists, increment the index and try again
                // messageIndex++;
            }
// Find the position of the chat item with the specific chatorderkey
            int targetPosition = combinedAdapter.getPositionByChatOrderKey(ChatorderKey);

            if (targetPosition != -1) {
                // Scroll to the target position
                recyclerView.scrollToPosition(targetPosition);
            } else {
                // The chatorderkey was not found in the dataset
                // Handle the case where the chatorderkey doesn't exist
            }


        }
    }

    // Function to send a message to the chats node
    private void sendMessageTochatsNode(String message) {
        while (true) {
            DatabaseReference ordersRef = databaseRef.child(shopContactNumber).child("orders").child(contactNumber)
                    .child("buttonchats").child(orderKey);
            System.out.println("userMessageRef path: " + ordersRef.toString());

            String chatorderKey = ordersRef.push().getKey();
            // Create a Map to represent the message, sender, and receiver
            Map<String, Object> messagesData = new HashMap<>();
            messagesData.put("message", message);
            messagesData.put("sender", userId); // Set sender as current user ID
            messagesData.put("receiver", shopContactNumber); // Set receiver as shop contact number
            messagesData.put("timestamp", formattedTime);
            messagesData.put("datetamp", formattedDate);
            messagesData.put("chatkey", chatorderKey);

            // Try to set the message data with the user message key
            try {
                // userMessageRef.setValue(messageData);
                ordersRef.child(chatorderKey).setValue(messagesData);
                break; // Exit the loop if successful
            } catch (DatabaseException e) {
                // If the key already exists, increment the index and try again
                // messageIndex++;
            }
// Find the position of the chat item with the specific chatorderkey
            int targetPosition = combinedAdapter.getPositionByChatOrderKey(chatorderKey);

            if (targetPosition != -1) {
                // Scroll to the target position
                recyclerView.scrollToPosition(targetPosition);
            } else {
                // The chatorderkey was not found in the dataset
                // Handle the case where the chatorderkey doesn't exist
            }


        }
    }

        private void sendMessage() {


        // Send a message in the Firebase Realtime Database with status "sent"
        DatabaseReference messagesRef = databaseRef.child(shopContactNumber).child("messages");
        String messageId = messagesRef.push().getKey();
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("text", "Your message text");
        messageData.put("senderId", userId);
        messageData.put("status", "sent"); // Status is initially set to "sent"
        messagesRef.child(messageId).setValue(messageData);
    }
    private void storeCurrentTimeToFirebase(long clickTime) {
        // Format the timestamp as "hh:mm a" (e.g., "10:00 AM")
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String formattedTime = dateFormat.format(new Date(clickTime));
        // You can store the clickTime in your Firebase database as needed.
        DatabaseReference timeRef = userRef.child(userId).child("timestamps");
        // Create a new child node with a unique key and store the formatted time
        String timestampKey = timeRef.push().getKey();
        timeRef.child(timestampKey).setValue(formattedTime);
    }



    private void fetchOrderHistoryAndChatMessages(DatabaseReference orderHistoryRef, DatabaseReference chatRef) {


        orderHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Order> orderHistoryList = new ArrayList<>();


                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    String orderKey = orderSnapshot.child("orderkey").getValue(String.class);

                    if (order != null && "pending".equals(order.getStatus()) && orderKey != null) {
                        DatabaseReference chatRef = databaseRef.child(shopContactNumber).child("orders").child(contactNumber).child("chats").child(orderKey);
                        DatabaseReference buttonchatsRef = databaseRef.child(shopContactNumber).child("orders").child(contactNumber).child("buttonchats").child(orderKey);

                        chatRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<ChatMessage> chatMessageList = new ArrayList<>();

                                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                    String messageKey = messageSnapshot.getKey();
//                                    String message = messageSnapshot.child("message").getValue(String.class);
//                                    String sender = messageSnapshot.child("sender").getValue(String.class);
//                                    String receiver = messageSnapshot.child("receiver").getValue(String.class);

                                    if (!addedChatMessageKeys.contains(messageKey)) {
                                        String message = messageSnapshot.child("message").getValue(String.class);
                                        String sender = messageSnapshot.child("sender").getValue(String.class);
                                        String receiver = messageSnapshot.child("receiver").getValue(String.class);
                                        String time = messageSnapshot.child("timestamp").getValue(String.class);
                                        String datetamp = messageSnapshot.child("datetamp").getValue(String.class);
                                        String chatkey = messageSnapshot.child("chatkey").getValue(String.class);

                                        if (message != null && sender != null && receiver != null) {
                                            ChatMessage chatMessage = new ChatMessage(sender, receiver, message, time, datetamp, chatkey);
                                            chatMessageList.add(chatMessage);
                                            addedChatMessageKeys.add(messageKey); // Add the message key to the set
                                        }
                                    }
                                }

                                // Check if the order key is not already in the addedOrderKeys set before adding it
                                if (!addedOrderKeys.contains(orderKey)) {
                                    combinedList.add(order); // Add the order details
                                    addedOrderKeys.add(orderKey); // Add the order key to the set

                                }

                                combinedList.addAll(chatMessageList);
                                // Notify the adapter that the data has changed
                                combinedAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle errors here
                            }
                        });

                        // Retrieve messages from buttonchatsRef and add them to combinedList
                        buttonchatsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<ChatMessage> buttonChatMessageList = new ArrayList<>();

                                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                    String messageKey = messageSnapshot.getKey();
                                    System.out.println("erfe " +messageKey);

                                    // Check if the message key is not already added from chatRef
                                    if (!addedButtonChatMessageKeys.contains(messageKey)) {

                                        String message = messageSnapshot.child("message").getValue(String.class);
                                        String sender = messageSnapshot.child("sender").getValue(String.class);
                                        String receiver = messageSnapshot.child("receiver").getValue(String.class);
                                        String time = messageSnapshot.child("timestamp").getValue(String.class);
                                        String datetamp = messageSnapshot.child("datetamp").getValue(String.class);
                                        String chatkey = messageSnapshot.child("chatkey").getValue(String.class);

                                        if (message != null && sender != null && receiver != null) {
                                            ChatMessage chatMessage = new ChatMessage(sender, receiver, message, time, datetamp, chatkey);
                                            buttonChatMessageList.add(chatMessage);
                                            addedButtonChatMessageKeys.add(messageKey); // Add the message key to the set

                                        }
                                    }
                                }

                                combinedList.addAll(buttonChatMessageList);
                                // Notify the adapter that the data has changed
                                combinedAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle errors here
                            }
                        });

                    }
                }

                // Add order history data to the combined list (if needed)
                // combinedList.addAll(orderHistoryList);

                // Notify the adapter that the data has changed (if needed)
                // combinedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
                Toast.makeText(PlaceOrder.this, "Error loading order history", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchOrderHistory(DatabaseReference orderHistoryRef) {
        orderHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Order> orderHistoryList = new ArrayList<>();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    chatorderkey = orderSnapshot.child("orderkey").getValue(String.class);
                    System.out.println("dfsgsg "+chatorderkey);
                    if (order != null && "pending".equals(order.getStatus())) {
                       // orderHistoryList.add(order);
                        fetchAndDisplayChatMessages(chatorderkey, order);
                    }
                }

                // Add order history data to the combined list
               // combinedList.addAll(orderHistoryList);

                // Notify the adapter that the data has changed
               // combinedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
                Toast.makeText(PlaceOrder.this, "Error loading order history", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAndDisplayChatMessages(String orderKey, Order order) {
        DatabaseReference chatRef = databaseRef.child(shopContactNumber).child("orders").child(contactNumber).child("chats").child(orderKey);
        combinedList.clear();
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<ChatMessage> chatMessageList = new ArrayList<>();

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String message = messageSnapshot.child("message").getValue(String.class);
                    String sender = messageSnapshot.child("sender").getValue(String.class);
                    String receiver = messageSnapshot.child("receiver").getValue(String.class);
                    String time = messageSnapshot.child("timestamp").getValue(String.class);
                    String datetamp = messageSnapshot.child("datetamp").getValue(String.class);
                    String chatkey = messageSnapshot.child("chatkey").getValue(String.class);

                    if (message != null && sender != null && receiver != null) {
                        ChatMessage chatMessage = new ChatMessage(sender, receiver, message, time, datetamp, chatkey);
                        chatMessageList.add(chatMessage);
                    }


                }
                // Clear the combinedList before adding new data
// Add chat message data to the combined list
                combinedList.add(order); // Add the order details
                combinedList.addAll(chatMessageList);


                // Notify the adapter that the data has changed
                combinedAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Assuming your ScrollView has an ID of scrollView
//        ScrollView scrollView = findViewById(R.id.scrollView);
//        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//        scrollView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 1); // You can adjust the delay as needed

    }


    @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
    }

    @Override
    public void onSubmitClick(int position) {


    }


//    private void fetchChatMessages(DatabaseReference chatRef) {
//        chatRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<ChatMessage> chatMessageList = new ArrayList<>();
//
//                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
//                    for (DataSnapshot messageSnapshot : chatSnapshot.getChildren()) {
//                        String message = messageSnapshot.child("message").getValue(String.class);
//                        System.out.println("sfsdfg " +message);
//                        String sender = messageSnapshot.child("sender").getValue(String.class);
//                        String receiver = messageSnapshot.child("receiver").getValue(String.class);
//
//                        if (message != null && sender != null && receiver != null) {
//                            ChatMessage chatMessage = new ChatMessage(sender, receiver, message);
//                            chatMessageList.add(chatMessage);
//                        }
//                    }
//                }
//
//                // Add chat message data to the combined list
//                combinedList.addAll(chatMessageList);
//
//                // Notify the adapter that the data has changed
//                combinedAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle errors here
//                Toast.makeText(PlaceOrder.this, "Error loading chat messages", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void loadChatMessagesFromFirebase() {
//        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference contactNumberRef =userRef.child(userId).child("ordered").child(shopContactNumber);
//
//        contactNumberRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                chatMessageList.clear(); // Clear the list to avoid duplicates
//
//                // Loop through each child node under "ContactNumber"
//                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
//                    Order order = contactSnapshot.getValue(Order.class);
//                    if (order != null) {
//                        orderHistoryList.add(order);
//                    }
//                    String contactNumberKey = contactSnapshot.getKey();
//                    DatabaseReference chatsRef = contactSnapshot.child("chats").getRef();
//                    Log.d("TAG", "loadOrderHistoryDataFromFirebase: " +chatsRef);
//
//                    chatsRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot chatDataSnapshot) {
//                            // Loop through chat messages for this specific contactNumber
//                            for (DataSnapshot chatSnapshot : chatDataSnapshot.getChildren()) {
//
//                                String message = chatSnapshot.child("message").getValue(String.class);
//                                String sender = chatSnapshot.child("sender").getValue(String.class);
//                                String receiver = chatSnapshot.child("receiver").getValue(String.class);
//                                System.out.println("shjhsdh" +message);
//
//                                // Create a ChatMessage object or process the data as needed
//                                ChatMessage chatMessage = new ChatMessage(sender, receiver, message);
//
//                                if (chatMessage != null) {
//                                    chatMessageList.add(chatMessage);
////                                    if (sender != null && userId != null && sender.equals(userId)) {
////                                        // Display on the right side
////                                        chatMessage.setIsRight(true);
////                                    } else {
////                                        // Display on the left side
////                                        chatMessage.setIsRight(false);
////                                    }
//                                }
//                            }
//
//                            // Notify your adapter that the data has changed
//                            combinedAdapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            // Handle errors here
//                            Toast.makeText(PlaceOrder.this, "Error loading chat messages", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle errors here
//                Toast.makeText(PlaceOrder.this, "Error loading contact numbers", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void loadOrderHistoryDataFromFirebase() {
//        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference orderHistoryRef = userRef.child(userId).child("ordered").child(shopContactNumber);
//        Log.d("TAG", "loadOrderHistoryDataFromFirebase: " +orderHistoryRef);
//
//        orderHistoryRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                orderHistoryList.clear(); // Clear the list to avoid duplicates
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Order order = snapshot.getValue(Order.class);
//                    if (order != null) {
//                        orderHistoryList.add(order);
//                        DatabaseReference chatRef = userRef.child(userId).child("ordered").child(shopContactNumber).child("chats");
//                        Log.d("TAG", "loadOrderHistoryDataFromFirebase: " +chatRef);
//                    }
//                }
//
//                // Notify the adapter that the data has changed
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle errors here
//                Toast.makeText(PlaceOrder.this, "Error loading order history", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}
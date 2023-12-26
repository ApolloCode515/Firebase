package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import io.reactivex.rxjava3.annotations.NonNull;

public class OrderDetails extends AppCompatActivity implements CombinedAdapter.OrderClickListener {

    private DatabaseReference databaseRef, userRef;
    private String orderKey, userId, contactNumber;
    private ImageView callImageView;
    private TextView usernameTextView,orderDetailsTextView, chatMessagesTextView;
    RecyclerView recyclerView;
    CardView bottoncard;
    LinearLayout chatbox;
    EditText sendedittext;
    private CombinedAdapter combinedAdapter;
    private List<Object> combinedList;
    OrderHistoryAdapter adapter;
    private Set<String> addedOrderKeys = new HashSet<>();
    private Set<String> addedChatMessageKeys = new HashSet<>();
    private Set<String> addedButtonChatMessageKeys = new HashSet<>();
    String receiverID, formattedTime, formattedDate;
    private static final String USER_ID_KEY = "userID";
    private boolean isButtonVisible = true;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        usernameTextView = findViewById(R.id.usernameTextView);
        callImageView = findViewById(R.id.callImageView);
        sendedittext = findViewById(R.id.sendedittext);
        bottoncard = findViewById(R.id.buttoncard);
        chatbox = findViewById(R.id.historychatbox);

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

        CardView createButton = findViewById(R.id.down);
        NestedScrollView nestedScrollView = findViewById(R.id.scrollView);

//        createButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Toggle the button visibility when clicked
//                nestedScrollView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        nestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
//                    }
//                });
//                isButtonVisible = !isButtonVisible;
//                createButton.setVisibility(isButtonVisible ? View.VISIBLE : View.GONE);
//            }
//        });
//        createButton.setVisibility(View.GONE);
//        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY > oldScrollY) {
//                    // Scrolling down, hide the button
//                    createButton.setVisibility(View.GONE);
//                } else if (scrollY < oldScrollY) {
//                    // Scrolling up, show the button
//                    createButton.setVisibility(View.VISIBLE);
//                }
//            }
//        });

        // Retrieve the selected order key from the intent
        Intent intent = getIntent();
        orderKey = intent.getStringExtra("orderKey");
        System.out.println("efuhf " +orderKey);



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



        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    System.out.println("sefsf " +contactNumber);
                 //   DatabaseReference orderRef = databaseRef.child(contactNumber).child("orders").child(orderKey);
                  //  orderHistoryRef = databaseRef.child(shopContactNumber).child("orders").child(contactNumber);// Adjust the reference to your Firebase structure
                //    System.out.println("sgrgr " +orderRef.toString());
//                    orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot Ordersnapshot) {
//                            if (Ordersnapshot.exists()) {
//
//                                for (DataSnapshot orderSnapshot : Ordersnapshot.getChildren()) {
//                                    if (orderSnapshot.hasChild("itemName") &&
//                                            orderSnapshot.hasChild("firstImageUrl") &&
//                                            orderSnapshot.hasChild("buyerContactNumber") &&
//                                            orderSnapshot.hasChild("buyerName") &&
//                                            orderSnapshot.hasChild("quantity") &&
//                                            orderSnapshot.hasChild("status") &&
//                                            orderSnapshot.hasChild("timestamp")) {
//
//                                         receiverID = orderSnapshot.child("senderID").getValue(String.class);
//
//
//                                        System.out.println("sfhgf " + receiverID);// Add more fields as needed
//                                        // Create an Order object and add it to the list
//
//                                    }
//                                }
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//                        }
//                    });
                    Intent intent = getIntent();
                    if (intent != null) {

                        String buyerContactNumber = intent.getStringExtra("buyerContactNumber");
                        System.out.println("sfdcv " +buyerContactNumber);
                        String orderkey = intent.getStringExtra("orderkey");
                        System.out.println("sfds " +orderkey);
                        String ownercontactNumber = intent.getStringExtra("ownercontactNumber");
                        System.out.println("sfds " +ownercontactNumber);
                        fetchOrderHistoryAndChatMessages(buyerContactNumber, orderkey);
                      //  fetchuserorders(buyerContactNumber, orderkey, ownercontactNumber);
                        fetchOrderHistory(buyerContactNumber,orderkey);

                        callImageView.setTag(buyerContactNumber);
                        callImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Get the phone number from the tag
                                String phoneNumber = (String) callImageView.getTag();

                                // Create an Intent to initiate a phone call
                                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));

                                // Start the phone call activity
                                startActivity(callIntent);
                            }
                        });

                        bottoncard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String message = sendedittext.getText().toString().trim();
                                long clickTime = System.currentTimeMillis();
                                // Check if the message is not empty before saving
                                if (!message.isEmpty()) {
                                    sendMessageToChatsNode(message,buyerContactNumber, orderkey, clickTime);
                                    sendMessageTochatsNode(message,buyerContactNumber, orderkey, clickTime);
                                    sendedittext.setText("");
                                } else {
                                    // Handle the case where the message is empty or whitespace
                                    Toast.makeText(getApplicationContext(), "Message cannot be empty", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    // Retrieve the extra parameter
                    boolean isBottonCardVisible = getIntent().getBooleanExtra("isBottonCardVisible", true);

                    // Set the visibility based on the extra parameter
                    if (isBottonCardVisible) {
                        chatbox.setVisibility(View.VISIBLE);
                      //  sendedittext.setVisibility(View.VISIBLE);
                    } else {
                        chatbox.setVisibility(View.GONE);
                       // sendedittext.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });




        recyclerView = findViewById(R.id.history); // Replace with your RecyclerView ID
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        // Create sample data for order history and chat messages
        List<Order> orderHistoryList = createSampleOrderHistory();
        List<ChatMessage> chatMessageList = createSampleChatMessages();
        combinedList = new ArrayList<>();
        combinedList.addAll(orderHistoryList);
        combinedList.addAll(chatMessageList);
// Assuming you have a reference to your RecyclerView named "history"


        // Combine the adapters into a single CombinedAdapter
        combinedAdapter = new CombinedAdapter(combinedList, sharedPreference, this);
        recyclerView.setAdapter(combinedAdapter);
        recyclerView.scrollToPosition(combinedList.size() - 1);
        // Fetch and display chat messages
    //    fetchChatMessages();

        // Scroll to the last item in the RecyclerView
//        if (combinedList.size() > 0) {
//            recyclerView.scrollToPosition(combinedList.size() - 1);
//        }

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

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("dataUpdated", true);
                setResult(RESULT_OK, intent);
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

    private void sendMessageToChatsNode(String message,String buyerContactNumber, String orderkey, long clickTime) {
        while (true) {

            SimpleDateFormat TimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            formattedTime = TimeFormat.format(new Date(clickTime));

            // Format the timestamp as "hh:mm a" (e.g., "10:00 AM")
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ", Locale.getDefault());
            formattedDate = dateFormat.format(new Date(clickTime));

            DatabaseReference userMessageRef =databaseRef.child(contactNumber).child("orders")
                    .child(buyerContactNumber).child("buttonchats").child(orderkey);

            DatabaseReference userMessagehistoryRef =databaseRef.child(contactNumber).child("history")
                    .child(buyerContactNumber).child("buttonchats").child(orderkey);

            System.out.println("sfjhhg " +userMessageRef.toString());
            String ChatorderKey = userMessageRef.push().getKey();
            // Create a Map to represent the message, sender, and receiver
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", message);
            messageData.put("sender", userId); // Set sender as current user ID
            messageData.put("receiver", receiverID); // Set receiver as shop contact number
            messageData.put("timestamp", formattedTime);
            messageData.put("datetamp", formattedDate);

            // Try to set the message data with the user message key
            try {
                // userMessageRef.setValue(messageData);
                userMessageRef.child(ChatorderKey).setValue(messageData);
                userMessagehistoryRef.child(ChatorderKey).setValue(messageData);
                break; // Exit the loop if successful
            } catch (DatabaseException e) {
                // If the key already exists, increment the index and try again
                // messageIndex++;
            }

        }
    }

    private void sendMessageTochatsNode(String message,String buyerContactNumber, String orderkey, long clickTime) {
        while (true) {

            DatabaseReference userMessageRef =userRef.child(receiverID).child("ordered")
                    .child(contactNumber).child("buttonchats").child(orderkey);
            // System.out.println("sfjhhg " +ordersRef.toString());
            String ChatorderKey = userMessageRef.push().getKey();
            // Create a Map to represent the message, sender, and receiver
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", message);
            messageData.put("sender", userId); // Set sender as current user ID
            messageData.put("receiver", receiverID); // Set receiver as shop contact number
            messageData.put("timestamp", formattedTime);
            messageData.put("datetamp", formattedDate);

            // Try to set the message data with the user message key
            try {
                // userMessageRef.setValue(messageData);
                userMessageRef.child(ChatorderKey).setValue(messageData);
                break; // Exit the loop if successful
            } catch (DatabaseException e) {
                // If the key already exists, increment the index and try again
                // messageIndex++;
            }

        }
    }

    private void scrollToLastItem() {
        if (recyclerView != null && combinedAdapter.getItemCount() > 0) {
            // Scroll to the last item in the RecyclerView
            recyclerView.scrollToPosition(combinedAdapter.getItemCount() - 1);
        }
    }


    private void fetchOrderHistoryAndChatMessages(String buyercontactNumber, String orderkey) {

        DatabaseReference orderRef = databaseRef.child(contactNumber).child("orders").child(buyercontactNumber).child(orderkey);
        System.out.println("dvjfbvjf " +orderRef);
        DatabaseReference chatRef = databaseRef.child(contactNumber).child("orders").child(buyercontactNumber).child("chats").child(orderkey);
        DatabaseReference buttonchatsRef = databaseRef.child(contactNumber).child("orders").child(buyercontactNumber).child("buttonchats").child(orderkey);
        System.out.println("diuh " +buttonchatsRef);

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    String username = dataSnapshot.child("buyerName").getValue(String.class);
                    receiverID = dataSnapshot.child("senderID").getValue(String.class);
                    usernameTextView.setText(username);
                    System.out.println("adsdf " +receiverID);
                    List<ChatMessage> chatMessageList = new ArrayList<>();
                    chatMessageList.clear();



                    if (order != null && "pending".equals(order.getStatus()) && orderkey != null) {

                        chatRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot dataSnapshot) {


                                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                    String messageKey = messageSnapshot.getKey();


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
                            public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError databaseError) {
                                // Handle errors here
                            }
                        });

                        // Retrieve messages from buttonchatsRef and add them to combinedList
                        buttonchatsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot dataSnapshot) {
                                List<ChatMessage> buttonChatMessageList = new ArrayList<>();
                                addedChatMessageKeys.clear();
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
                            public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError databaseError) {
                                // Handle errors here
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
                Toast.makeText(OrderDetails.this, "Error loading order history", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchuserorders(String buyercontactNumber, String orderkey, String ownercontactNumber) {
        DatabaseReference userorderRef = databaseRef.child(ownercontactNumber).child("orders").child(buyercontactNumber).child(orderkey);
        System.out.println("dfgfb " +userorderRef);
        DatabaseReference userchatRef = databaseRef.child(ownercontactNumber).child("orders").child(buyercontactNumber).child("chats").child(orderkey);
        DatabaseReference userbuttonchatsRef = databaseRef.child(ownercontactNumber).child("orders").child(buyercontactNumber).child("buttonchats").child(orderkey);
        System.out.println("diuh " +userbuttonchatsRef);

        userorderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    String username = dataSnapshot.child("buyerName").getValue(String.class);
                    receiverID = dataSnapshot.child("senderID").getValue(String.class);
                    usernameTextView.setText(username);
                    System.out.println("adsdf " +receiverID);
                    List<ChatMessage> chatMessageList = new ArrayList<>();
                    chatMessageList.clear();



                    if (order != null && "pending".equals(order.getStatus()) && orderkey != null) {

                        userchatRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot dataSnapshot) {


                                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                    String messageKey = messageSnapshot.getKey();


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
                            public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError databaseError) {
                                // Handle errors here
                            }
                        });

                        // Retrieve messages from buttonchatsRef and add them to combinedList
                        userbuttonchatsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot dataSnapshot) {
                                List<ChatMessage> buttonChatMessageList = new ArrayList<>();
                                addedChatMessageKeys.clear();
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
                            public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError databaseError) {
                                // Handle errors here
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
                Toast.makeText(OrderDetails.this, "Error loading order history", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchOrderHistory(String buyercontactNumber, String orderkey) {

        DatabaseReference orderRef = databaseRef.child(contactNumber).child("history").child(buyercontactNumber).child(orderkey);

        DatabaseReference chatRef = databaseRef.child(contactNumber).child("history").child(buyercontactNumber).child("chats").child(orderkey);
        DatabaseReference buttonchatsRef = databaseRef.child(contactNumber).child("history").child(buyercontactNumber).child("buttonchats").child(orderkey);
        System.out.println("diuh " +buttonchatsRef);
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    String username = dataSnapshot.child("buyerName").getValue(String.class);
                    receiverID = dataSnapshot.child("senderID").getValue(String.class);
                    usernameTextView.setText(username);
                    System.out.println("adsdf " +receiverID);
                    List<ChatMessage> chatMessageList = new ArrayList<>();
                    chatMessageList.clear();



                    if (order != null && "pending".equals(order.getStatus()) && orderkey != null) {

                        chatRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot dataSnapshot) {


                                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                    String messageKey = messageSnapshot.getKey();


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
                            public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError databaseError) {
                                // Handle errors here
                            }
                        });

                        // Retrieve messages from buttonchatsRef and add them to combinedList
                        buttonchatsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot dataSnapshot) {
                                List<ChatMessage> buttonChatMessageList = new ArrayList<>();
                                addedChatMessageKeys.clear();
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
                            public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError databaseError) {
                                // Handle errors here
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
                Toast.makeText(OrderDetails.this, "Error loading order history", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onBackPressed() {
        // Navigate to the previous page when the back button is pressed
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("dataUpdated", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSubmitClick(int position) {

    }
}
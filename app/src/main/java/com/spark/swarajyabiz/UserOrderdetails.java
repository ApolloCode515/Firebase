package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
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
import com.google.firebase.database.ChildEventListener;
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

public class UserOrderdetails extends AppCompatActivity implements CombinedAdapter.OrderClickListener{

        private DatabaseReference databaseRef, userRef;
        private String orderKey, userId, contactNumber;
        private ImageView callImageView;
        private TextView usernameTextView,orderDetailsTextView, chatMessagesTextView;
        RecyclerView recyclerView;
        CardView buttoncard;
        LinearLayout chatbox;
        EditText sendedittext;
         String ownercontactNumber,formattedTime,formattedDate;
        private CombinedAdapter combinedAdapter;
        private List<Object> combinedList;
        OrderHistoryAdapter adapter;
        private Set<String> addedOrderKeys = new HashSet<>();
        private Set<String> addedChatMessageKeys = new HashSet<>();
        private Set<String> addedButtonChatMessageKeys = new HashSet<>();
        String receiverID;
        private static final String USER_ID_KEY = "userID";
        private boolean isButtonVisible = true;
        List<ChatMessage> chatMessageList;
        String lastChatMessageKey = "";
        String lastButtonChatMessageKey = "";
         private long lastClickTime = 0;
    private static final long DOUBLE_CLICK_TIME_INTERVAL = 300;

        @SuppressLint({"WrongViewCast", "MissingInflatedId"})
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_orderdetails);

            usernameTextView = findViewById(R.id.usernameTextView);
            callImageView = findViewById(R.id.callImageView);
            sendedittext = findViewById(R.id.sendedittext);
            buttoncard = findViewById(R.id.bottoncard);
            chatbox = findViewById(R.id.historychatbox);

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
            CardView createButton = findViewById(R.id.down);
            NestedScrollView nestedScrollView = findViewById(R.id.scrollView);


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
                            ownercontactNumber = intent.getStringExtra("ownercontactNumber");
                            System.out.println("sfds " +ownercontactNumber);
                            fetchuserorders(buyerContactNumber, orderkey, ownercontactNumber);
                            DatabaseReference orderRef =databaseRef.child(ownercontactNumber).child("orders").child(buyerContactNumber);
//                            orderRef.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//                                    if (snapshot.exists()){
//                                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
//                                            String orderkey = dataSnapshot1.getKey();
//                                            if (orderkey.contains("RX")){
//                                                fetchuserorders(buyerContactNumber, orderkey, ownercontactNumber);
//                                            }
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//                                }
//                            });

                          //  fetchOrderHistoryAndChatMessages(buyerContactNumber, orderkey);

                           // fetchOrderHistory(buyerContactNumber,orderkey);

                            callImageView.setTag(buyerContactNumber);
                            callImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Get the phone number from the tag
                                    String phoneNumber = (String) callImageView.getTag();

                                    // Create an Intent to initiate a phone call
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ownercontactNumber));

                                    // Start the phone call activity
                                    startActivity(callIntent);
                                }
                            });

                            buttoncard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String message = sendedittext.getText().toString().trim();
                                    long clickTime = System.currentTimeMillis();

                                    if ( !TextUtils.isEmpty(message.trim())) {
                                        // Check if the message is not empty before saving
                                            sendMessageToChatsNode(message, buyerContactNumber, orderkey, clickTime);
                                            sendMessageTochatsNode(message, buyerContactNumber, orderkey, clickTime);
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
                         //   chatbox.setVisibility(View.VISIBLE);
                            //  sendedittext.setVisibility(View.VISIBLE);
                        } else {
                         //   chatbox.setVisibility(View.GONE);
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
            layoutManager.setStackFromEnd(true); // This will display data from the bottom
            recyclerView.setLayoutManager(layoutManager);
            // Create sample data for order history and chat messages
            List<Order> orderHistoryList = createSampleOrderHistory();
            chatMessageList = createSampleChatMessages();
            combinedList = new ArrayList<>();
            combinedList.addAll(orderHistoryList);
            combinedList.addAll(chatMessageList);

            // Combine the adapters into a single CombinedAdapter
            combinedAdapter = new CombinedAdapter(combinedList, sharedPreference, this);

            recyclerView.setAdapter(combinedAdapter);

            // Fetch and display chat messages
            //    fetchChatMessages();
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

// Set up Firebase reference for messages
            DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");

// Attach a ValueEventListener to listen for new messages
            messagesRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                    // Deserialize the new message from dataSnapshot
                    ChatMessage newMessage = dataSnapshot.getValue(ChatMessage.class); // Assuming Message is your data model

                    // Add the new message to your local data source
                    chatMessageList.add(newMessage);

                    // Notify the adapter that a new item has been inserted
                    combinedAdapter.notifyItemInserted(chatMessageList.size() - 1);

                    // Scroll to the bottom of the RecyclerView
                    recyclerView.scrollToPosition(chatMessageList.size() - 1);
                }

                @Override
                public void onChildChanged(@androidx.annotation.NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@androidx.annotation.NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@androidx.annotation.NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                }

                // Implement other ChildEventListener methods (onChildChanged, onChildRemoved, etc.) as needed
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

//    @Override
//    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
//        // Deserialize the new message from dataSnapshot
//        ChatMessage newMessage = dataSnapshot.getValue(ChatMessage.class); // Assuming Message is your data model
//
//        // Add the new message to your local data source
//        chatMessageList.add(newMessage);
//
//        // Notify the adapter that a new item has been inserted
//        combinedAdapter.notifyItemInserted(chatMessageList.size() - 1);
//
//        // Scroll to the bottom of the RecyclerView
//        recyclerView.scrollToPosition(chatMessageList.size() - 1);
//    }

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
// Format the timestamp as "hh:mm a" (e.g., "10:00 AM")
                SimpleDateFormat TimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                formattedTime = TimeFormat.format(new Date(clickTime));

//                 Format the timestamp as "hh:mm a" (e.g., "10:00 AM")
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ", Locale.getDefault());
                formattedDate = dateFormat.format(new Date(clickTime));

                DatabaseReference userMessageRef =databaseRef.child(ownercontactNumber).child("orders")
                        .child(buyerContactNumber).child("buttonchats").child(orderkey);

                DatabaseReference userMessagehistoryRef =databaseRef.child(receiverID).child("history")
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

        private void sendMessageTochatsNode(String message,String buyerContactNumber, String orderkey, long lastClickTime) {
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
                        String username = dataSnapshot.child("shopName").getValue(String.class);
                        receiverID = dataSnapshot.child("receiverID").getValue(String.class);
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
                    Toast.makeText(UserOrderdetails.this, "Error loading order history", Toast.LENGTH_SHORT).show();
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
                        String status = dataSnapshot.child("status").getValue(String.class);
                        if (!status.equals("cart")) {
                            Order order = dataSnapshot.getValue(Order.class);
                            String username = dataSnapshot.child("shopName").getValue(String.class);
                            receiverID = dataSnapshot.child("receiverID").getValue(String.class);
                            usernameTextView.setText(username);
                            System.out.println("adsdf " + username);
                            List<ChatMessage> chatMessageList = new ArrayList<>();
                            chatMessageList.clear();


                            if (order != null && "Placed".equals(order.getStatus()) && orderkey != null) {
                                userchatRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot dataSnapshot) {


                                        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                            String messageKey = messageSnapshot.getKey();
                                            if (!addedChatMessageKeys.contains(messageKey) && messageKey.compareTo(lastChatMessageKey) > 0) {
                                                String message = messageSnapshot.child("message").getValue(String.class);
                                                String sender = messageSnapshot.child("sender").getValue(String.class);
                                                String receiver = messageSnapshot.child("receiver").getValue(String.class);
                                                String time = messageSnapshot.child("timestamp").getValue(String.class);
                                                String datetamp = messageSnapshot.child("datetamp").getValue(String.class);
                                                String chatkey = messageSnapshot.child("chatkey").getValue(String.class);

                                                lastChatMessageKey = messageKey;

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
                                            System.out.println("erfe " + messageKey);

                                            // Check if the message key is not already added from chatRef
                                            if (!addedButtonChatMessageKeys.contains(messageKey) && messageKey.compareTo(lastButtonChatMessageKey) > 0) {

                                                String message = messageSnapshot.child("message").getValue(String.class);
                                                String sender = messageSnapshot.child("sender").getValue(String.class);
                                                String receiver = messageSnapshot.child("receiver").getValue(String.class);
                                                String time = messageSnapshot.child("timestamp").getValue(String.class);
                                                String datetamp = messageSnapshot.child("datetamp").getValue(String.class);
                                                String chatkey = messageSnapshot.child("chatkey").getValue(String.class);

                                                lastButtonChatMessageKey = messageKey;

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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                    Toast.makeText(UserOrderdetails.this, "Error loading order history", Toast.LENGTH_SHORT).show();
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
                        receiverID = dataSnapshot.child("receiverID").getValue(String.class);
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
                    Toast.makeText(UserOrderdetails.this, "Error loading order history", Toast.LENGTH_SHORT).show();
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
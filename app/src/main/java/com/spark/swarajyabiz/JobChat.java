package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JobChat extends AppCompatActivity {

    String userId, formattedTime, formattedDate, BusiContactNum, UserContactNum, jobId, candidateName;
    TextView companyname, jobtitle;
    CardView chatcard;
    EditText sendedittext;
    List<Message> messageList;
    JobChatAdapter adapter;
    DatabaseReference messagesRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_chat);

        companyname = findViewById(R.id.companynametext);
        jobtitle = findViewById(R.id.jobtitletext);
        chatcard = findViewById(R.id.chatcard);
        sendedittext = findViewById(R.id.sendedittext);

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

        companyname.setText(getIntent().getStringExtra("companyname"));
        jobtitle.setText(getIntent().getStringExtra("jobtitle"));
        UserContactNum = getIntent().getStringExtra("UserContactNum");
        BusiContactNum = getIntent().getStringExtra("BusiContactNum");
        jobId = getIntent().getStringExtra("jobID");
        candidateName = getIntent().getStringExtra("candidateName");
        System.out.println("BusiContactNum " +getIntent().getStringExtra("BusiContactNum"));
        System.out.println("UserContactNum " +getIntent().getStringExtra("UserContactNum"));


        messagesRef = FirebaseDatabase.getInstance().getReference("JobChats")
                .child(UserContactNum).child(jobId);

        // Initialize your RecyclerView and adapter
        RecyclerView recyclerView = findViewById(R.id.jobchatview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageList = new ArrayList<>();
        adapter = new JobChatAdapter(messageList, sharedPreference);
        recyclerView.setAdapter(adapter);

        if (candidateName != null){
            companyname.setText(candidateName);
            jobtitle.setVisibility(View.GONE);
        }
        chatcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = sendedittext.getText().toString().trim();
                long clickTime = System.currentTimeMillis();
                // Check if the message is not empty before saving
                if (!message.isEmpty()) {
                    if (candidateName != null){
                        sendMessageTochatsNode(message,BusiContactNum, clickTime);
                    }else {
                        DatabaseReference jobapplyRef = FirebaseDatabase.getInstance().getReference("JobPosts")
                                .child(BusiContactNum)
                                .child(jobId)
                                .child("Applications")
                                .child(UserContactNum);

                        jobapplyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // User has applied, send the message
                                    sendMessageToChatsNode(message, UserContactNum, clickTime);
                                } else {
                                    // User has not applied, show a toast
                                    Toast.makeText(getApplicationContext(), "Please apply to this post first", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle database error if needed
                                Toast.makeText(getApplicationContext(), "Error checking user application status", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    sendedittext.setText("");
                } else {
                    // Handle the case where the message is empty or whitespace
                    Toast.makeText(getApplicationContext(), "Message cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        retrievechat();
    }

    private void retrievechat(){
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null) {
                        messageList.add(message);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void sendMessageToChatsNode(String message,String userContactNum, long clickTime) {
        while (true) {

            SimpleDateFormat TimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            formattedTime = TimeFormat.format(new Date(clickTime));

            // Format the timestamp as "hh:mm a" (e.g., "10:00 AM")
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ", Locale.getDefault());
            formattedDate = dateFormat.format(new Date(clickTime));

            DatabaseReference userMessageRef = FirebaseDatabase.getInstance().getReference("JobChats")
                    .child(UserContactNum).child(jobId);

            System.out.println("sfjhhg " +userMessageRef.toString());
            String ChatorderKey = userMessageRef.push().getKey();
            // Create a Map to represent the message, sender, and receiver
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", message);
            messageData.put("sender", UserContactNum); // Set sender as current user ID
            messageData.put("receiver", BusiContactNum); // Set receiver as shop contact number
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

    private void sendMessageTochatsNode(String message,String busiContactNum, long clickTime) {
        while (true) {

            SimpleDateFormat TimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            formattedTime = TimeFormat.format(new Date(clickTime));

            // Format the timestamp as "hh:mm a" (e.g., "10:00 AM")
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ", Locale.getDefault());
            formattedDate = dateFormat.format(new Date(clickTime));

            DatabaseReference userMessageRef = FirebaseDatabase.getInstance().getReference("JobChats")
                    .child(UserContactNum).child(jobId);
            // System.out.println("sfjhhg " +ordersRef.toString());
            String ChatorderKey = userMessageRef.push().getKey();
            // Create a Map to represent the message, sender, and receiver
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("message", message);
            messageData.put("sender", busiContactNum); // Set sender as current user ID
            messageData.put("receiver", UserContactNum); // Set receiver as shop contact number
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

}
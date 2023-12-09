package com.spark.admin;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CombinedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> items; // A list of mixed items (Order or ChatMessage)
    private static final int VIEW_TYPE_ORDER = 0;
    private static final int VIEW_TYPE_CHAT = 1;
    private static final String USER_ID_KEY = "userID";
    private static SharedPreferences sharedPreferences;

    public CombinedAdapter(List<Object> items, SharedPreferences sharedPreferences) {
        this.items = items;
        this.sharedPreferences = sharedPreferences;
    }

    public void addMessage(ChatMessage chatMessage) {
        items.add(chatMessage);
        notifyItemInserted(items.size() - 1);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_ORDER) {
            View itemView = inflater.inflate(R.layout.ordered_list, parent, false);
            return new OrderViewHolder(itemView);
        } else {
            View itemView = inflater.inflate(R.layout.chat_message_item, parent, false);
            return new ChatViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);

        if (holder instanceof OrderViewHolder && item instanceof Order) {
            Order order = (Order) item;
            ((OrderViewHolder) holder).bind(order);
        } else if (holder instanceof ChatViewHolder && item instanceof ChatMessage) {
            ChatMessage chatMessage = (ChatMessage) item;
            ((ChatViewHolder) holder).bind(chatMessage);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);

        if (item instanceof Order) {
            return VIEW_TYPE_ORDER;
        } else if (item instanceof ChatMessage) {
            return VIEW_TYPE_CHAT;
        }

        return super.getItemViewType(position);
    }


    // ViewHolder for Order items
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView quantityTextView;
        TextView intrestedTextView;
        ImageView itemImage;
        RelativeLayout itemdetails;

        public OrderViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.ItemName);
            quantityTextView = itemView.findViewById(R.id.quantityedittext);
            itemImage = itemView.findViewById(R.id.itemimage);
            intrestedTextView = itemView.findViewById(R.id.intrestedtextview);
            itemdetails = itemView.findViewById(R.id.itemposition);
        }

        public void bind(Order order) {
            itemNameTextView.setText(order.getItemName());
            intrestedTextView.setText("I am intrested in " +order.getItemName());
//            quantityTextView.setText("Quantity: " + order.getQuantity());

            // Load and display the image using Glide (or your preferred image loading library)
            Glide.with(itemView.getContext())
                    .load(order.getFirstImageUrl())
                    .into(itemImage);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseRef = database.getReference().child("Shop");
            DatabaseReference userRef = database.getReference("Users");
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String userId = sharedPreferences.getString("mobilenumber", null);
            if (userId != null) {
                // Use the userId as needed
                System.out.println("dffvf  " + userId);
            }
            if (userId != null) {
                // userId = mAuth.getCurrentUser().getUid();
                System.out.println("dffvf  " +userId);
            }
            System.out.println("feedhf " +userId);
            if (order.getSenderID().equals(userId)) {
                // Right-aligned for messages sent by the current user
                itemdetails.setHorizontalGravity(Gravity.END);
            } else {
                // Left-aligned for messages sent by other users
                itemdetails.setHorizontalGravity(Gravity.START);
            }

        }


    }

    // ViewHolder for ChatMessage items
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView ,timetextview;
        LinearLayout chatBox;
        RelativeLayout relativeLayout;
        CardView chatcard;

        public ChatViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timetextview = itemView.findViewById(R.id.timeTextView);
            chatBox = itemView.findViewById(R.id.chatbox);
            relativeLayout = itemView.findViewById(R.id.itemchats);
            chatcard = itemView.findViewById(R.id.chatcard);
        }

        @SuppressLint("ResourceAsColor")
        public void bind(ChatMessage chatMessage) {
            messageTextView.setText(chatMessage.getMessage());
            timetextview.setText(chatMessage.getTimestamp());
            Log.d("TAG", "bind: " +chatMessage.getSender());

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseRef = database.getReference().child("Shop");
            DatabaseReference userRef = database.getReference("Users");
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String userId = sharedPreferences.getString("mobilenumber", null);
            if (userId != null) {
                // Use the userId as needed
                System.out.println("dffvf  " + userId);
            }
            System.out.println("feedhf " +userId);
            if (chatMessage.getSender().equals(userId)) {
                // Right-aligned for messages sent by the current user
                chatBox.setHorizontalGravity(Gravity.END);
                relativeLayout.setGravity(Gravity.END);
            } else {
                // Left-aligned for messages sent by other users
                chatBox.setHorizontalGravity(Gravity.START);
                relativeLayout.setGravity(Gravity.START);
            }
//            userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        String  currentUserID = dataSnapshot.child("UserID").getValue(String.class);
//                        String currentuserName = dataSnapshot.child("Name").getValue(String.class);
//
//                        System.out.println("sdfh " +currentUserID);
//                        if (chatMessage.getSender().equals(currentUserID)) {
//                            // Right-aligned for messages sent by the current user
//                          // chatBox.setHorizontalGravity(Gravity.END);
//                        } else {
//                            // Left-aligned for messages sent by other users
//                          //  chatBox.setHorizontalGravity(Gravity.START);
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//
//                }
//            });

            // Adjust the layout for right or left-aligned messages
            // Adjust the layout for right or left-aligned messages

        }
    }

    public void currentuserID(){


    }
}


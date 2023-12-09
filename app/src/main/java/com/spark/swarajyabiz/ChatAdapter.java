package com.spark.swarajyabiz;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatMessage> chatMessageList;

    public ChatAdapter(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_message_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessageList.get(position);
        holder.messageTextView.setText(chatMessage.getMessage());
        // Adjust the layout for right or left-aligned messages
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        LinearLayout chatBox;

        public ViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            chatBox = itemView.findViewById(R.id.chatbox);
        }

        public void bind(ChatMessage chatMessage) {
            messageTextView.setText(chatMessage.getMessage());
            Log.d("TAG", "bind: " +chatMessage.getSender());


//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference databaseRef = database.getReference().child("Shop");
//            DatabaseReference userRef = database.getReference("Users");
//            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            FirebaseUser currentUser = mAuth.getCurrentUser();
//            if (currentUser != null) {
//                String userId = currentUser.getUid();
//                Log.d("TAG", "Current User ID: " + userId);
//
//                // Compare the sender's ID with the current user's ID
//                if (chatMessage.getSender().equals(userId)) {
//                    // Right-aligned for messages sent by the current user
//                    chatBox.setHorizontalGravity(Gravity.END);
//                } else {
//                    // Left-aligned for messages sent by other users
//                    chatBox.setHorizontalGravity(Gravity.START);
//                }
//            }
        }

    }
}


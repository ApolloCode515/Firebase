package com.spark.swarajyabiz;

import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JobChatAdapter extends RecyclerView.Adapter<JobChatAdapter.ViewHolder> {

    private List<Message> messages;
    private static SharedPreferences sharedPreferences;

    public JobChatAdapter(List<Message> messages, SharedPreferences sharedPreferences) {
        this.messages = messages;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout here and return a ViewHolder
        // Example:
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to your ViewHolder here
        // Example:
        Message message = messages.get(position);
        holder.messageTextView.setText(message.getMessage());
        holder.timetextview.setText(message.getTimestamp());
        String userId = sharedPreferences.getString("mobilenumber", null);
        if (userId != null) {
            // Use the userId as needed
            System.out.println("dffvf  " + userId);
        }
        if (message.getSender().equals(userId)) {
            // Right-aligned for messages sent by the current user
            holder.chatBox.setHorizontalGravity(Gravity.END);
//                relativeLayout.setGravity(Gravity.END);
        } else {
            // Left-aligned for messages sent by other users
            holder.chatBox.setHorizontalGravity(Gravity.START);
            //       relativeLayout.setGravity(Gravity.START);

        }

//        holder.senderTextView.setText(message.getSender());
        // Set other views accordingly
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare your item views here
        TextView messageTextView;
        TextView timetextview;
        LinearLayout chatBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your item views here
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timetextview = itemView.findViewById(R.id.timeTextView);
            chatBox = itemView.findViewById(R.id.chatbox);
            // Find other views by ID and initialize them
        }
    }
}

package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
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
    private int lastMessagePosition = -1;
    int targetPosition = -1; // Initialize with an invalid position
    private RecyclerView recyclerView;
    OrderClickListener orderClickListener;

    public CombinedAdapter(List<Object> items, SharedPreferences sharedPreferences, OrderClickListener orderClickListener ) {
        this.items = items;
        this.sharedPreferences = sharedPreferences;
        this.recyclerView = recyclerView;
        this.orderClickListener = orderClickListener;
    }

    public void addMessage(ChatMessage chatMessage) {
        items.add(chatMessage);
        notifyItemInserted(items.size() - 1);

        // Scroll to the newly added item
        recyclerView.smoothScrollToPosition(items.size() - 1);
    }

    public int getPositionByChatOrderKey(String chatorderkey) {
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (item instanceof ChatMessage) {
                ChatMessage chatMessage = (ChatMessage) item;
                if (chatMessage.getChatkey().equals(chatorderkey)) {
                    return i;
                }
            }
        }
        return -1; // Chatorderkey not found in the dataset
    }

    public interface OrderClickListener {
        void onSubmitClick(int position);
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

//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        Object item = items.get(position);
//        lastMessagePosition = position;
//
//        if (holder instanceof OrderViewHolder && item instanceof Order) {
//            Order order = (Order) item;
//            ((OrderViewHolder) holder).bind(order, position,orderClickListener);
//        } else if (holder instanceof ChatViewHolder && item instanceof ChatMessage) {
//            ChatMessage chatMessage = (ChatMessage) item;
//            ((ChatViewHolder) holder).bind(chatMessage, position,lastMessagePosition);
//        }
//    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);
        lastMessagePosition = position;

        if (holder instanceof OrderViewHolder && item instanceof Order) {
            Order order = (Order) item;
            ((OrderViewHolder) holder).bind(order, position, orderClickListener);
        } else if (holder instanceof ChatViewHolder && item instanceof ChatMessage) {
            ChatMessage chatMessage = (ChatMessage) item;
            ((ChatViewHolder) holder).bind(chatMessage, position, lastMessagePosition);
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
        TextView timetextview, datetextview;
        RelativeLayout itemdetails;
        CardView cardView;
        Button submitbutton;

        public OrderViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.ItemName);
            quantityTextView = itemView.findViewById(R.id.quantityedittext);
            itemImage = itemView.findViewById(R.id.itemimage);
            intrestedTextView = itemView.findViewById(R.id.intrestedtextview);
            itemdetails = itemView.findViewById(R.id.itemposition);
            timetextview = itemView.findViewById(R.id.timetextview);
           // datetextview = itemView.findViewById(R.id.datetextview);
            cardView = itemView.findViewById(R.id.chatcard);
           // submitbutton= itemView.findViewById(R.id.submitButton);
        }

        public void bind(Order order, int position, OrderClickListener orderClickListener) {
            itemNameTextView.setText(order.getItemName());
           // datetextview.setText((CharSequence) order.getDatetamp());
            timetextview.setText((CharSequence) order.getTimestamp());
            // Check if the current message is the last one sent at the same time


//            submitbutton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    orderClickListener.onSubmitClick(position);
//                }
//            });

            intrestedTextView.setText("I am intrested in " +order.getItemName());


//            quantityTextView.setText("Quantity: " + order.getQuantity());

            // Load and display the image using Glide (or your preferred image loading library)
            Glide.with(itemView.getContext())
                    .load(order.getFirstImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .transition(DrawableTransitionOptions.withCrossFade())
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
                timetextview.setGravity(Gravity.END);
            } else {
                // Left-aligned for messages sent by other users
                itemdetails.setHorizontalGravity(Gravity.START);
            }

            Log.d("fsdfgsdfghsf","1 "+order.getItemName());
            System.out.println("fsdfgsdfghsf  " +order.getItemName());

            Toast.makeText(itemView.getContext(), " "+order.getItemName(), Toast.LENGTH_SHORT).show();
            Toast.makeText(itemView.getContext(), "usdr1 "+userId, Toast.LENGTH_SHORT).show();

        }


    }

    // ViewHolder for ChatMessage items
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView ,timetextview, datetextview;
        LinearLayout chatBox;
        FrameLayout frameLayout;
        RelativeLayout relativeLayout;
        CardView chatcard;

        public ChatViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timetextview = itemView.findViewById(R.id.timeTextView);
            datetextview = itemView.findViewById(R.id.datetextview);
            chatBox = itemView.findViewById(R.id.chatbox);
            frameLayout = itemView.findViewById(R.id.frameLayout);
         //   relativeLayout = itemView.findViewById(R.id.itemchats);
            chatcard = itemView.findViewById(R.id.chatcard);
        }


        public void bind(ChatMessage chatMessage, int position, int lastmessageposition) {
            messageTextView.setText(chatMessage.getMessage());

            datetextview.setText(chatMessage.getDatetamp());
            // Check if the current message is the last one sent at the same time
            if (position == lastmessageposition) {
                timetextview.setVisibility(View.VISIBLE);
                timetextview.setText((CharSequence) chatMessage.getTimestamp());
            } else {
                timetextview.setVisibility(View.GONE);
            }

            Log.d("TAG", "bind: " +chatMessage.getSender());

            Log.d("fsdfgsdfghsf","2 "+chatMessage.getMessage());
            System.out.println("fsdfgsdfghsf[  ]" +chatMessage.getMessage());


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseRef = database.getReference().child("Shop");
            DatabaseReference userRef = database.getReference("Users");
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String userId = sharedPreferences.getString("mobilenumber", null);
            if (userId != null) {
                // Use the userId as needed
                System.out.println("dffvf  " + userId);
            }
            System.out.println("feedhf " +chatMessage.getMessage());
            if (chatMessage.getSender().equals(userId)) {
                // Right-aligned for messages sent by the current user
                chatBox.setHorizontalGravity(Gravity.END);
//                relativeLayout.setGravity(Gravity.END);
            } else {
                // Left-aligned for messages sent by other users
                chatBox.setHorizontalGravity(Gravity.START);
         //       relativeLayout.setGravity(Gravity.START);

            }

            Toast.makeText(itemView.getContext(), "chat "+chatMessage.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(itemView.getContext(), "usdr "+userId, Toast.LENGTH_SHORT).show();
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
}


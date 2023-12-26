package com.spark.admin;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.OrderKeyViewHolder> {

        private List<OrderKey> orderKeys;

        public MessagesAdapter(List<OrderKey> orderKeys) {
            this.orderKeys = orderKeys;
        }

        @NonNull
        @Override
        public OrderKeyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_list, parent, false);
            return new OrderKeyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderKeyViewHolder holder, int position) {
            OrderKey orderKey = orderKeys.get(position);
            holder.bind(orderKey);

            // Handle item click
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the selected order key
                    String selectedOrderKey = orderKey.getKey();
                    System.out.println("efhfed "+selectedOrderKey);
                    Order selectedOrder = orderKey.getOrder();
                    System.out.println("efd " +selectedOrder);

                    // Create an Intent to open the OrderDetailsActivity
                    Intent intent = new Intent(v.getContext(), OrderDetails.class);
                    intent.putExtra("orderKey", orderKey.getKey());
                    intent.putExtra("order", orderKey.getOrder());
                    v.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return orderKeys.size();
        }

        public static class OrderKeyViewHolder extends RecyclerView.ViewHolder {
            private TextView orderKeyTextView;

            public OrderKeyViewHolder(@NonNull View itemView) {
                super(itemView);
                orderKeyTextView = itemView.findViewById(R.id.textViewcontactnumbers); // Replace with your TextView ID
            }

            public void bind(OrderKey orderKey) {
                orderKeyTextView.setText(orderKey.getKey());
            }
        }


//    private List<Order> orderList;
//
//
//    // Constructor to initialize the adapter with the order data
//    public MessagesAdapter(List<Order> orderList) {
//        this.orderList = orderList;
//
//    }
//
//    public interface OrderClickListener {
//        void onRemoveClick(int position);
//
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // Inflate your item layout here (e.g., R.layout.order_history_item)
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.messages_list, parent, false);
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        // Bind data to your ViewHolder's views here
//        Order order = orderList.get(position);
//        holder.itemNameTextView.setText(order.getBuyerContactNumber());
////        holder.quantityTextView.setText(order.getQuantity());
////        holder.NameTextView.setText(order.getBuyerName());
////        holder.dateTextView.setText(order.getDatetamp().toString());
////
////
////        // Load and display the image using Glide
//        Glide.with(holder.itemView.getContext())
//                .load(order.getFirstImageUrl())
//                .into(holder.shopimage);
//
//        holder.messagelist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int clickedPosition = holder.getAdapterPosition();
//                if (clickedPosition != RecyclerView.NO_POSITION) {
//                    Order clickedItem = orderList.get(clickedPosition);
//
//                    // Create an Intent to open the ItemDetailsActivity
//                    Intent intent = new Intent(view.getContext(), ItemInformation.class);
//                    intent.putExtra("itemName", clickedItem.getItemName());
//                    intent.putExtra("firstImageUrl", clickedItem.getFirstImageUrl());
//                    intent.putExtra("quantity", clickedItem.getQuantity());
//                    intent.putExtra("timestamp", clickedItem.getTimestamp().toString());
//                  //  intent.putExtra("firstImageUrl", clickedItem.getFirstImageUrl());
//
//                    //  IntentDataHolder.setSharedIntent(intent);
//                    // intent.putExtra("itemImage", clickedItem.getImageUrl());
////                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    context.startActivity(intent);
//                    // Start the activity
//                    IntentItemDataHolder.setSharedItemIntent(intent);
//                    view.getContext().startActivity(intent);
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return orderList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView itemNameTextView;
////        TextView quantityTextView;
////        TextView NameTextView;
////        TextView dateTextView;
////        ImageView removeImageView;
//         ImageView shopimage;
//         CardView messagelist;
//
//        // Add references to other views in your item layout
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            itemNameTextView = itemView.findViewById(R.id.textViewcontactnumbers);
//            messagelist = itemView.findViewById(R.id.messagelist);
////            NameTextView = itemView.findViewById(R.id.Myorder_name);
////            quantityTextView = itemView.findViewById(R.id.myorder_Quantity_number);
////            dateTextView = itemView.findViewById(R.id.myorder_textView_Date);
//              shopimage = itemView.findViewById(R.id.shop_image);
////            removeImageView = itemView.findViewById(R.id.remove);
//            //button1 = findViewById(R.id.button1);
//        }
//    }
}



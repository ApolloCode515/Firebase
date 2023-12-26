package com.spark.swarajyabiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<Request> requestList;
    private RequestListener requestListener;
    private DatabaseReference requestsRef;
    String contactNumber;
    String userID;
    public RequestAdapter(List<Request> requestList, RequestListener requestListener, String contactNumber) {

        this.requestList = requestList;
        this.contactNumber = contactNumber;
        this.requestListener = requestListener;
        this.requestsRef = FirebaseDatabase.getInstance().getReference().child("Shop").child(contactNumber).child("requests");

    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_request, parent, false);
        return new RequestViewHolder(view);

    }

    public void removeItem(int position) {

        if (position >= 0 && position < requestList.size()) {
            requestList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, requestList.size());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requestList.get(position);
        holder.bind(request);
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView shopNameTextView;
        private TextView userNameTextView;
        private Button acceptButton;
        private Button cancelButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.textViewUserName);
            shopNameTextView = itemView.findViewById(R.id.textViewShopName);
            acceptButton = itemView.findViewById(R.id.Accept);
            cancelButton = itemView.findViewById(R.id.Cancel);
            acceptButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);

        }

        public void bind(Request request) {
            String userId = request.getUserId();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userName = dataSnapshot.child("Name").getValue(String.class);
                        userNameTextView.setText(userName);
                    }
                    shopNameTextView.setText(contactNumber);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                }
            });

            // Add OnClickListener
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference requestRef = requestsRef.child(request.getUserId());
                    requestRef.setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                requestListener.onYesButtonClick(request);
                                removeItem(getAdapterPosition());
                            } else {
                                // Handle the error
                            }
                        }
                    });
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference requestRef = requestsRef.child(request.getUserId());
                    requestRef.setValue("no").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                requestListener.onNoButtonClick(request);
                                removeItem(getAdapterPosition());
                            } else {
                                // Handle the error
                            }
                        }
                    });
                }
            });
        }

        @Override
        public void onClick(View v) {
           /* int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Request request = requestList.get(position);
                DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Shop").child(shopName).child("requests").child(request.getUserId());

                if (v.getId() == R.id.Accept) {
                    requestRef.setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                requestListener.onYesButtonClick(request);
                                removeItem(getAdapterPosition());
                            } else {
                                // Handle the error
                            }
                        }
                    });
                } else if (v.getId() == R.id.Cancel) {
                    requestRef.setValue("no").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                requestListener.onNoButtonClick(request);
                                removeItem(getAdapterPosition());
                            } else {
                                // Handle the error
                            }
                        }
                    });
                }
            }*/
        }
    }





        public interface RequestListener {
        void onYesButtonClick(Request request);

        void onNoButtonClick(Request request);
    }
}

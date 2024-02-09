package com.spark.swarajyabiz;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    private List<EmployeeDetails> employeeDetailsList;
    Context context;
    private static SharedPreferences sharedPreferences;
    private OnClickListener onClickListener;
    private boolean isHomeFragment;
    String premium;

    // Constructor to initialize the dataset
    public EmployeeAdapter(List<EmployeeDetails> employeeDetailsList, Context context, SharedPreferences sharedPreferences) {
        this.employeeDetailsList = employeeDetailsList;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.onClickListener = onClickListener;
    }

    public void setHomeFragment(boolean isHomeFragment) {
        this.isHomeFragment = isHomeFragment;
        notifyDataSetChanged();
    }

    // ViewHolder class representing your list item's layout
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView candidatename, candidateemail, candidatecontact, candidatequalification, candidatestream, candidateskills;
        CardView cardView;
        Button chatbtn;
        RelativeLayout calllayout;
        // Add other TextViews for other attributes of JobDetails

        public ViewHolder(View itemView) {
            super(itemView);
            candidatename = itemView.findViewById(R.id.candidatename); // Replace with your actual TextView ID
            candidateemail = itemView.findViewById(R.id.candidateemail); // Replace with your actual TextView ID
            candidatecontact = itemView.findViewById(R.id.candidatecontact);
            candidatequalification = itemView.findViewById(R.id.candidatequalification);
            candidatestream = itemView.findViewById(R.id.candidatestream);
            candidateskills = itemView.findViewById(R.id.candidateskill);
            cardView = itemView.findViewById(R.id.cardview);
            chatbtn = itemView.findViewById(R.id.chatbtn);
            calllayout = itemView.findViewById(R.id.calllayout);
            // Initialize other TextViews here
        }
    }

    public interface OnClickListener {
        void onItemClick(int position) throws ExecutionException, InterruptedException;

        void onchatClick(int position);
    }

    public void clear() {
        employeeDetailsList.clear();
        notifyDataSetChanged();
    }

    public void setData(List<EmployeeDetails> filteredData) {
        employeeDetailsList = filteredData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your list item layout here and return a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Bind the data to the TextViews in each ViewHolder
        EmployeeDetails employeeDetails = employeeDetailsList.get(position);

        holder.candidatename.setText(employeeDetails.getCandidateName());
        holder.candidateemail.setText(employeeDetails.getCandidateEmail());

        holder.candidatequalification.setText(employeeDetails.getCandidateQualification());
        holder.candidatestream.setText(employeeDetails.getCandidateStream());
        holder.candidateskills.setText(employeeDetails.getCandidateSkills());
        // Set other attributes as needed

        String userId = sharedPreferences.getString("mobilenumber", null);
        if (userId != null) {
            // Use the userId as needed
            System.out.println("dffvf  " + userId);
        }
        // Reference to "JobPosts" without the shopcontactnumber
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        holder.calllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            boolean premium = snapshot.child("premium").getValue(boolean.class);
                            System.out.println("srdcvr " +premium);

//                    if (premium){
//                        holder.candidatecontact.setText(employeeDetails.getCandidateContactNumber());
//                    } else {
//                        String candidateContactNumber = employeeDetails.getCandidateContactNumber();
//                        if (candidateContactNumber.length() == 10) {
//                            String firstTwoChars = candidateContactNumber.substring(0, 2);
//                            String lastTwoChars = candidateContactNumber.substring(candidateContactNumber.length() - 2);
//                            String xChars = new String(new char[candidateContactNumber.length() - 4]).replace('\0', 'X');
//                            holder.candidatecontact.setText(firstTwoChars + xChars + lastTwoChars);
//                        }
//
//                    }

                            if (premium){
                                String candidateContactNumber = employeeDetails.getCandidateContactNumber();
                                openDialerWithPhoneNumber(candidateContactNumber, position);
                            }else {
                                Intent intent = new Intent(context, PremiumMembership.class);
                                context.startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                    }
                });

            }
        });

        if (isHomeFragment) {
            holder.calllayout.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParams = holder.cardView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.cardView.setLayoutParams(layoutParams);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Notify the activity or fragment about the click event
                    if (onClickListener != null) {
                        try {
                            onClickListener.onItemClick(position);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } else {
            holder.calllayout.setVisibility(View.VISIBLE);
        }

//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Toggle visibility of workplace layout
//                if (holder.workplacelay.getVisibility() == View.VISIBLE) {
//                    holder.workplacelay.setVisibility(View.GONE);
//                } else {
//                    holder.workplacelay.setVisibility(View.VISIBLE);
//                }
//
//                // Toggle visibility of job type layout
//                if (holder.jobtypelay.getVisibility() == View.VISIBLE) {
//                    holder.jobtypelay.setVisibility(View.GONE);
//                } else {
//                    holder.jobtypelay.setVisibility(View.VISIBLE);
//                }
//
//                // Toggle visibility of description layout
//                if (holder.descriptionlay.getVisibility() == View.VISIBLE) {
//                    holder.descriptionlay.setVisibility(View.GONE);
//                } else {
//                    holder.descriptionlay.setVisibility(View.VISIBLE);
//                }
//            }
//        });

//        holder.calllayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Notify the activity or fragment about the click event
//                if (onClickListener != null) {
//                    onClickListener.onchatClick(position);
//
//                }
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return employeeDetailsList != null ? employeeDetailsList.size() : 0;
    }

    public void openDialerWithPhoneNumber(String phoneNumber, int position) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            // Handle the case where the dialer app is not available
            // Toast.makeText(NotificationAdapter.this, "Dialer app not found.", Toast.LENGTH_SHORT).show();
        }
    }
}



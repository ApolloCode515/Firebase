package com.spark.swarajyabiz;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ApplicationDetailsAdapter extends RecyclerView.Adapter<ApplicationDetailsAdapter.ViewHolder> {

    private List<CandidateDetials> candidateDetialsList;
    Context context;
    private static SharedPreferences sharedPreferences;
    private OnClickListener onClickListener;

    // Constructor to initialize the dataset
    public ApplicationDetailsAdapter(List<CandidateDetials> candidateDetialsList, Context context, SharedPreferences sharedPreferences, OnClickListener onClickListener) {
        this.candidateDetialsList = candidateDetialsList;
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.onClickListener = onClickListener;
    }

    // ViewHolder class representing your list item's layout
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView jobTitleTextView, datetextview;
        public TextView candidatename, candidateemail, candidatecontact, candidatequalification, candidatestream, candidtateskills;
        RelativeLayout workplacelay, jobtypelay, descriptionlay;
        CardView cardView;
        ImageView deleteimageview;
        Button chatbtn;
        // Add other TextViews for other attributes of JobDetails

        public ViewHolder(View itemView) {
            super(itemView);
            candidatename = itemView.findViewById(R.id.candidatenametextview); // Replace with your actual TextView ID
            candidateemail = itemView.findViewById(R.id.candidateemailtextview); // Replace with your actual TextView ID
            candidatecontact = itemView.findViewById(R.id.candidatecontacttextview);
            candidatequalification = itemView.findViewById(R.id.candidatequalificationtextview);
            candidatestream = itemView.findViewById(R.id.candidatestreamtextview);
            candidtateskills = itemView.findViewById(R.id.candidateskillstextview);
            cardView = itemView.findViewById(R.id.cardview);
            datetextview = itemView.findViewById(R.id.datetextview);
            chatbtn = itemView.findViewById(R.id.chatbtn);
//            workplacelay = itemView.findViewById(R.id.workplacelay);
//            jobtypelay = itemView.findViewById(R.id.jobtypelay);
//            descriptionlay = itemView.findViewById(R.id.descriptionlay);
//            deleteimageview = itemView.findViewById(R.id.deleteimageview);
//
            // Initialize other TextViews here
        }
    }

    public interface OnClickListener {
        void onChatClick(int position, String condidateName, String UserContactNum) throws ExecutionException, InterruptedException;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your list item layout here and return a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidate__details_lists, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Bind the data to the TextViews in each ViewHolder
        CandidateDetials currentJobDetails = candidateDetialsList.get(position);
        holder.candidatename.setText(currentJobDetails.getCandidateName());
        holder.candidateemail.setText(currentJobDetails.getCandidateEmail());
        holder.candidatecontact.setText(currentJobDetails.getCandidateContactNumber());
        holder.candidatequalification.setText(currentJobDetails.getCandidateQualification());

        holder.candidtateskills.setText(currentJobDetails.getCandidateSkills());
        holder.datetextview.setText(currentJobDetails.getAppliedon());
        // Set other attributes as needed

        String candidatestream = currentJobDetails.getCandidateStream();
        if (candidatestream != null){
            holder.candidatestream.setVisibility(View.VISIBLE);
            holder.candidatestream.setText(currentJobDetails.getCandidateStream());
        } else{
            holder.candidatestream.setVisibility(View.GONE);
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of workplace layout
                if (holder.workplacelay.getVisibility() == View.VISIBLE) {
                    holder.workplacelay.setVisibility(View.GONE);
                } else {
                    holder.workplacelay.setVisibility(View.VISIBLE);
                }

                // Toggle visibility of job type layout
                if (holder.jobtypelay.getVisibility() == View.VISIBLE) {
                    holder.jobtypelay.setVisibility(View.GONE);
                } else {
                    holder.jobtypelay.setVisibility(View.VISIBLE);
                }

                // Toggle visibility of description layout
                if (holder.descriptionlay.getVisibility() == View.VISIBLE) {
                    holder.descriptionlay.setVisibility(View.GONE);
                } else {
                    holder.descriptionlay.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onClickListener.onChatClick(position, currentJobDetails.getCandidateName(), currentJobDetails.getCandidateContactNumber() );
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Notify the activity or fragment about the click event
//                if (onClickListener != null) {
//                    try {
//                        onClickListener.onItemClick(position);
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });


        // Check if the context is AllJobPosts
//        if (context instanceof AllJobPosts) {
//            holder.deleteimageview.setVisibility(View.VISIBLE);
//            holder.deleteimageview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Display an AlertDialog with "Delete" and "Cancel" buttons
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Delete Job Post");
//                    builder.setMessage("Are you sure you want to delete this job post?");
//                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Perform delete operation here
//                           // deleteJobPost(holder.getAdapterPosition());
//                        }
//                    });
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Cancel button clicked, do nothing
//                        }
//                    });
//                    builder.show();
//                }
//            });
//        } else {
//            holder.deleteimageview.setVisibility(View.GONE);
//        }

    }

    @Override
    public int getItemCount() {
        return candidateDetialsList.size();
    }



//    private void deleteJobPost(int position) {
//        // Get the JobDetails at the clicked position
//        JobDetails deletedJobDetails = candidateDetialsList.get(position);
//
//        String userId = sharedPreferences.getString("mobilenumber", null);
//        if (userId != null) {
//            // Use the userId as needed
//            System.out.println("dffvf  " + userId);
//        }
//
//        // Reference to "JobPosts" without the shopcontactnumber
//        DatabaseReference jobPostsRef = FirebaseDatabase.getInstance().getReference().child("JobPosts");
//
//        // Using the push key for both locations
//        String pushKey = deletedJobDetails.getJobkey();
//
//        // Remove the job post from both locations
//        DatabaseReference jobRefWithPushKey = FirebaseDatabase.getInstance().getReference("Shop").child(userId).child("JobPosts").child(pushKey);
//        DatabaseReference jobPostsRefWithPushKey = jobPostsRef.child(pushKey);
//
//        jobRefWithPushKey.removeValue();
//        jobPostsRefWithPushKey.removeValue();
//
//        // Remove the item from the RecyclerView
//        jobDetailsList.remove(position);
//        notifyItemRemoved(position);
//    }

}



package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class JobPostAdapter extends RecyclerView.Adapter<JobPostAdapter.ViewHolder> {

    private List<JobDetails> jobDetailsList;
    Context context;
    private static SharedPreferences sharedPreferences;
    private OnClickListener onClickListener;
    private boolean isHomeFragment;

    // Constructor to initialize the dataset
    public JobPostAdapter(List<JobDetails> jobDetailsList, Context context, SharedPreferences sharedPreferences,
                          OnClickListener onClickListener) {
        this.jobDetailsList = jobDetailsList;
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
        public TextView jobTitleTextView, datetextview;
        public TextView companyNameTextView, joblocationtextview, workplacetextview, jobtypetextview, descriptiontextview;
        RelativeLayout workplacelay, jobtypelay, descriptionlay;
        CardView cardView;
        ImageView deleteimageview;
        Button applybtn, chatbtn;
        // Add other TextViews for other attributes of JobDetails

        public ViewHolder(View itemView) {
            super(itemView);
            jobTitleTextView = itemView.findViewById(R.id.jobtitletextview); // Replace with your actual TextView ID
            companyNameTextView = itemView.findViewById(R.id.companytextview); // Replace with your actual TextView ID
            joblocationtextview = itemView.findViewById(R.id.joblocationtextview);
            workplacetextview = itemView.findViewById(R.id.workplacetextview);
            jobtypetextview = itemView.findViewById(R.id.jobtypetextview);
            descriptiontextview = itemView.findViewById(R.id.descriptiontextview);
            cardView = itemView.findViewById(R.id.cardview);
            workplacelay = itemView.findViewById(R.id.workplacelay);
            jobtypelay = itemView.findViewById(R.id.jobtypelay);
            descriptionlay = itemView.findViewById(R.id.descriptionlay);
            deleteimageview = itemView.findViewById(R.id.deleteimageview);
            datetextview = itemView.findViewById(R.id.datetextview);
            applybtn = itemView.findViewById(R.id.applyjobbtn);
            chatbtn = itemView.findViewById(R.id.chatbtn);
            // Initialize other TextViews here
        }
    }

    public interface OnClickListener {
        void onItemClick(int position) throws ExecutionException, InterruptedException;
        void onchatClick(int position);
    }

    public void clear() {
        jobDetailsList.clear();
        notifyDataSetChanged();
    }

    public void setData(List<JobDetails> filteredData) {
        jobDetailsList = filteredData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your list item layout here and return a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_post_lists, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Bind the data to the TextViews in each ViewHolder
        JobDetails currentJobDetails = jobDetailsList.get(position);
        holder.jobTitleTextView.setText(currentJobDetails.getJobtitle());
        holder.companyNameTextView.setText(currentJobDetails.getCompanyname());
        holder.joblocationtextview.setText(currentJobDetails.getJoblocation());
        holder.workplacetextview.setText(currentJobDetails.getWorkplacetype());
        holder.jobtypetextview.setText(currentJobDetails.getJobtype());
        holder.descriptiontextview.setText(currentJobDetails.getDescription());
        holder.datetextview.setText(currentJobDetails.getCurrentdate());
        // Set other attributes as needed

        if (isHomeFragment) {
            holder.applybtn.setVisibility(View.GONE);
            holder.chatbtn.setVisibility(View.GONE);
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

        } else{
            holder.applybtn.setVisibility(View.VISIBLE);
            holder.chatbtn.setVisibility(View.VISIBLE);
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

        holder.applybtn.setOnClickListener(new View.OnClickListener() {
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

        holder.chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the activity or fragment about the click event
                if (onClickListener != null) {
                        onClickListener.onchatClick(position);

                }
            }
        });


        // Check if the context is AllJobPosts
        if (context instanceof AllJobPosts) {
            holder.deleteimageview.setVisibility(View.VISIBLE);
            holder.deleteimageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Display an AlertDialog with "Delete" and "Cancel" buttons
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete Job Post");
                    builder.setMessage("Are you sure you want to delete this job post?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Perform delete operation here
                            deleteJobPost(holder.getAdapterPosition());
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel button clicked, do nothing
                        }
                    });
                    builder.show();
                }
            });
        } else {
            holder.deleteimageview.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return jobDetailsList != null ? jobDetailsList.size() : 0;
    }



    private void deleteJobPost(int position) {
        // Get the JobDetails at the clicked position
        JobDetails deletedJobDetails = jobDetailsList.get(position);
        String jobId = deletedJobDetails.getJobID();

        String userId = sharedPreferences.getString("mobilenumber", null);
        if (userId != null) {
            // Use the userId as needed
            System.out.println("dffvf  " + userId);
        }

        // Reference to "JobPosts" without the shopcontactnumber
        DatabaseReference jobPostsRef = FirebaseDatabase.getInstance().getReference().child("JobPosts");

        // Using the push key for both locations
        String pushKey = deletedJobDetails.getJobkey();

        // Remove the job post from both locations
        DatabaseReference jobRefWithPushKey = FirebaseDatabase.getInstance().getReference("JobPosts")
                .child(userId).child(jobId);

        jobRefWithPushKey.removeValue();

        // Remove the item from the RecyclerView
        jobDetailsList.remove(position);
        notifyItemRemoved(position);
    }

}


package com.spark.swarajyabiz;


import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AllReferralAdapter extends RecyclerView.Adapter<AllReferralAdapter.ReferralViewHolder> {

    private List<String> referrallist;
    private List<String> referralmsg;
    SharedPreferences sharedPreferences;

    public AllReferralAdapter(List<String> referrallist,List<String>referralmsg, SharedPreferences sharedPreferences) {
        this.referrallist = referrallist;
        this.referralmsg = referralmsg;
        this.sharedPreferences = sharedPreferences;
    }

    public void setReferralList(List<String> referrallist) {
        this.referrallist = referrallist;
        notifyDataSetChanged();
    }

    public void setReferralmsg(List<String> referralmsg){
        this.referralmsg = referralmsg;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReferralViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_referral_list, parent, false);
        return new ReferralViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferralViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String Referrallist = referrallist.get(position);
        String Referralmsg = referralmsg.get(position);
        holder.referralkeytextview.setText(Referralmsg);
       // holder.referralmsgtextview.setText(Referralmsg);

        // Reset text to default (e.g., "Not Installed")
        holder.referralmsgtextview.setText("Not Installed");

        // Check if referral key is installed or premium
        referralInstallOrPremiumCheck(holder.referralmsgtextview, Referralmsg);

    }

    @Override
    public int getItemCount() {
        return referralmsg.size();
    }

    public static class ReferralViewHolder extends RecyclerView.ViewHolder {

        private TextView referralkeytextview, referralmsgtextview;
        private ImageView deletematter;

        public ReferralViewHolder(@NonNull View itemView) {
            super(itemView);
            referralkeytextview = itemView.findViewById(R.id.referralkeytext);
            referralmsgtextview = itemView.findViewById(R.id.referralmsgtext);
        }

    }

    private void referralInstallOrPremiumCheck(TextView referralmsgtextview, String referralKey) {
        String userId = sharedPreferences.getString("mobilenumber", null);
        if (userId != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
            DatabaseReference allReferralRef = FirebaseDatabase.getInstance().getReference("Referral").child(userId);

            allReferralRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String currentReferralKey = dataSnapshot.getValue(String.class);

                            // Check if the currentReferralKey matches the referralKey
                            if (currentReferralKey != null && currentReferralKey.equals(referralKey)) {
                                // Referral key matches, check premium status and update text
                                checkReferralKeyInUserRef(userRef, referralKey, referralmsgtextview);
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });
        }
    }
// Inside the AllReferralAdapter class

    private void checkReferralKeyInUserRef(DatabaseReference userRef, String referralKey,
                                           TextView referralmsgtextview) {
        userRef.child(referralKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("sdvrf " + referralKey);
                    referralmsgtextview.setText("Installed");
                    referralmsgtextview.setTextColor(Color.GREEN);
                    // Check premium status and update the text accordingly
                    checkPremiumStatus(userRef.child(referralKey), referralmsgtextview);
                } else {
                    System.out.println("refg werd " + referralKey);

                    // Referral key doesn't exist in userRef, set text to "Not Installed"
                    referralmsgtextview.setText("Not Installed");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void checkPremiumStatus(DatabaseReference referralUserRef, TextView referralmsgtextview) {
        // Assuming that "premium" is a boolean field in the userRef node
        System.out.println("sdvds " +referralUserRef);
        referralUserRef.child("premium").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Boolean premium = snapshot.child("premium").getValue(Boolean.class);

                    // Update text based on premium status
                    if (premium != null && premium.equals(true)) {
                        referralmsgtextview.setText("Subscribe");
                        referralmsgtextview.setTextColor(Color.RED);
                    }
                }
                // Otherwise, keep it as "Installed" (default)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

}


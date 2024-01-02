package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;
import java.util.List;

public class AllReferrals extends AppCompatActivity {

    ImageView back;
    DatabaseReference allreferralRef;
    String userId;
    RecyclerView recyclerView;
    AllReferralAdapter allReferralAdapter;
    List<String> referralkeys, referralmsgs;
    CardView walletcard;
    TextView wallettextview, UPItextview;
    ShimmerTextView proTextView;
    AlertDialog alertDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_referrals);

        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.allreferralview);
        walletcard = findViewById(R.id.walletCard);
        wallettextview = findViewById(R.id.wallettextview);
        UPItextview = findViewById(R.id.UPItextview);
        proTextView = findViewById(R.id.proTag);
        SpannableString spannableString = new SpannableString("\u20B9100 ");
        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_account_balance_wallet_24);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(imageSpan, spannableString.length() - 1, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

// Set the SpannableString to the ShimmerTextView
        proTextView.setText(spannableString);
        Shimmer shimmer = new Shimmer();
        shimmer.start(proTextView);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
            View decorsView = window.getDecorView();
            // Make the status bar icons dark
            decorsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

        referralkeys = new ArrayList<>();
        referralmsgs = new ArrayList<>();
        allreferralRef = FirebaseDatabase.getInstance().getReference("Referral").child(userId);
        System.out.println("refhvun " +allreferralRef);
        allReferralAdapter = new AllReferralAdapter(referralkeys, referralmsgs, sharedPreference);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(allReferralAdapter);
        retrivereferrals();

        UPItextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void retrivereferrals() {
        allreferralRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int count = 0; // Initialize count variable

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String referralmsg = dataSnapshot.getValue(String.class);
                        System.out.println("ewsdvc " + referralmsg);

                        if (referralmsg.equals("You got ₹ 10") || referralmsg.equals("Subscribe")) {
                            // Increment count for each occurrence of "You got ₹10" or "Subscribe"
                            count++;
                        }

                        String referralkey = dataSnapshot.getKey();
                        referralkeys.add(referralkey);
                        referralmsgs.add(referralmsg);
                    }

                    // Multiply count by 10 and set the result to wallettextview
                    proTextView.setText("₹ " + (count * 10));

                    allReferralAdapter.setReferralList(referralkeys);
                    allReferralAdapter.setReferralmsg(referralmsgs);
                    allReferralAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Initialize the layout inflater
        // Inflate the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.upi_alert_dialog, null);
        builder.setView(customLayout);


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        EditText upiKeyEditText = customLayout.findViewById(R.id.UPIedittext);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button saveButton = customLayout.findViewById(R.id.savebtn);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button editButton = customLayout.findViewById(R.id.editbtn);


        // Retrieve the current UPI key from Firebase and set it to the EditText
        // Replace "yourUserId" with the actual user ID
        DatabaseReference upiKeyRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("upiKey");
        upiKeyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String currentUpiKey = dataSnapshot.getValue(String.class);
                    upiKeyEditText.setText(currentUpiKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUpiKey = upiKeyEditText.getText().toString().trim();
                upiKeyRef.setValue(newUpiKey);
                upiKeyEditText.setEnabled(false);
                alertDialog.dismiss();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upiKeyEditText.setEnabled(true);
                String newUpiKey = upiKeyEditText.getText().toString().trim();
                upiKeyRef.setValue(newUpiKey);
            }
        });

        // Show the AlertDialog
        alertDialog = builder.create();
        alertDialog.show();
    }


}